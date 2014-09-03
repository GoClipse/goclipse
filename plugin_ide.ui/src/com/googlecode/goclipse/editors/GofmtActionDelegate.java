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
		
		String gofmtPath = PreferenceConstants.FORMATTER_PATH.get();
		
		IProject project = null; // TODO
		IProgressMonitor pm = new NullProgressMonitor(); // TODO
		
		ExternalProcessResult processResult = GoToolManager.getDefault().runGoTool(
			gofmtPath, project, pm, currentContent);
		
		if (processResult.exitValue != 0) {
			throw GoCore.createCoreException(
				gofmtPath + " completed with non-zero exit value (" + processResult.exitValue + ")", null);
		}
		
		String formattedText = processResult.getStdOutBytes().toString();
		
		if (!formattedText.equals(currentContent)) {
			return formattedText;
		} else {
			return null;
		}
	}
	
}