/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Bruno Medeiros - modifications, removed OverlayStore requirements
 *******************************************************************************/
package melnorme.lang.ide.ui.preferences;

import melnorme.lang.ide.ui.LangUIPlugin;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

/**
 * Abstract preference page which is used to wrap a
 * {@link melnorme.lang.ide.ui.preferences.IPreferenceConfigurationBlock2}.
 *
 * @since 3.0
 */
public abstract class AbstractConfigurationBlockPreferencePage2 extends PreferencePage 
		implements IWorkbenchPreferencePage {
	
	private IPreferenceConfigurationBlock2 fConfigurationBlock;
	
	public AbstractConfigurationBlockPreferencePage2() {
		setDescription();
		setPreferenceStore();
		fConfigurationBlock = createConfigurationBlock();
	}
	
	protected abstract void setPreferenceStore();
	protected abstract IPreferenceConfigurationBlock2 createConfigurationBlock();
	protected abstract String getHelpId();
	protected abstract void setDescription();
	
	@Override
	public void init(IWorkbench workbench) {
	}
	
	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), getHelpId());
	}
	
	@Override
	protected Control createContents(Composite parent) {
		Control content = fConfigurationBlock.createControl(parent);
		
		Dialog.applyDialogFont(content);
		return content;
	}
	
	@Override
	public boolean performOk() {
		fConfigurationBlock.performOk();
		LangUIPlugin.flushInstanceScope();
		
		return true;
	}
	
	@Override
	public void performDefaults() {
		fConfigurationBlock.performDefaults();
		
		super.performDefaults();
	}
	
	@Override
	public void dispose() {
		fConfigurationBlock.dispose();
		
		super.dispose();
	}
}