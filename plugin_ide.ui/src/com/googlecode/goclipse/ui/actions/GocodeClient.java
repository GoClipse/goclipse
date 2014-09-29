package com.googlecode.goclipse.ui.actions;

import java.util.List;

import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import com.googlecode.goclipse.builder.GoToolManager;
import com.googlecode.goclipse.tooling.gocode.GocodeCompletionOperation;

public class GocodeClient extends GocodeCompletionOperation<CoreException> {
	
	// FIXME: we should run this outside the UI thread, with an actual monitor to allow cancelling!
	IProgressMonitor pm = new NullProgressMonitor();
	
	public GocodeClient(String gocodePath) {
		super(gocodePath);
	}
	
	@Override
	protected ExternalProcessResult runGocode(List<String> arguments, String input) throws CoreException {
		return GoToolManager.getDefault().runEngineClientTool(gocodePath, arguments, input, pm);
	}
	
}