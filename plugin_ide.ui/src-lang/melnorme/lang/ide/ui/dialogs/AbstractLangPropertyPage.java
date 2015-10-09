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

import melnorme.lang.ide.ui.preferences.common.IPreferencesWidgetComponent;
import melnorme.util.swt.SWTFactoryUtil;

public abstract class AbstractLangPropertyPage extends PropertyPage {
	
	protected IPreferencesWidgetComponent settingsComponent;
	
	public AbstractLangPropertyPage() {
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
		settingsComponent = createProjectConfigComponent(getProject());
	}
	
	public IPreferencesWidgetComponent getSettingsComponent() {
		return settingsComponent;
	}
	
	protected abstract IPreferencesWidgetComponent createProjectConfigComponent(IProject project);
	
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
		return settingsComponent.createComponent(parent);
	}
	
	@Override
	public boolean performOk() {
		settingsComponent.saveSettings();
		return true;
	}
	
	@Override
	protected void performDefaults() {
		settingsComponent.loadDefaults();
	}
	
}