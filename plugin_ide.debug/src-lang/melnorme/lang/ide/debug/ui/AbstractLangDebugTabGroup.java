/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.debug.ui;


import melnorme.lang.ide.ui.LangMainLaunchConfigurationTab_Actual;
import melnorme.lang.ide.ui.LangProgramArgumentsTab_Actual;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.EnvironmentTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.debug.ui.sourcelookup.SourceLookupTab;

public abstract class AbstractLangDebugTabGroup extends AbstractLaunchConfigurationTabGroup {
	
	public AbstractLangDebugTabGroup() {
		super();
	}
	
	@Override
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] {
				new LangMainLaunchConfigurationTab_Actual(),
				new LangProgramArgumentsTab_Actual(),
				new EnvironmentTab(),
				new org.eclipse.cdt.dsf.gdb.internal.ui.launching.LocalApplicationCDebuggerTab(),
				new SourceLookupTab(),
				new CommonTab(),
		};
		
		setTabs(tabs);
	}
	
}