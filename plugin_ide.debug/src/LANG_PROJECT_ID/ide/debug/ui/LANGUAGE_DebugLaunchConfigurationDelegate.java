package LANG_PROJECT_ID.ide.debug.ui;


import melnorme.lang.ide.debug.core.AbstractLangDebugLaunchConfigurationDelegate;

import org.eclipse.cdt.dsf.gdb.launching.GdbLaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ISourceLocator;

public class LANGUAGE_DebugLaunchConfigurationDelegate extends AbstractLangDebugLaunchConfigurationDelegate {
	
	@Override
	protected GdbLaunch doCreateGdbLaunch(ILaunchConfiguration configuration, String mode, ISourceLocator locator) {
		return new LANGUAGE_GdbLaunch(configuration, mode, locator);
	}
	
}