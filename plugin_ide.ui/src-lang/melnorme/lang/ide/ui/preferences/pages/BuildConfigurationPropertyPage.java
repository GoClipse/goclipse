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
package melnorme.lang.ide.ui.preferences.pages;


import melnorme.lang.ide.ui.dialogs.AbstractLangPropertyPage2;
import melnorme.lang.ide.ui.preferences.ProjectBuildConfigurationComponent;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public abstract class BuildConfigurationPropertyPage 
	extends AbstractLangPropertyPage2<ProjectBuildConfigurationComponent> {
	
	public BuildConfigurationPropertyPage() {
		super();
		noDefaultAndApplyButton();
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public void applyData(Object data) {
		if(data instanceof String) {
			String targetName = (String) data;
			getPreferencesWidget().getBuildTargetField().setFieldValue(targetName);
		}
	}
	
	@Override
	public void doPerformSave(IOperationMonitor om) throws CommonException, OperationCancellation {
		getPreferencesWidget().doSaveSettings();
	}
	
	@Override
	protected void performDefaults() {
		getPreferencesWidget().loadDefaults();
	}
	
}