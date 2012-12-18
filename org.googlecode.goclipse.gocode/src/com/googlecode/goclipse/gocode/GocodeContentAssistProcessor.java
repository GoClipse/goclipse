package com.googlecode.goclipse.gocode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ContextInformation;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPathEditorInput;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.go.CodeContext;
import com.googlecode.goclipse.utils.IContentAssistProcessorExt;

/**
 * Implement an IContentAssistProcessor (and IContentAssistProcessorExt), and delegate the work
 * through to the GoCodeClient class. This IContentAssistProcessor is declared in the plugin.xml,
 * and is called from the GoEditorSourceViewerConfiguration class in the main GoClipse plugin.
 */
public class GocodeContentAssistProcessor implements IContentAssistProcessorExt {

  private GocodeClient client = new GocodeClient();

  private static HashMap<String, CodeContext> codeContexts = new HashMap<String, CodeContext>();

  private Image defaultImage = Activator.getImageDescriptor("icons/orange_cube16.png").createImage();
  private Image funcImage = Activator.getImageDescriptor("icons/function_co.png").createImage();
  private Image privateFuncImage = Activator.getImageDescriptor("icons/public_co.gif").createImage();
  private Image interfaceImage = Activator.getImageDescriptor("icons/interface.gif").createImage();
  private Image structImage = Activator.getImageDescriptor("icons/struct.png").createImage();
  private Image importImage = Activator.getImageDescriptor("icons/imp_obj.gif").createImage();
  private Image privateVarImage = Activator.getImageDescriptor("icons/field_private_obj.gif").createImage();
  private Image publicVarImage = Activator.getImageDescriptor("icons/field_public_obj.gif").createImage();

  @SuppressWarnings("unused")
  private Image localVarImage = Activator.getImageDescriptor("icons/variable_local_obj.gif").createImage();

  private IEditorPart editor;

  /**
   * 
   */
  public GocodeContentAssistProcessor() {

  }

  @Override
  public void setEditorContext(IEditorPart editor) {
    this.editor = editor;
  }

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

      @SuppressWarnings("unused")
      int linenumber = 0;

      try {
        // the following starts on line 0?, so we add 1 to the result
        linenumber = document.getLineOfOffset(offset) + 1;
      } catch (BadLocationException e1) {
        GocodePlugin.logError(e1);
      }

      if (codeContext == null) {
        try {
          codeContext = CodeContext.getCodeContext(getProjectFor(editor), filename, document.get());
        } catch (IOException e) {
          GocodePlugin.logError(e);
        }
      }

      if (path != null) {
        String fileName = path.toOSString();

        List<String> completions = client.getCompletions(getProjectFor(editor), fileName,
            document.get(), offset);

        if (completions == null) {
          completions = Collections.emptyList();
        }

        for (String string : completions) {
          String prefix = "";
          prefix = lastWord(document, offset);
          int firstComma = string.indexOf(",,");
          int secondComma = string.indexOf(",,", firstComma + 2);

          if (firstComma != -1 && secondComma != -1) {
            String type = string.substring(0, firstComma);

            if ("PANIC".equals(type)) {
              GocodePlugin.logError("PANIC from gocode - likely go/gocode version mismatch?");
              continue;
            }

            String identifier = string.substring(firstComma + 2, secondComma);

            if ("PANIC".equals(identifier)) {
              GocodePlugin.logError("PANIC from gocode - likely go/gocode version mismatch?");
              continue;
            }

            String spec = string.substring(secondComma + 2);

            String descriptiveString = identifier + " : " + spec;
            String description = codeContext.getDescriptionForName(identifier).trim();
            IContextInformation info = new ContextInformation(description, description);
            //MessageFormat.format(JavaEditorMessages.getString("CompletionProcessor.Proposal.ContextInfo.pattern"), new Object[] { fgProposals[i] })); //$NON-NLS-1$

            Image image = defaultImage;
            String substr = identifier.substring(prefix.length());
            int replacementLength = identifier.length() - prefix.length();

            if (descriptiveString != null && descriptiveString.contains(" : func")) {
              if (codeContext.isMethodName(identifier)) {
                image = privateFuncImage;

              } else {
                image = funcImage;
              }

              substr = identifier.substring(prefix.length()) + "()";
              replacementLength++;

            } else if (descriptiveString != null && descriptiveString.contains(" : interface")) {
              image = interfaceImage;

            } else if (descriptiveString != null && descriptiveString.contains(" : struct")) {
              image = structImage;

            } else if ("package".equals(type)) {
              image = importImage;

              substr = identifier.substring(prefix.length()) + ".";
              replacementLength++;
            } else {
              if (substr != null && substr.length() > 0 && Character.isUpperCase(substr.charAt(0))) {
                image = publicVarImage;

              } else {
                image = privateVarImage;
              }
            }

            // format the output
            descriptiveString = descriptiveString.replace(" : func", " ").replace(" : interface",
                " ").replace(" : struct", " ").replace("(", "( ").replace(")", " )");

            results.add(new CompletionProposal(identifier, offset - prefix.length(),
                prefix.length(), identifier.length(), image, descriptiveString, info, description));
          }
        }
      }
    }

    return results.toArray(new ICompletionProposal[] {});
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
      GocodePlugin.logError(e);
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
    return client.getError();
  }

  @Override
  public IContextInformationValidator getContextInformationValidator() {
    return null;
  }

}
