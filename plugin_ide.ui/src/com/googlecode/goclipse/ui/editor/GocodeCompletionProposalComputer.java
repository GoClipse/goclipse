package com.googlecode.goclipse.ui.editor;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;

import java.util.ArrayList;
import java.util.List;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.TimeoutProgressMonitor;
import melnorme.lang.ide.ui.editor.EditorUtils;
import melnorme.lang.ide.ui.text.completion.LangCompletionProposalComputer;
import melnorme.lang.ide.ui.text.completion.LangContentAssistInvocationContext;
import melnorme.lang.tooling.completion.LangCompletionResult;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorPart;

import com.googlecode.goclipse.core.GoCore;
import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.core.tools.GocodeServerManager;
import com.googlecode.goclipse.tooling.env.GoEnvironment;
import com.googlecode.goclipse.tooling.gocode.GocodeCompletionOperation;
import com.googlecode.goclipse.ui.GoPluginImages;
import com.googlecode.goclipse.ui.GoUIPlugin;

public class GocodeCompletionProposalComputer extends LangCompletionProposalComputer {
	
	@Override
	protected List<ICompletionProposal> computeProposals(LangContentAssistInvocationContext context, int offset,
			TimeoutProgressMonitor pm) throws CoreException, CommonException, OperationCancellation {
		
		IEditorPart editor = context.getEditor_nonNull();
		Location fileLoc = context.getEditorInputLocation();
		IDocument document = context.getViewer().getDocument();
		
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
			getProcessRunner(pm), goEnvironment, gocodePath.toOSString());
		
		ExternalProcessResult processResult = client.execute(fileLoc.toPathString(), document.get(), offset);
		String stdout = processResult.getStdOutBytes().toString(StringUtil.UTF8);
		List<String> completions = new ArrayList2<>(GocodeCompletionOperation.LINE_SPLITTER.split(stdout));
		
		ArrayList<ICompletionProposal> results = new ArrayList<ICompletionProposal>();
		
		for (String completionEntry : completions) {
			handleResult(offset, /*codeContext,*/ results, prefix, completionEntry);
		}
		
		return results;
	}
	
	@Override
	protected LangCompletionResult doComputeProposals(LangContentAssistInvocationContext context, int offset,
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
			
			String descriptiveString = identifier + " : " + spec;
			// BM: removed used of CodeContext because it is buggy, 
			// see https://github.com/GoClipse/goclipse/issues/112
//			String description = codeContext.getDescriptionForName(identifier).trim();
//			IContextInformation info = new ContextInformation(description, description);
			
			Image image = GoPluginImages.SOURCE_OTHER.getImage();
			String substr = identifier.substring(prefix.length());
			@SuppressWarnings("unused")
			int replacementLength = identifier.length() - prefix.length();
			
			if (descriptiveString != null && descriptiveString.contains(" : func")) {
				if(identifier.isEmpty() || Character.isLowerCase(identifier.charAt(0))) {
					image = GoPluginImages.SOURCE_PRIVATE_FUNCTION.getImage();
				} else {
					image = GoPluginImages.SOURCE_FUNCTION.getImage();
				}
				
				substr = identifier.substring(prefix.length()) + "()";
				replacementLength++;
				
			} else if (descriptiveString != null && descriptiveString.contains(" : interface")) {
				image = GoPluginImages.SOURCE_INTERFACE.getImage();
				
			} else if (descriptiveString != null && descriptiveString.contains(" : struct")) {
				image = GoPluginImages.SOURCE_STRUCT.getImage();
				
			} else if ("package".equals(type)) {
				image = GoPluginImages.SOURCE_IMPORT.getImage();
				
				substr = identifier.substring(prefix.length()) + ".";
				replacementLength++;
			} else {
				if (substr != null && substr.length() > 0 && Character.isUpperCase(substr.charAt(0))) {
					image = GoPluginImages.SOURCE_PUBLIC_VAR.getImage();
					
				} else {
					image = GoPluginImages.SOURCE_PRIVATE_VAR.getImage();
				}
			}
			
			// format the output
			descriptiveString = descriptiveString.replace(" : func", " ").replace(" : interface",
					" ").replace(" : struct", " ").replace("(", "( ").replace(")", " )");
			
			results.add(new CompletionProposal(identifier, offset - prefix.length(),
				prefix.length(), identifier.length(), image, descriptiveString, null, null));
		}
	}

}