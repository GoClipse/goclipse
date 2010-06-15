package com.googlecode.goclipse.editors;

import java.util.ArrayList;

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

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.go.Keywords;

/**
 * 
 * @author steel
 */
public class CompletionProcessor implements IContentAssistProcessor {
   
   private Image go = Activator.getImageDescriptor("icons/go-icon16.png").createImage();
   
	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
	   String prefix = "";
	   String indent = "";
	   try {
         IDocument document = viewer.getDocument();
         prefix = lastWord(document, offset);
         indent = lastIndent(document, offset);
//         EscriptModel model = EscriptModel.getModel(document, null);
         //model.getContentProposals(prefix, indent, offset, result);
         ArrayList<ICompletionProposal> result= new ArrayList<ICompletionProposal>();
         
         Keywords[] values = Keywords.values();
         for (int i= 0; i < values.length; i++) {
            String keyword = values[i].getValue();
            if(keyword.startsWith(prefix)){
               IContextInformation info= new ContextInformation(keyword, "XXX");
               //MessageFormat.format(JavaEditorMessages.getString("CompletionProcessor.Proposal.ContextInfo.pattern"), new Object[] { fgProposals[i] })); //$NON-NLS-1$
               result.add(new CompletionProposal(
                     keyword.substring(prefix.length(), keyword.length())+" ", 
                     offset, 
                     0, 
                     keyword.length() - prefix.length()+1, 
                     go, 
                     keyword, 
                     info, 
                     values[i].getDescription()));
            }
         }
         return result.toArray(new ICompletionProposal[result.size()]);
      } 
	   catch (Exception e) {
         // ... log the exception ...
         return null;
      }

	   
	}
	
	private String lastWord(IDocument doc, int offset) {
      try {
         for (int n = offset-1; n >= 0; n--) {
           char c = doc.getChar(n);
           if (!Character.isJavaIdentifierPart(c))
             return doc.get(n + 1, offset-n-1);
         }
      } catch (BadLocationException e) {
         // ... log the exception ...
      }
      return "";
   }
	
	private String lastIndent(IDocument doc, int offset) {
      try {
         int start = offset-1; 
         while (start >= 0 && doc.getChar(start)!= '\n') start--;
         int end = start;
         while (end < offset && Character.isSpaceChar(doc.getChar(end))) end++;
         return doc.get(start+1, end-start-1);
      } catch (BadLocationException e) {
         //SysUtils.debug(e);
      }
      return "";
   }



	@Override
	public IContextInformation[] computeContextInformation(ITextViewer viewer,
			int offset) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IContextInformationValidator getContextInformationValidator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}

}
