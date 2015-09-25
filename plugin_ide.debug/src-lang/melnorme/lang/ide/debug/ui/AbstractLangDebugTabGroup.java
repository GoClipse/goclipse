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
package melnorme.lang.ide.debug.ui;

import org.eclipse.cdt.debug.core.ICDTLaunchConfigurationConstants;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.EnvironmentTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.debug.ui.sourcelookup.SourceLookupTab;

import melnorme.lang.ide.ui.launch.AbstractLangTabGroup;
import melnorme.lang.ide.ui.launch.LangArgumentsTab;

public abstract class AbstractLangDebugTabGroup extends AbstractLangTabGroup {
	
	@Override
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		setTabs(new ILaunchConfigurationTab[] {
				createMainLaunchConfigTab(),
				new LangArgumentsTab(),
				new EnvironmentTab(),
				new Lang_LocalApplicationCDebuggerTab(),
				new SourceLookupTab(),
				new CommonTab(),
		});
	}
	
	protected class Lang_LocalApplicationCDebuggerTab
			extends org.eclipse.cdt.dsf.gdb.internal.ui.launching.LocalApplicationCDebuggerTab {
		
		@Override
		public void setDefaults(ILaunchConfigurationWorkingCopy config) {
			super.setDefaults(config);
			
			config.setAttribute(ICDTLaunchConfigurationConstants.ATTR_DEBUGGER_STOP_AT_MAIN, false);
		}
	}
	
}