package com.googlecode.goclipse.gocode;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPath;
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
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPathEditorInput;

import com.googlecode.goclipse.editors.CompletionProcessor;

public class ContentAssistProcessor implements IContentAssistProcessor {

	GoCodeClient client = new GoCodeClient();
	private Image go = com.googlecode.goclipse.Activator.getImageDescriptor("icons/orange_cube16.png").createImage();
	private Image funcImage = com.googlecode.goclipse.Activator.getImageDescriptor("icons/func16.png").createImage();
	private Image interfaceImage = com.googlecode.goclipse.Activator.getImageDescriptor("icons/interface_alt24.png").createImage();

	
	public ContentAssistProcessor() {
		// TODO Auto-generated constructor stub
	}

	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
		IPath path = null;
		try {
			IEditorPart editor = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			IEditorInput input = editor.getEditorInput();
			if (input instanceof IPathEditorInput) {
				IPathEditorInput pathInput = (IPathEditorInput) input;
				path = pathInput.getPath();
			}
		} catch (NullPointerException npe) {
		}
        ArrayList<ICompletionProposal> results = new ArrayList<ICompletionProposal>();
		if (path != null){
			String fileName = path.toOSString();
			IDocument document = viewer.getDocument();
			List<String> completions = client.getCompletions(fileName, document.get(), offset);
	
			if (completions != null) {
				for (String string : completions) {
					String prefix = "";
			         prefix = lastWord(document, offset);

					int firstComma = string.indexOf(",,");
					int secondComma = string.indexOf(",,", firstComma+2);
					if (firstComma != -1 && secondComma != -1) {
						String type = string.substring(0, firstComma);
						String identifier = string.substring(firstComma+2, secondComma);
						String spec = string.substring(secondComma+2);
						
						String descriptiveString = identifier+" : "+spec;
						IContextInformation info = new ContextInformation(descriptiveString, "");
						//MessageFormat.format(JavaEditorMessages.getString("CompletionProcessor.Proposal.ContextInfo.pattern"), new Object[] { fgProposals[i] })); //$NON-NLS-1$
						Image image = go;
						if(descriptiveString!=null && descriptiveString.contains(" : func")){
						   image = funcImage;
						}
						else if(descriptiveString!=null && descriptiveString.contains(" : interface")){
							image = interfaceImage;
						}
						String substr = identifier.substring(prefix.length());
						results.add(new CompletionProposal(substr, offset, 0, identifier.length() - prefix.length(), image, descriptiveString, info, ""));
					}
				}
			}
		}
		return results.toArray(new ICompletionProposal[] {});
	}
	
	/**
	 * 
	 * @param doc
	 * @param offset
	 * @return
	 */
	public static String lastWord(IDocument doc, int offset) {
		try {
			for (int n = offset - 1; n >= 0; n--) {
				char c = doc.getChar(n);
				if (!Character.isJavaIdentifierPart(c))
					return doc.get(n + 1, offset - n - 1);
			}
		} catch (BadLocationException e) {
			// ... log the exception ...
		}
		return "";
	}

	public IContextInformation[] computeContextInformation(ITextViewer viewer,
			int offset) {
		// TODO Auto-generated method stub
		return null;
	}

	public char[] getCompletionProposalAutoActivationCharacters() {
		return new char[] {'.'};
	}

	public char[] getContextInformationAutoActivationCharacters() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return client.getError();
	}

	public IContextInformationValidator getContextInformationValidator() {
		// TODO Auto-generated method stub
		return null;
	}

}
