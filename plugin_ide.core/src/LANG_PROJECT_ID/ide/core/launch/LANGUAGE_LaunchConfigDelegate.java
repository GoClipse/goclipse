package LANG_PROJECT_ID.ide.core.launch;


import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.Launch;

import melnorme.lang.ide.launching.LangLaunchConfigurationDelegate;

public class LANGUAGE_LaunchConfigDelegate extends LangLaunchConfigurationDelegate {
	
	@Override
	protected ILaunch getLaunchForRunMode(ILaunchConfiguration configuration, String mode) throws CoreException {
		return new Launch(configuration, mode, null);
	}
	
	@Override
	protected ILaunch getLaunchForDebugMode(ILaunchConfiguration configuration, String mode) throws CoreException {
		throw abort_UnsupportedMode(mode);
	}
	
}