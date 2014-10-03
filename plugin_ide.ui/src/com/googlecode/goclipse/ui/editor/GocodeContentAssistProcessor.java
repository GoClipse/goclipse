package com.googlecode.goclipse.ui.editor;

import melnorme.lang.ide.ui.editor.EditorUtils;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.ui.IEditorPart;

import com.googlecode.goclipse.core.GoCore;
import com.googlecode.goclipse.editors.GoEditor;

/**
 * Implement an IContentAssistProcessor (and IContentAssistProcessorExt), and delegate the work
 * through to the GoCodeClient class. This IContentAssistProcessor is declared in the plugin.xml,
 * and is called from the GoEditorSourceViewerConfiguration class in the main GoClipse plugin.
 */
public class GocodeContentAssistProcessor extends LangContentAssistProcessor {
	
	public GocodeContentAssistProcessor(GoEditor editor) {
		super(editor);
	}
	
	@Override
	protected ICompletionProposal[] doComputeCompletionProposals(int offset, String filePath, IDocument document)
			throws CoreException {
		String prefix = GocodeContentAssistProcessor.lastWord(document, offset);
		
		return new GocodeCompletionProposalComputer(editor, filePath, offset,document, prefix)
			.computeCompletionProposals();
	}
	
	public static IProject getProjectFor(IEditorPart editor) {
		return EditorUtils.getAssociatedProject(editor.getEditorInput());
	}
	
	/**
	 * @param doc
	 * @param offset
	 * @return
	 */
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
