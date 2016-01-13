/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
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

import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IconAndMessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.AbstractToolManager;
import melnorme.lang.ide.core.utils.operation.EclipseCancelMonitor;
import melnorme.lang.ide.ui.utils.WorkbenchUtils;
import melnorme.lang.ide.ui.utils.operations.BasicUIOperation;
import melnorme.lang.ide.ui.utils.operations.RunOperationAsJob;
import melnorme.util.swt.SWTFactory;
import melnorme.utilbox.misc.ArrayUtil;

public abstract class StartBundleDownloadOperation extends BasicUIOperation {
	
	protected final AbstractToolManager toolMgr = LangCore.getToolManager();
	
	public StartBundleDownloadOperation(String operationName) {
		super(operationName);
	}
	
	protected void startProcessUnderJob(ProcessBuilder pb, String toolLocation) {
		String cmdLineRender = DebugPlugin.renderArguments(ArrayUtil.createFrom(pb.command(), String.class), null);
		
		startProcessUnderJob(pb, cmdLineRender, toolLocation);
	}
	
	protected void startProcessUnderJob(ProcessBuilder pb, String cmdLineRender, String toolLocation) {
		boolean confirm = openIntroDialog(cmdLineRender, toolLocation);
		
		if(confirm) {
			scheduleDownloadJob(pb);
		}
	}
	
	protected boolean openIntroDialog(String cmdLineRender, String toolLocation) {
		Shell shell = WorkbenchUtils.getActiveWorkbenchShell();
		IntroDialog introDialog = new IntroDialog(shell, operationName, cmdLineRender, toolLocation);
		boolean confirm = introDialog.open() == Dialog.OK;
		return confirm;
	}
	
	protected static class IntroDialog extends IconAndMessageDialog {
		
		protected final String dialogTitle;
		protected final String commandLine;
		protected final String binLocation;
		
		public IntroDialog(Shell parentShell, String dialogTitle, String commandLine, String binLocation) {
			super(parentShell);
			this.dialogTitle = assertNotNull(dialogTitle);
			this.commandLine = assertNotNull(commandLine);
			this.binLocation = assertNotNull(binLocation);;
		}
		
		@Override
		protected void configureShell(Shell newShell) {
			super.configureShell(newShell);
			newShell.setText(dialogTitle);
		}
		
		@Override
		protected Image getImage() {
			return Display.getCurrent().getSystemImage(SWT.ICON_INFORMATION);
		}
		
		@Override
		protected Control createDialogArea(Composite parent) {
			Composite dialogArea = (Composite) super.createDialogArea(parent);
			SWTFactory.createLabel(dialogArea, SWT.NONE, 
					"The following process will be started in the background:");
			
			SWTFactory.createReadonlyText(dialogArea, commandLine, 
				GridDataFactory.fillDefaults().hint(600, SWT.DEFAULT).create());
			
			SWTFactory.createLabel(dialogArea, SWT.NONE, 
					"The executable will be located at:");
			
			SWTFactory.createReadonlyText(dialogArea, binLocation, GridDataFactory.fillDefaults().create());
			
			return dialogArea;
		}
		
	}
	
	protected void scheduleDownloadJob(ProcessBuilder pb) {
		new RunOperationAsJob(operationName, (pm) -> {
			
			inJob_handleOperationStart();
			
			toolMgr.new RunEngineClientOperation(pb, new EclipseCancelMonitor(pm)).runProcess(null);
			Display.getDefault().asyncExec(() -> afterDownloadJobCompletes_inUI());
		}).schedule();
	}
	
	protected void inJob_handleOperationStart() {
		toolMgr.startNewToolOperation(true);
	}
	
	protected void afterDownloadJobCompletes_inUI() {
	}
	
}