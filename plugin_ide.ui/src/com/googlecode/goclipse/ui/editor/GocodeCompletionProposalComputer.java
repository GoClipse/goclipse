package com.googlecode.goclipse.ui.editor;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.ui.IEditorPart;

import com.googlecode.goclipse.core.GoCore;
import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.core.tools.GocodeServerManager;
import com.googlecode.goclipse.tooling.env.GoEnvironment;
import com.googlecode.goclipse.tooling.gocode.GocodeCompletionOperation;
import com.googlecode.goclipse.ui.GoUIPlugin;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.utils.operation.TimeoutProgressMonitor;
import melnorme.lang.ide.ui.editor.EditorUtils;
import melnorme.lang.ide.ui.editor.actions.SourceOperationContext;
import melnorme.lang.ide.ui.text.completion.LangCompletionProposalComputer;
import melnorme.lang.tooling.CompletionProposalKind;
import melnorme.lang.tooling.EProtection;
import melnorme.lang.tooling.ElementAttributes;
import melnorme.lang.tooling.ToolCompletionProposal;
import melnorme.lang.tooling.completion.LangCompletionResult;
import melnorme.lang.tooling.ops.OperationSoftFailure;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public class GocodeCompletionProposalComputer extends LangCompletionProposalComputer {
	
	@Override
	protected Indexable<ICompletionProposal> computeProposals(SourceOperationContext context, int offset,
			TimeoutProgressMonitor pm) 
			throws CoreException, CommonException, OperationCancellation, OperationSoftFailure {
		
		IEditorPart editor = context.getEditor_nonNull();
		Location fileLoc = context.getEditorInputLocation();
		IDocument document = context.getDocument();
		
		String prefix = lastWord(document, offset);
		
		GoUIPlugin.prepareGocodeManager_inUI();
		IPath gocodePath = GocodeServerManager.getGocodePath();
		if (gocodePath == null) {
			throw LangCore.createCoreException("Error: gocode path not provided.", null);
		}
		IProject project = getProjectFor(editor);
		
		GoEnvironment goEnvironment = GoProjectEnvironment.getGoEnvironment(project);
		
		// TODO: we should run this operation outside the UI thread.
		GocodeCompletionOperation client = new GocodeCompletionOperation(
			getEngineToolRunner(pm), goEnvironment, gocodePath.toOSString());
		
		ExternalProcessResult processResult = client.execute(fileLoc.toPathString(), document.get(), offset);
		String stdout = processResult.getStdOutBytes().toString(StringUtil.UTF8);
		List<String> completions = new ArrayList2<>(GocodeCompletionOperation.LINE_SPLITTER.split(stdout));
		
		ArrayList2<ICompletionProposal> results = new ArrayList2<>();
		
		for (String completionEntry : completions) {
			handleResult(offset, /*codeContext,*/ results, prefix, completionEntry);
		}
		
		return results;
	}
	
	@Override
	protected LangCompletionResult doComputeProposals(SourceOperationContext context, int offset,
			TimeoutProgressMonitor pm) throws CoreException, CommonException, OperationCancellation {
		throw assertFail();
	}
	
	public static IProject getProjectFor(IEditorPart editor) {
		return EditorUtils.getAssociatedProject(editor.getEditorInput());
	}
	
	public static String lastWord(IDocument doc, int offset) {
		try {
			for (int n = offset - 1; n >= 0; n--) {
				char c = doc.getChar(n);
				
				if (!Character.isJavaIdentifierPart(c)) {
					return doc.get(n + 1, offset - n - 1);
				}
			}
		} catch (BadLocationException e) {
			GoCore.logInternalError(e);
		}
		
		return "";
	}
	
	protected void handleResult(final int offset, /*CodeContext codeContext, */ArrayList<ICompletionProposal> results,
			String prefix, String completionEntry) {
		int firstComma = completionEntry.indexOf(",,");
		int secondComma = completionEntry.indexOf(",,", firstComma + 2);
		
		if (firstComma != -1 && secondComma != -1) {
			String type = completionEntry.substring(0, firstComma);
			
			if ("PANIC".equals(type)) {
				GoUIPlugin.logError("PANIC from gocode - likely go/gocode version mismatch?");
				return;
			}
			
			String identifier = completionEntry.substring(firstComma + 2, secondComma);
			
			if ("PANIC".equals(identifier)) {
				GoUIPlugin.logError("PANIC from gocode - likely go/gocode version mismatch?");
				return;
			}
			
			String spec = completionEntry.substring(secondComma + 2);
			
			ToolCompletionProposal proposal = getProposal(offset, prefix, type, identifier, spec);
			results.add(adaptToolProposal(proposal));
		}
	}
	
	protected ToolCompletionProposal getProposal(final int offset, String prefix, String type, String identifier,
			String spec) {
		String descriptiveString = identifier + " : " + spec;
		// BM: removed used of CodeContext because it is buggy, 
		// see https://github.com/GoClipse/goclipse/issues/112
//			String description = codeContext.getDescriptionForName(identifier).trim();
//			IContextInformation info = new ContextInformation(description, description);
		
		CompletionProposalKind kind = CompletionProposalKind.UNKNOWN;
		EProtection prot = null;
		
		String substr = identifier.substring(prefix.length());
		@SuppressWarnings("unused")
		int replacementLength = identifier.length() - prefix.length();
		
		if (descriptiveString != null && descriptiveString.contains(" : func")) {
			if(identifier.isEmpty() || Character.isLowerCase(identifier.charAt(0))) {
				prot = EProtection.PRIVATE;
			}
			kind = CompletionProposalKind.FUNCTION;
			
			substr = identifier.substring(prefix.length()) + "()";
			replacementLength++;
			
		} else if (descriptiveString != null && descriptiveString.contains(" : interface")) {
			kind = CompletionProposalKind.INTERFACE;
			
		} else if (descriptiveString != null && descriptiveString.contains(" : struct")) {
			kind = CompletionProposalKind.STRUCT;
			
		} else if ("package".equals(type)) {
			kind = CompletionProposalKind.IMPORT;
			
			substr = identifier.substring(prefix.length()) + ".";
			replacementLength++;
		} else {
			if (substr != null && substr.length() > 0 && Character.isLowerCase(substr.charAt(0))) {
				prot = EProtection.PRIVATE;
			}
			kind = CompletionProposalKind.VARIABLE;
		}
		
		// format the output
		descriptiveString = descriptiveString.replace(" : func", " ").replace(" : interface",
				" ").replace(" : struct", " ").replace("(", "( ").replace(")", " )");
		
		ElementAttributes attributes = new ElementAttributes(prot);
		
		return new ToolCompletionProposal(
			offset - prefix.length(), prefix.length(), identifier, descriptiveString, 
			kind, attributes, null, null);
	}
	
}