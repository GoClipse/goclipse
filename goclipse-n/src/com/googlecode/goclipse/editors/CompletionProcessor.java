package com.googlecode.goclipse.editors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.eclipse.ui.IPathEditorInput;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.go.CodeContext;
import com.googlecode.goclipse.go.lang.model.Node;
import com.googlecode.goclipse.go.lib.indexer.Keywords;

/**
 * @author steel
 */
public class CompletionProcessor implements IContentAssistProcessor {

  private static HashMap<String, CodeContext> codeContexts = new HashMap<String, CodeContext>();

  private Image go = com.googlecode.goclipse.Activator.getImageDescriptor("icons/go.png").createImage();

  private GoEditor editor;

  public CompletionProcessor(GoEditor editor) {
    this.editor = editor;
  }

  /**
	 * 
	 */
  @Override
  public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
    IPath path = null;

    try {
      IEditorInput input = editor.getEditorInput();

      if (input instanceof IPathEditorInput) {
        IPathEditorInput pathInput = (IPathEditorInput) input;
        path = pathInput.getPath();
      }
    } catch (NullPointerException npe) {

    }

    ArrayList<ICompletionProposal> results = new ArrayList<ICompletionProposal>();

    if (path != null) {
      String filename = path.toOSString();
      IDocument document = viewer.getDocument();

      CodeContext codeContext = codeContexts.get(filename);
      int linenumber = 0;

      try {
        // the following starts on line 0?, so we add 1 to the result
        linenumber = document.getLineOfOffset(offset) + 1;
      } catch (BadLocationException e1) {
        Activator.logError(e1);
      }

      if (codeContext == null) {

        try {
          codeContext = CodeContext.getCodeContext(filename, document.get());
        } catch (IOException e) {
          Activator.logError(e);
        }
      }

      String prefix = lastWord(document, offset);
      String indent = lastIndent(document, offset);
      String line = document.get().split("\\r?\\n")[linenumber];

      if (codeContext != null) {

        List<Node> units = codeContext.getCompletionsForString(line, prefix, linenumber);

        for (Node unit : units) {
          IContextInformation info = new ContextInformation(unit.getDocumentation(),
              unit.getDocumentation());

          int prefix_len = prefix.lastIndexOf('.');
          if (unit.getInsertionText() != null) {
            int len = unit.getInsertionText().length();
            if (len > 0) {
              results.add(new CompletionProposal(unit.getInsertionText(), offset, 0,
                  unit.getInsertionText().length() - 1, unit.getImage(), unit.getName(), info,
                  unit.getDocumentation()));
            }
          }
        }
      } else {
        // The following is a simple place holder for a more complex
        // completion
        Keywords[] values = Keywords.values();
        for (int i = 0; i < values.length; i++) {
          String keyword = values[i].getValue();
          if (keyword.startsWith(prefix)) {
            IContextInformation info = new ContextInformation(keyword, "XXX");
            //MessageFormat.format(JavaEditorMessages.getString("CompletionProcessor.Proposal.ContextInfo.pattern"), new Object[] { fgProposals[i] })); //$NON-NLS-1$
            results.add(new CompletionProposal(keyword.substring(prefix.length(), keyword.length())
                + " ", offset, 0, keyword.length() - prefix.length() + 1, go, keyword, info,
                values[i].getDescription()));
          }
        }
      }
      
      return results.toArray(new ICompletionProposal[results.size()]);
    }
    
    return null;
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
        if (!Character.isJavaIdentifierPart(c) && c != '.')
          return doc.get(n + 1, offset - n - 1);
      }
    } catch (BadLocationException e) {
      // ... log the exception ...
    }
    return "";
  }

  /**
   * @param doc
   * @param offset
   * @return
   */
  public static String lastIndent(IDocument doc, int offset) {
    try {
      int start = offset - 1;
      while (start >= 0 && doc.getChar(start) != '\n')
        start--;
      int end = start;
      while (end < offset && Character.isSpaceChar(doc.getChar(end)))
        end++;
      return doc.get(start + 1, end - start - 1);
    } catch (BadLocationException e) {
      // SysUtils.debug(e);
    }
    return "";
  }

  @Override
  public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public char[] getCompletionProposalAutoActivationCharacters() {
    // TODO Auto-generated method stub
    return new char[] {'.'};
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
