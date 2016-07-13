package com.googlecode.goclipse.ui.editor;

import java.nio.file.Path;

import org.eclipse.core.resources.IProject;

import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.core.tools.GocodeServerInstance;
import com.googlecode.goclipse.core.tools.GocodeServerManager;
import com.googlecode.goclipse.tooling.env.GoEnvironment;
import com.googlecode.goclipse.tooling.gocode.GocodeCompletionOperation;
import com.googlecode.goclipse.tooling.gocode.GocodeOutputParser2;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.ide.ui.text.completion.LangCompletionProposalComputer;
import melnorme.lang.tooling.ToolCompletionProposal;
import melnorme.lang.tooling.toolchain.ops.OperationSoftFailure;
import melnorme.lang.tooling.toolchain.ops.SourceOpContext;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.concurrency.ICancelMonitor;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public class GocodeCompletionProposalComputer extends LangCompletionProposalComputer {
	
	protected final GocodeServerManager gocodeServerManager = LangCore.get().languageServerHandler();
	
	@Override
	protected Indexable<ToolCompletionProposal> doComputeProposals(SourceOpContext sourceContext, ICancelMonitor cm)
			throws CommonException, OperationCancellation, OperationSoftFailure {
		Location fileLoc = sourceContext.getFileLocation();
		int offset = sourceContext.getOffset();
		
		GocodeServerInstance gocodeServerInstance = gocodeServerManager.getReadyServerInstance();
		Path gocodePath = gocodeServerInstance.getServerPath();
		IProject project = ResourceUtils.getProjectFromMemberLocation(sourceContext.getOptionalFileLocation());
		
		GoEnvironment goEnvironment = GoProjectEnvironment.getGoEnvironment(project);
		
		// TODO: we should run this operation outside the UI thread.
		GocodeCompletionOperation client = new GocodeCompletionOperation(
			getEngineToolRunner(), goEnvironment, gocodePath.toString(), cm);
		
		String source = sourceContext.getSource();
		ExternalProcessResult processResult = client.execute(fileLoc.toPathString(), source, offset);
		
		GocodeOutputParser2 gocodeOutputParser = new GocodeOutputParser2(offset, source){
			@Override
			protected void logWarning(String message) {
				LangCore.logWarning(message);
			}
		};
		return gocodeOutputParser.doParseResult(processResult);
	}
	
}