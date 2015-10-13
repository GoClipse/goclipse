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

import static melnorme.lang.tooling.data.PathValidator.LocationKind.FILE_ONLY;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.osgi.framework.Version;

import com.googlecode.goclipse.core.GoCore;
import com.googlecode.goclipse.core.GoEnvironmentPrefs;

import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.preferences.common.AbstractComponentsPrefPage;
import melnorme.lang.ide.ui.preferences.common.IPreferencesDialogComponent.EnablementButtonFieldPrefAdapter;
import melnorme.lang.tooling.data.LocationValidator;

public class GoPreferencePage extends AbstractComponentsPrefPage {
	
	protected final GoSDKConfigBlock goSDKBlock = new GoSDKConfigBlock();
	
	public GoPreferencePage() {
		super(LangUIPlugin.getCorePrefStore());
		
		setDescription("GoClipse v" + getVersionText());
		
		addValidationSource(goSDKBlock.validation);
		goSDKBlock.validation.addListener(() -> updateStatusMessage());
		
		addStringComponent(GoEnvironmentPrefs.GO_ROOT, goSDKBlock.goRootField);
		addComboComponent(GoEnvironmentPrefs.GO_OS, goSDKBlock.goOSField);
		addComboComponent(GoEnvironmentPrefs.GO_ARCH, goSDKBlock.goArchField);
		
		connectStringField(GoEnvironmentPrefs.COMPILER_PATH, goSDKBlock.goToolPath, 
			new LocationValidator(goSDKBlock.goToolPath.getLabelText(), FILE_ONLY));
		connectStringField(GoEnvironmentPrefs.FORMATTER_PATH, goSDKBlock.goFmtPath, 
			new LocationValidator(goSDKBlock.goFmtPath.getLabelText(), FILE_ONLY));
		connectStringField(GoEnvironmentPrefs.DOCUMENTOR_PATH, goSDKBlock.goDocPath, 
			new LocationValidator(goSDKBlock.goDocPath.getLabelText(), FILE_ONLY));
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