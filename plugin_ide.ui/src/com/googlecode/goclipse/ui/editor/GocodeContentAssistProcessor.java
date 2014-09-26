package com.googlecode.goclipse.ui.editor;

import static melnorme.utilbox.core.CoreUtil.array;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.editor.EditorUtils;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ContextInformation;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorPart;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.core.GoCore;
import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.editors.GoEditor;
import com.googlecode.goclipse.go.CodeContext;
import com.googlecode.goclipse.tooling.GoEnvironment;
import com.googlecode.goclipse.tooling.StatusException;
import com.googlecode.goclipse.ui.GoPluginImages;
import com.googlecode.goclipse.ui.GoUIPlugin;
import com.googlecode.goclipse.ui.actions.GocodeClient;

/**
 * Implement an IContentAssistProcessor (and IContentAssistProcessorExt), and delegate the work
 * through to the GoCodeClient class. This IContentAssistProcessor is declared in the plugin.xml,
 * and is called from the GoEditorSourceViewerConfiguration class in the main GoClipse plugin.
 */
public class GocodeContentAssistProcessor implements IContentAssistProcessor {
	
	private static HashMap<String, CodeContext> codeContexts = new HashMap<String, CodeContext>();
	
	private final IEditorPart editor;
	
	protected String errorMessage;
	
	public GocodeContentAssistProcessor(GoEditor editor) {
		this.editor = editor;
	}
	
	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
		errorMessage = null;
		try {
			return computeCompletionProposals_do(viewer, offset);
		} catch (CoreException e) {
			GoCore.logError(e);
			errorMessage = e.getMessage();
			return array();
		}
	}
	
	protected IPath getBestGocodePath() {
		IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(
			Activator.CONTENT_ASSIST_EXTENSION_ID);
		
		try {
			for (IConfigurationElement e : config) {
				final Object extension = e.createExecutableExtension("class");
				
				if (extension instanceof IGocodePathProvider) {
					return ((IGocodePathProvider) extension).getBestGocodePath();
				}
			}
		} catch (CoreException ex) {
			// do nothing
		}
		return null;
	}
	
	protected ICompletionProposal[] computeCompletionProposals_do(ITextViewer viewer, final int offset)
			throws CoreException {
		
		if(editor == null) {
			throw LangCore.createCoreException("Error, no editor provided:", null);
		}
		
		String filePath = EditorUtils.getFilePathFromEditorInput(editor.getEditorInput()).toString();
		
		if (filePath == null) {
			throw LangCore.createCoreException("Error: Could not determine file path for editor.", null);
		}
		
		final IDocument document = viewer.getDocument();
		CodeContext codeContext = codeContexts.get(filePath);
		
		if (codeContext == null) {
			try {
				codeContext = CodeContext.getCodeContext(getProjectFor(editor), filePath, document.get());
			} catch (IOException e) {
				throw LangCore.createCoreException("Error during code Context:", e);
			}
		}
		
		IPath gocodePath = getBestGocodePath();
		
		if (gocodePath == null) {
			throw LangCore.createCoreException("Error: gocode path not provided.", null);
		}
		String gocodePathStr = gocodePath.toOSString();
		
		List<String> completions;
		
		ArrayList<ICompletionProposal> results = new ArrayList<ICompletionProposal>();
		
		try {
			GoEnvironment goEnvironment = GoProjectEnvironment.getGoEnvironment(getProjectFor(editor));
			
			GocodeClient client = new GocodeClient(gocodePathStr);
			
			ExternalProcessResult processResult = client.execute(goEnvironment, filePath, document.get(), offset);
			String stdout = processResult.getStdOutBytes().toString(StringUtil.UTF8);
			completions = new ArrayList2<>(GocodeClient.LINE_SPLITTER.split(stdout));
			
		} catch (StatusException e) {
			throw LangCore.createCoreException(e.getMessage(), e.getCause());
		}
		
		String prefix = lastWord(document, offset);
		
		for (String completionEntry : completions) {
			handleResult(offset, codeContext, results, prefix, completionEntry);
		}
		
		return results.toArray(new ICompletionProposal[] {});
	}
	
	protected void handleResult(final int offset, CodeContext codeContext, ArrayList<ICompletionProposal> results,
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
			String description = codeContext.getDescriptionForName(identifier).trim();
			IContextInformation info = new ContextInformation(description, description);
			//MessageFormat.format(JavaEditorMessages.getString("CompletionProcessor.Proposal.ContextInfo.pattern"), new Object[] { fgProposals[i] })); //$NON-NLS-1$
			
			Image image = GoPluginImages.SOURCE_OTHER.getImage();
			String substr = identifier.substring(prefix.length());
			int replacementLength = identifier.length() - prefix.length();
			
			if (descriptiveString != null && descriptiveString.contains(" : func")) {
				if (codeContext.isMethodName(identifier)) {
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
				prefix.length(), identifier.length(), image, descriptiveString, info, description));
		}
	}
	
	/**
	 * @param editor
	 * @return
	 */
	private IProject getProjectFor(IEditorPart editor) {
		if (editor == null) {
			return null;
		}
		
		IResource resource = (IResource) editor.getEditorInput().getAdapter(IResource.class);
		
		return resource != null ? resource.getProject() : null;
	}
	
	/**
	 * @param doc
	 * @param offset
	 * @return
	 */
	private static String lastWord(IDocument doc, int offset) {
		try {
			for (int n = offset - 1; n >= 0; n--) {
				char c = doc.getChar(n);
				
				if (!Character.isJavaIdentifierPart(c)) {
					return doc.get(n + 1, offset - n - 1);
				}
			}
		} catch (BadLocationException e) {
			GoCore.logError(e);
		}
		
		return "";
	}
	
	@Override
	public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
		return null;
	}
	
	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return new char[] {'.'};
	}
	
	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		return null;
	}
	
	@Override
	public String getErrorMessage() {
		return errorMessage;
	}
	
	@Override
	public IContextInformationValidator getContextInformationValidator() {
		return null;
	}
	
}
