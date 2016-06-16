package com.googlecode.goclipse.ui.editor;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;

import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.core.tools.GocodeServerManager;
import com.googlecode.goclipse.tooling.env.GoEnvironment;
import com.googlecode.goclipse.tooling.gocode.GocodeCompletionOperation;
import com.googlecode.goclipse.tooling.gocode.GocodeOutputParser2;
import com.googlecode.goclipse.ui.GoUIPlugin;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.ide.ui.text.completion.LangCompletionProposalComputer;
import melnorme.lang.tooling.completion.LangCompletionResult;
import melnorme.lang.tooling.toolchain.ops.SourceOpContext;
import melnorme.utilbox.concurrency.ICancelMonitor;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public class GocodeCompletionProposalComputer extends LangCompletionProposalComputer {
	
	@Override
	protected LangCompletionResult doComputeProposals(SourceOpContext sourceContext, ICancelMonitor cm)
			throws CommonException, OperationCancellation {
		Location fileLoc = sourceContext.getFileLocation();
		int offset = sourceContext.getOffset();
		
		GoUIPlugin.prepareGocodeManager_inUI();
		IPath gocodePath = GocodeServerManager.getGocodePath();
		if (gocodePath == null) {
			throw new CommonException("Error: gocode path not provided.");
		}
		IProject project = ResourceUtils.getProject(sourceContext.getOptionalFileLocation());
		
		GoEnvironment goEnvironment = GoProjectEnvironment.getGoEnvironment(project);
		
		// TODO: we should run this operation outside the UI thread.
		GocodeCompletionOperation client = new GocodeCompletionOperation(
			getEngineToolRunner(), goEnvironment, gocodePath.toOSString(), cm);
		
		String source = sourceContext.getSource();
		ExternalProcessResult processResult = client.execute(fileLoc.toPathString(), source, offset);
		
		GocodeOutputParser2 gocodeOutputParser = new GocodeOutputParser2(offset, source){
			@Override
			protected void logWarning(String message) {
				LangCore.logWarning(message);
			}
		};
		return gocodeOutputParser.parseProcessResult(processResult);
	}
	
}