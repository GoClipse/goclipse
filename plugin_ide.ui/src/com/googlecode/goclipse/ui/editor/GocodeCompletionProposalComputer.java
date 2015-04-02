package com.googlecode.goclipse.ui.editor;

import java.util.ArrayList;
import java.util.List;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.editor.EditorUtils;
import melnorme.lang.ide.ui.text.completion.LangCompletionProposalComputer;
import melnorme.lang.ide.ui.text.completion.LangContentAssistInvocationContext;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorPart;

import com.googlecode.goclipse.core.GoCore;
import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.tooling.env.GoEnvironment;
import com.googlecode.goclipse.ui.GoPluginImages;
import com.googlecode.goclipse.ui.GoUIPlugin;
import com.googlecode.goclipse.ui.actions.GocodeClient;

public class GocodeCompletionProposalComputer extends LangCompletionProposalComputer {
	
	@Override
	protected List<ICompletionProposal> doComputeCompletionProposals(LangContentAssistInvocationContext context,
			int offset) throws CoreException {
		
		IEditorPart editor = context.getEditor_nonNull();
		Location fileLoc = context.getEditorInputLocation();
		IDocument document = context.getViewer().getDocument();
		
		String prefix = lastWord(document, offset);
		
		IPath gocodePath = GoUIPlugin.prepareGocodeManager_inUI().getGocodePath();
		if (gocodePath == null) {
			throw LangCore.createCoreException("Error: gocode path not provided.", null);
		}
		IProject project = getProjectFor(editor);
		
		ArrayList<ICompletionProposal> results = new ArrayList<ICompletionProposal>();
		
		try {
			GoEnvironment goEnvironment = GoProjectEnvironment.getGoEnvironment(project);
			
			// TODO: we should run this operation outside the UI thread.
			IProgressMonitor pm = new TimeoutProgressMonitor(5000, true);
			GocodeClient client = new GocodeClient(gocodePath.toOSString(), goEnvironment, pm);
			
			ExternalProcessResult processResult = client.execute(fileLoc.toPathString(), document.get(), offset);
			String stdout = processResult.getStdOutBytes().toString(StringUtil.UTF8);
			List<String> completions = new ArrayList2<>(GocodeClient.LINE_SPLITTER.split(stdout));
			
			for (String completionEntry : completions) {
				handleResult(offset, /*codeContext,*/ results, prefix, completionEntry);
			}
			
			return results;
		} catch (CommonException e) {
			throw LangCore.createCoreException(e.getMessage(), e.getCause());
		} catch (OperationCancellation e) {
			throw LangCore.createCoreException("Timeout invoking content assist.", null);
		}
		
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