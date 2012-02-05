package com.googlecode.goclipse.gocode;

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
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPathEditorInput;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.go.CodeContext;

/**
 * 
 */
public class ContentAssistProcessor implements IContentAssistProcessor {
	
	GoCodeClient client = new GoCodeClient();
	
	private static HashMap<String, CodeContext> codeContexts = new HashMap<String, CodeContext>();
	
	private Image defaultImage     = com.googlecode.goclipse.Activator.getImageDescriptor("icons/orange_cube16.png").createImage();
	private Image funcImage        = com.googlecode.goclipse.Activator.getImageDescriptor("icons/function_co.png").createImage();
	private Image privateFuncImage = com.googlecode.goclipse.Activator.getImageDescriptor("icons/public_co.gif").createImage();
	private Image interfaceImage   = com.googlecode.goclipse.Activator.getImageDescriptor("icons/interface.gif").createImage();
	private Image structImage 	   = com.googlecode.goclipse.Activator.getImageDescriptor("icons/struct.png").createImage();
	private Image importImage      = com.googlecode.goclipse.Activator.getImageDescriptor("icons/imp_obj.gif").createImage();
	private Image privateVarImage  = com.googlecode.goclipse.Activator.getImageDescriptor("icons/field_private_obj.gif").createImage();
	private Image publicVarImage   = com.googlecode.goclipse.Activator.getImageDescriptor("icons/field_public_obj.gif").createImage();
	@SuppressWarnings("unused")
  private Image localVarImage    = com.googlecode.goclipse.Activator.getImageDescriptor("icons/variable_local_obj.gif").createImage();
		
	public int A = 0;
	
	@SuppressWarnings("unused")
  private int A(){
		return 0;
	}
	
	/**
	 * 
	 */
	public ContentAssistProcessor() {
		
	}
	
	@Override
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
		
        if(path!=null){
			
			String      filename 	= path.toOSString();
			IDocument   document 	= viewer.getDocument();
			CodeContext codeContext = codeContexts.get(filename);
			
			@SuppressWarnings("unused")
			int linenumber = 0;
			
			try {
				// the following starts on line 0?, so we add 1 to the result
				linenumber = document.getLineOfOffset(offset)+1;
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
			
			if (codeContext == null) {
				
				try {
					codeContext = CodeContext.getCodeContext(filename, document.get());
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			
	        if (path != null) {
			
	        	String fileName = path.toOSString();
				List<String> completions  = client.getCompletions(fileName, document.get(), offset);
					
				if (completions != null) {
					
					for (String string : completions) {
						
						String prefix = "";
				        prefix = lastWord(document, offset);
						int firstComma = string.indexOf(",,");
						int secondComma = string.indexOf(",,", firstComma+2);
						
						if (firstComma != -1 && secondComma != -1) {
							
							String type       = string.substring(0, firstComma);
							String identifier = string.substring(firstComma+2, secondComma);
							String spec       = string.substring(secondComma+2);
							
							String descriptiveString = identifier+" : "+spec;
							String description       = codeContext.getDescriptionForName(identifier).trim();
							IContextInformation info = new ContextInformation(description,description);
							//MessageFormat.format(JavaEditorMessages.getString("CompletionProcessor.Proposal.ContextInfo.pattern"), new Object[] { fgProposals[i] })); //$NON-NLS-1$
							
							Image image = defaultImage;
							String substr = identifier.substring(prefix.length());
							int replacementLength = identifier.length() - prefix.length();
							
							if(descriptiveString!=null && descriptiveString.contains(" : func")) {
								if(codeContext.isMethodName(identifier)){
									image = privateFuncImage;
								} else {
									image = funcImage;
								}
							    substr = identifier.substring(prefix.length())+"()";
							    replacementLength++;
							} else if(descriptiveString!=null && descriptiveString.contains(" : interface")) {
								image = interfaceImage;
							} else if(descriptiveString!=null && descriptiveString.contains(" : struct")) {
								image = structImage;
							} else if("package".equals(type)) {
								image = importImage;
								substr = identifier.substring(prefix.length())+".";
								replacementLength++;
							} else {
								if (substr!=null && substr.length()>0 && Character.isUpperCase(substr.charAt(0))) {
									image = publicVarImage;
								} else {
									image = privateVarImage;
								}
							}
							
							// format the output
							descriptiveString = descriptiveString.replace(" : func", " ").replace(
									" : interface", " ").replace(" : struct", " ").replace(
											"(", "( ").replace(")", " )");
							
							results.add(new CompletionProposal(substr, offset, 0, replacementLength, image, descriptiveString, info, description));
						}
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
	private static String lastWord(IDocument doc, int offset) {
		try {
			for (int n = offset - 1; n >= 0; n--) {
				char c = doc.getChar(n);
				if (!Character.isJavaIdentifierPart(c))
					return doc.get(n + 1, offset - n - 1);
			}
		} catch (BadLocationException e) {
			Activator.logError(e);
		}
		return "";
	}
	
	@Override
  public IContextInformation[] computeContextInformation(ITextViewer viewer,	int offset) {
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
