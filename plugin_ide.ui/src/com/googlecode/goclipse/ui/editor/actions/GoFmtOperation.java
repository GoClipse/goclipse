/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.ui.editor.actions;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.CoreUtil.list;

import java.nio.file.Path;

import org.eclipse.ui.texteditor.ITextEditor;

import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.core.GoToolPreferences;
import com.googlecode.goclipse.tooling.env.GoEnvironment;

import melnorme.lang.ide.ui.editor.actions.AbstractEditorToolOperation;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.lang.tooling.toolchain.ops.ToolResponse;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.MiscUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public class GoFmtOperation extends AbstractEditorToolOperation<String> {
	
	protected static final String RUN_GOFMT_OpName = "Run 'gofmt'";
	
	public GoFmtOperation(ITextEditor editor) {
		super(RUN_GOFMT_OpName, editor);
	}
	
	protected ProcessBuilder pb;
	
	@Override
	public void prepareOperation() throws CommonException {
		super.prepareOperation();
		
		GoEnvironment goEnv = GoProjectEnvironment.getGoEnvironment(project);
		pb = prepareProcessBuilder(goEnv);
	}
	
	protected ProcessBuilder prepareProcessBuilder(GoEnvironment goEnv) throws CommonException {
		Path gofmt = getGofmtLocation(goEnv);
		Indexable<String> cmd = list(gofmt.toString());
		return goEnv.createProcessBuilder(cmd, null, true);
	}
	
	protected Path getGofmtLocation(GoEnvironment goEnv) throws CommonException {
		if(GoToolPreferences.GOFMT_Path.getPreference().get() == null) {
			return getGofmtLocationFromGoRoot(goEnv.getGoRoot_Location()).toPath();
		}
		return GoToolPreferences.GOFMT_Path.getDerivedValue();
	}
	
	public static Location getGofmtLocationFromGoRoot(Location rootLocation) throws CommonException {
		return rootLocation.resolve_fromValid("bin/gofmt" + MiscUtil.getExecutableSuffix());
	}
	
	@Override
	protected ToolResponse<String> doBackgroundValueComputation(IOperationMonitor om)
			throws CommonException, OperationCancellation {
		
		ExternalProcessResult processResult = getToolService().runProcess(pb, getSource(), om);
		if(processResult.exitValue != 0) {
			return ToolResponse.newError(processResult.getStdErrBytes().toString());
		}
		
		return new ToolResponse<>(processResult.getStdOutBytes().toString());
	}
	
	@Override
	protected void handleResultData(String resultData) throws CommonException {
		assertNotNull(resultData);
		
		setEditorTextPreservingCarret(resultData);
	}
	
}