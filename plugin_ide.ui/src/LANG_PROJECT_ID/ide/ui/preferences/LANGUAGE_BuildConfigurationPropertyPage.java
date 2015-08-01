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
package LANG_PROJECT_ID.ide.ui.preferences;

import org.eclipse.core.resources.IProject;

import melnorme.lang.ide.ui.dialogs.LangBuildConfigurationPropertyPage;
import melnorme.lang.ide.ui.preferences.LangProjectBuildConfigurationComponent;


public class LANGUAGE_BuildConfigurationPropertyPage extends LangBuildConfigurationPropertyPage {
	
	@Override
	protected LangProjectBuildConfigurationComponent createProjectBuildConfigComponent(IProject project) {
		return new LangProjectBuildConfigurationComponent(project) {
		};
	}
	
}