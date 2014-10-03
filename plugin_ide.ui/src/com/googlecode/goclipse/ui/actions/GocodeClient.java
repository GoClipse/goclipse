package com.googlecode.goclipse.ui.actions;

import java.util.List;

import melnorme.lang.ide.core.operations.RunEngineClientOperation;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import com.googlecode.goclipse.builder.GoToolManager;
import com.googlecode.goclipse.tooling.env.GoEnvironment;
import com.googlecode.goclipse.tooling.gocode.GocodeCompletionOperation;

public class GocodeClient extends GocodeCompletionOperation<CoreException> {
	
	protected final IProgressMonitor pm;
	
	public GocodeClient(String gocodePath, GoEnvironment goEnvironment, IProgressMonitor pm) {
		super(goEnvironment, gocodePath);
		this.pm = pm;
	}
	
	@Override
	protected ExternalProcessResult runGocode(List<String> arguments, String input) throws CoreException {
		ProcessBuilder pb = goEnvironment.createProcessBuilder(new ArrayList2<>(gocodePath).addElements(arguments));
		return new RunEngineClientOperation(GoToolManager.getDefault(), pb).runProcess(input, pm);
	}
	
}