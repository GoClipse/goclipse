package MELNORME_ID.ide.ui.launch;

import melnorme.lang.ide.ui.launch.MainLaunchConfigurationTab;

import org.eclipse.core.resources.IResource;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;


public class LANGUAGE_MainLaunchConfigurationTab extends MainLaunchConfigurationTab {
	
	public LANGUAGE_MainLaunchConfigurationTab() {
		super();
	}
	
	@Override
	protected void programPathField_setDefaults(IResource contextualResource, ILaunchConfigurationWorkingCopy config) {
		// XXX
	}
	
}