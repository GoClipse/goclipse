package com.googlecode.goclipse.ui.editor;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.text.IDocument;

import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.core.tools.GocodeServerManager;
import com.googlecode.goclipse.tooling.env.GoEnvironment;
import com.googlecode.goclipse.tooling.gocode.GocodeCompletionOperation;
import com.googlecode.goclipse.tooling.gocode.GocodeOutputParser;
import com.googlecode.goclipse.ui.GoUIPlugin;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.utils.operation.TimeoutProgressMonitor;
import melnorme.lang.ide.ui.editor.actions.SourceOperationContext;
import melnorme.lang.ide.ui.text.completion.LangCompletionProposalComputer;
import melnorme.lang.tooling.completion.LangCompletionResult;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public class GocodeCompletionProposalComputer extends LangCompletionProposalComputer {
	
	@Override
	protected LangCompletionResult doComputeProposals(SourceOperationContext context, int offset,
			TimeoutProgressMonitor pm) throws CoreException, CommonException, OperationCancellation {
		Location fileLoc = context.getEditorInputLocation();
		IDocument document = context.getDocument();
		
		GoUIPlugin.prepareGocodeManager_inUI();
		IPath gocodePath = GocodeServerManager.getGocodePath();
		if (gocodePath == null) {
			throw LangCore.createCoreException("Error: gocode path not provided.", null);
		}
		IProject project = context.getProject();
		
		GoEnvironment goEnvironment = GoProjectEnvironment.getGoEnvironment(project);
		
		// TODO: we should run this operation outside the UI thread.
		GocodeCompletionOperation client = new GocodeCompletionOperation(
			getEngineToolRunner(pm), goEnvironment, gocodePath.toOSString());
		
		String source = document.get();
		ExternalProcessResult processResult = client.execute(fileLoc.toPathString(), source, offset);
		
		return new LangCompletionResult(new GocodeOutputParser(offset, source).parse(processResult));
	}
	
}