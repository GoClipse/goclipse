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
package melnorme.lang.ide.ui.dialogs;


import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.dialogs.PropertyPage;

import melnorme.lang.ide.ui.utils.DialogPageUtils;
import melnorme.lang.ide.ui.utils.operations.RunnableWithProgressOperationAdapter.ProgressMonitorDialogOpRunner;
import melnorme.lang.ide.ui.utils.operations.UIOperation;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.components.IValidatableWidget;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public abstract class AbstractLangPropertyPage2<WIDGET extends IValidatableWidget> extends PropertyPage {
	
	protected WIDGET preferencesWidget;
	
	public AbstractLangPropertyPage2() {
		super();
	}
	
	protected IProject getProject() {
		IAdaptable adaptable = getElement();
		if(adaptable instanceof IProject) {
			return (IProject) adaptable;
		}
		return (IProject) adaptable.getAdapter(IProject.class);
	}
	
	@Override
	public void setElement(IAdaptable element) {
		super.setElement(element);
		preferencesWidget = createProjectConfigWidget(getProject());
	}
	
	public WIDGET getPreferencesWidget() {
		return preferencesWidget;
	}
	
	protected abstract WIDGET createProjectConfigWidget(IProject project);
	
	/* -----------------  ----------------- */
	
	@Override
	protected Control createContents(Composite parent) {
		IProject project = getProject();
		if(project == null) {
			return SWTFactoryUtil.createLabel(parent, SWT.LEFT, "No project available", null);
		}
		return doCreateContents(parent, project);
	}
	
	@SuppressWarnings("unused")
	protected Control doCreateContents(Composite parent, IProject project) {
		Control component = preferencesWidget.createComponent(parent);
		
		preferencesWidget.getStatusField().registerListener(true, (__) -> updateStatusMessage());
		
		return component;
	}
	
	protected void updateStatusMessage() {
		DialogPageUtils.setPrefPageStatus(this, preferencesWidget.getStatusField().get());
	}
	
	@Override
	public final boolean performOk() {
		UIOperation op = new UIOperation("Saving project settings", this::doPerformSave) {
			@Override
			protected void executeBackgroundOperation() throws CommonException, OperationCancellation {
				// We don't use the standard WorkbenchProgressServiceOpRunner 
				// because of a bug with the workbench progess service:
				// https://bugs.eclipse.org/bugs/show_bug.cgi?id=495015
				new ProgressMonitorDialogOpRunner(getShell(), getBackgroundOperation()).execute();
			}
		};
		return op.executeAndHandle();
	}
	
	public abstract void doPerformSave(IOperationMonitor om) throws CommonException, OperationCancellation;
	
	@Override
	protected abstract void performDefaults();
	
}