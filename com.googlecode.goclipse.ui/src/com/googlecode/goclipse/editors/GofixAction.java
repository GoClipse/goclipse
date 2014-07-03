package com.googlecode.goclipse.editors;

import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import com.googlecode.goclipse.builder.GoToolManager;
import com.googlecode.goclipse.core.GoCore;
import com.googlecode.goclipse.preferences.PreferenceConstants;

/**
 * An action to run the gofix command.
 */
public class GofixAction extends TransformTextAction {
	
	public GofixAction() {
		super("Gofix");
	}

	@Override
	protected String transformText(final String text) throws CoreException {
		String goarch = GoCore.getPreferences().getString(PreferenceConstants.GOARCH);
		String goos = GoCore.getPreferences().getString(PreferenceConstants.GOOS);
		String goRoot = GoCore.getPreferences().getString(PreferenceConstants.GOROOT);
		String gofixPath = goRoot + "/pkg/tool/"+goos+"_"+goarch+"/fix";
		
		
		IProject project = null; // TODO
		IProgressMonitor pm = new NullProgressMonitor(); // TODO
		
		ExternalProcessResult processResult = GoToolManager.getDefault().runGoTool(gofixPath, project, pm, text);
		if (processResult.exitValue != 0) {
			throw GoCore.createCoreException(
				gofixPath + " completed with non-zero exit value (" + processResult.exitValue + ")", null);
		}
		
		String transformedText = processResult.getStdOutBytes().toString();
		
		if (!transformedText.equals(text)) {
			return transformedText;
		} else {
			return null;
		}
	}

}