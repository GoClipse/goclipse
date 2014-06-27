package com.googlecode.goclipse.editors;

import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.builder.GoToolManager;
import com.googlecode.goclipse.core.GoCore;
import com.googlecode.goclipse.preferences.PreferenceConstants;

/**
 * 
 * @author steel
 *
 */
public class GofmtActionDelegate extends TransformTextAction {
	
    public GofmtActionDelegate() {
    	super("gofmt");
	}

	@Override
	protected String transformText(final String text) throws CoreException {
		final String currentContent = text;
		
		String gofmtPath = GoCore.getPreferences().getString(PreferenceConstants.FORMATTER_PATH);
		
		IProject project = null; // TODO
		IProgressMonitor pm = new NullProgressMonitor(); // TODO
		
		ExternalProcessResult processResult = GoToolManager.getDefault().runGoTool(gofmtPath, project, pm, currentContent);
		
		if (processResult.exitValue != 0) {
			throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, gofmtPath
					+ " completed with non-zero exit value (" + processResult.exitValue + ")"));
		}

		String formattedText = processResult.getStdOutBytes().toString();

		if (!formattedText.equals(currentContent)) {
			return formattedText;
		} else {
			return null;
		}
	}

}