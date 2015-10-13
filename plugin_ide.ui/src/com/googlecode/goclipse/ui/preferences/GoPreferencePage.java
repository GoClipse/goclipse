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
package com.googlecode.goclipse.ui.preferences;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.osgi.framework.Version;

import com.googlecode.goclipse.core.GoCore;
import com.googlecode.goclipse.core.GoEnvironmentPrefs;

import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.preferences.common.AbstractComponentsPrefPage;
import melnorme.lang.ide.ui.preferences.common.IPreferencesDialogComponent.EnablementButtonFieldPrefAdapter;

public class GoPreferencePage extends AbstractComponentsPrefPage {
	
	protected final GoSDKConfigBlock goSDKBlock = new GoSDKConfigBlock();
	
	public GoPreferencePage() {
		super(LangUIPlugin.getCorePrefStore());
		
		setDescription("GoClipse v" + getVersionText());
		
		addValidationStatusField(goSDKBlock.validation);
		
		addStringComponent(GoEnvironmentPrefs.GO_ROOT, goSDKBlock.goRootField);
		addComboComponent(GoEnvironmentPrefs.GO_OS, goSDKBlock.goOSField);
		addComboComponent(GoEnvironmentPrefs.GO_ARCH, goSDKBlock.goArchField);
		
		addStringComponent(GoEnvironmentPrefs.COMPILER_PATH, goSDKBlock.goToolPath);
		addStringComponent(GoEnvironmentPrefs.FORMATTER_PATH, goSDKBlock.goFmtPath);
		addStringComponent(GoEnvironmentPrefs.DOCUMENTOR_PATH, goSDKBlock.goDocPath);
		/* FIXME: test */
		
		addPrefComponent(new EnablementButtonFieldPrefAdapter(GoEnvironmentPrefs.GO_PATH.key, goSDKBlock.goPathField));
	}
	
	protected static String getVersionText() {
		Version version = GoCore.getDefault().getBundle().getVersion();
		
		return version.getMajor() + "." + version.getMinor() + "." + version.getMicro();
	}
	
	@Override
	protected String getHelpId() {
		return null;
	}
	
	@Override
	protected Control createContents(Composite parent) {
		return goSDKBlock.createComponent(parent);
	}
	
}