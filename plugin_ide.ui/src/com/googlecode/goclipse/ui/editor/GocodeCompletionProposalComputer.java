package com.googlecode.goclipse.ui.editor;

import java.util.ArrayList;
import java.util.List;

import melnorme.lang.ide.core.LangCore;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorPart;

import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.tooling.env.GoEnvironment;
import com.googlecode.goclipse.ui.GoPluginImages;
import com.googlecode.goclipse.ui.GoUIPlugin;
import com.googlecode.goclipse.ui.actions.GocodeClient;
import com.googlecode.goclipse.ui.editor.LangContentAssistProcessor.ILangCompletionProposalComputer;

public class GocodeCompletionProposalComputer implements ILangCompletionProposalComputer {
	
	protected final String filePath;
	protected final int offset;
	protected final IDocument document;
	protected final String prefix;
	
	protected final IProject project;
	protected final IPath gocodePath;
	
	public GocodeCompletionProposalComputer(IEditorPart editor, String filePath, int offset, IDocument document,
			String prefix) throws CoreException {
		this.filePath = filePath;
		this.offset = offset;
		this.document = document;
		this.prefix = prefix;
		
		this.gocodePath = GoUIPlugin.prepareGocodeManager_inUI().getGocodePath();
		if (gocodePath == null) {
			throw LangCore.createCoreException("Error: gocode path not provided.", null);
		}
		project = GocodeContentAssistProcessor.getProjectFor(editor);
	}
	
	@Override
	public ICompletionProposal[] computeCompletionProposals()
			throws CoreException {
		
		ArrayList<ICompletionProposal> results = new ArrayList<ICompletionProposal>();
		
		try {
			GoEnvironment goEnvironment = GoProjectEnvironment.getGoEnvironment(project);
			
			// TODO: we should run this operation outside the UI thread.
			IProgressMonitor pm = new TimeoutProgressMonitor(5000, true);
			GocodeClient client = new GocodeClient(gocodePath.toOSString(), goEnvironment, pm);
			
			ExternalProcessResult processResult = client.execute(filePath, document.get(), offset);
			String stdout = processResult.getStdOutBytes().toString(StringUtil.UTF8);
			List<String> completions = new ArrayList2<>(GocodeClient.LINE_SPLITTER.split(stdout));
			
//			CodeContext codeContext = codeContexts.get(filePath);
//			if (codeContext == null) {
//				try {
//					codeContext = CodeContext.getCodeContext(project, filePath, document.get());
//				} catch (IOException e) {
//					throw LangCore.createCoreException("Error during code Context:", e);
//				}
//			}
			
			
			for (String completionEntry : completions) {
				handleResult(offset, /*codeContext,*/ results, prefix, completionEntry);
			}
			
			return results.toArray(new ICompletionProposal[] {});
		} catch (CommonException e) {
			throw LangCore.createCoreException(e.getMessage(), e.getCause());
		}
		
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