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

import static melnorme.utilbox.core.CoreUtil.list;

import java.nio.file.Path;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.texteditor.ITextEditor;

import com.googlecode.goclipse.tooling.env.GoEnvironment;

import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.MiscUtil;

public class RunGoFmtOperation extends AbstractEditorGoToolOperation {
	
	protected static final String RUN_GOFMT_OpName = "Run 'gofmt'";
	
	public RunGoFmtOperation(ITextEditor editor) {
		super(RUN_GOFMT_OpName, editor);
	}
	
	@Override
	protected ProcessBuilder prepareProcessBuilder(Path goSDKPath, GoEnvironment goEnv)
			throws CoreException, CommonException {
		Location gofmt = goEnv.getGoRoot_Location().resolve_fromValid("bin/gofmt" + MiscUtil.getExecutableSuffix());
		Indexable<String> cmd = list(gofmt.toString());
		return goEnv.createProcessBuilder(cmd, null, true);
	}
	
}