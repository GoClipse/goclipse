/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.operations;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.util.regex.Pattern;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Display;

import melnorme.lang.ide.ui.operations.StartBundleDownloadOperation;
import melnorme.lang.ide.ui.preferences.pages.DownloadToolTextField;
import melnorme.util.swt.components.FieldComponent;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public abstract class StartToolDownload_FromField extends StartBundleDownloadOperation {
	
	protected final FieldComponent<String> toolField;
	protected final String dlSource;
	protected final String exeName;
	
	public StartToolDownload_FromField(String operationName,
			DownloadToolTextField toolTextField, String gitSource, String exeName) {
		super(operationName);
		this.toolField = assertNotNull(toolTextField);
		this.dlSource = assertNotNull(gitSource);
		this.exeName = assertNotNull(exeName);
	}
	
	@Override
	protected void startProcessUnderJob(ProcessBuilder pb, String cmdLineRender, String toolLocation) {
		// Pretty command line in a more user-friendly format
		cmdLineRender = cmdLineRender.replaceAll(Pattern.quote(" --"), " \n--");
		super.startProcessUnderJob(pb, cmdLineRender, toolLocation);
	}
	
	protected String toolBinPath;
	
	@Override
	protected void doOperation() throws CoreException, CommonException, OperationCancellation {
		ProcessBuilder pb = getProcessToStart_andSetToolPath();
		assertNotNull(toolBinPath);
		
		// Dialog explaining action to run
		startProcessUnderJob(pb, toolBinPath);
	}
	
	protected abstract ProcessBuilder getProcessToStart_andSetToolPath() throws CommonException;
	
	@Override
	protected void scheduleDownloadJob(ProcessBuilder pb) {
		toolField.setFieldValue(toolBinPath);
		super.scheduleDownloadJob(pb);
	}
	
	@Override
	protected void afterDownloadJobCompletes_inUI() {
		assertTrue(Display.getCurrent() != null);
		toolField.fireFieldValueChanged();
	}
	
}