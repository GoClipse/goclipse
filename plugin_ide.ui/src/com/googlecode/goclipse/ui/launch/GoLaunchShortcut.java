package com.googlecode.goclipse.ui.launch;

import melnorme.lang.ide.launching.LaunchConstants;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.FileEditorInput;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.core.launch.GoLaunchConstants;

/**
 * @author steel
 */
/* FIXME: use abstract Launch configuration*/
public class GoLaunchShortcut implements ILaunchShortcut {
	
	@Override
	public void launch(ISelection selection, final String mode) {
		Activator.logInfo("launch shortcut: selection " + selection + " mode " + mode);
		
		if (selection != null) {
			if (selection instanceof TreeSelection) {
				TreeSelection ts = (TreeSelection) selection;
				
				Object e = ts.getFirstElement();
				
				if (e instanceof IFile) {
					IFile file = (IFile) e;
					final String fName = file.getProjectRelativePath().toOSString();
					final String pName = file.getProject().getName();
					launch(pName, fName, mode);
				}
			}
		}
	}
	
	@Override
	public void launch(IEditorPart editor, final String mode) {
		Activator.logInfo("launch shortcut: editor" + editor.getTitle() + " mode " + mode);
		
		IEditorInput ei = editor.getEditorInput();
		
		if (ei != null && ei instanceof FileEditorInput) {
			FileEditorInput fei = (FileEditorInput) ei;
			IFile file = fei.getFile();
			final String fName = file.getProjectRelativePath().toOSString();
			final String pName = file.getProject().getName();
			launch(pName, fName, mode);
		}
	}
	
	private void launch(String pName, String fName, String mode) {
		try {
			ILaunchConfiguration found = null;
			ILaunchManager lm = DebugPlugin.getDefault().getLaunchManager();
			ILaunchConfigurationType lct = lm.getLaunchConfigurationType(LaunchConstants.LAUNCH_CONFIG_ID);
			ILaunchConfiguration[] lcfgs = lm.getLaunchConfigurations(lct);
			
			for (ILaunchConfiguration lcf : lcfgs) {
				String project = lcf.getAttribute(GoLaunchConstants.GO_CONF_ATTRIBUTE_PROJECT, "");
				String mainfile = lcf.getAttribute(GoLaunchConstants.GO_CONF_ATTRIBUTE_MAIN, "");
				String prgArgs = lcf.getAttribute(GoLaunchConstants.GO_CONF_ATTRIBUTE_ARGS, "");
				
				if (prgArgs.isEmpty()) {
					// this is an empty run, no params, don't mix with already
					// definded with params
					if (project.equalsIgnoreCase(pName)
							&& Path.fromOSString(fName).equals(Path.fromOSString(mainfile))) {
						found = lcf;
						break;
					}
				}
			}
			
			if (found == null) {
				//create a new launch configuration
				String cfgName = lm.generateLaunchConfigurationName(Path.fromOSString(fName).lastSegment());
				ILaunchConfigurationWorkingCopy workingCopy = lct.newInstance(null, cfgName);
				workingCopy.setAttribute(GoLaunchConstants.GO_CONF_ATTRIBUTE_PROJECT, pName);
				workingCopy.setAttribute(GoLaunchConstants.GO_CONF_ATTRIBUTE_MAIN, fName);
				workingCopy.setAttribute(GoLaunchConstants.GO_CONF_ATTRIBUTE_ARGS, "");
				workingCopy.setAttribute(DebugPlugin.ATTR_CAPTURE_OUTPUT, true);
				workingCopy.setAttribute(DebugPlugin.ATTR_CONSOLE_ENCODING, "UTF-8");
				
				found = workingCopy.doSave();
			}
			
			if (found != null) {
				found.launch(mode, null, true, true);
			}
		} catch (CoreException ce) {
			Activator.logError(ce);
		}
	}
	
}
