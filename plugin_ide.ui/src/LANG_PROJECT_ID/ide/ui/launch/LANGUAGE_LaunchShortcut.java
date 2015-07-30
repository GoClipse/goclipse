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
package LANG_PROJECT_ID.ide.ui.launch;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.launch.LangLaunchShortcut;

public class LANGUAGE_LaunchShortcut extends LangLaunchShortcut {
	
	@Override
	protected String getLaunchTypeId() {
		return LangCore.PLUGIN_ID + ".LaunchConfigurationType";
	}
	
}