/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.text.completion;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import static melnorme.utilbox.core.CoreUtil.array;

import java.text.MessageFormat;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.bindings.TriggerSequence;
import org.eclipse.jface.bindings.keys.KeySequence;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ContentAssistEvent;
import org.eclipse.jface.text.contentassist.ICompletionListener;
import org.eclipse.jface.text.contentassist.ICompletionListenerExtension;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension3;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension4;
import org.eclipse.jface.text.contentassist.IContentAssistantExtension2;
import org.eclipse.jface.text.contentassist.IContentAssistantExtension3;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.keys.IBindingService;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;

import melnorme.lang.ide.core.text.ISourceBufferExt;
import melnorme.lang.ide.ui.LangImages;
import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.editor.EditorUtils;
import melnorme.lang.ide.ui.editor.hover.BrowserControlHover.BrowserControlCreator;
import melnorme.lang.ide.ui.templates.LangTemplateCompletionProposalComputer;
import melnorme.lang.ide.ui.utils.UIOperationsStatusHandler;
import melnorme.lang.tooling.utils.HTMLHelper;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.core.CommonException;

public class LangContentAssistProcessor extends ContenAssistProcessorExt {
	
	protected final ContentAssistantExt contentAssistant;
	protected final Indexable<CompletionProposalsGrouping> categories;
	protected final ISourceBufferExt sourceBuffer;
	protected final ITextEditor editor; // can be null
	protected final IProject project; // can be null
	
	public LangContentAssistProcessor(ContentAssistantExt contentAssistant, 
			Indexable<CompletionProposalsGrouping> groupings, ISourceBufferExt sourceBuffer, ITextEditor editor) {
		this.contentAssistant = assertNotNull(contentAssistant);
		this.categories = groupings;
		assertTrue(categories != null && categories.size() > 0);
		
		this.sourceBuffer = assertNotNull(sourceBuffer);
		this.editor = editor;
		this.project = editor == null ? null : EditorUtils.getAssociatedProject(editor.getEditorInput()); 
		
		contentAssistant.addCompletionListener(new CompletionSessionListener());
	}
	
	public static abstract class ContentAssistCategoriesBuilder {
		
		public ArrayList2<CompletionProposalsGrouping> getCategories() {
			ArrayList2<CompletionProposalsGrouping> categories = new ArrayList2<>();
			categories.addIfNotNull(createDefaultCategory());
			categories.addIfNotNull(createSnippetsCategory());
			return categories;
		}
		
		protected CompletionProposalsGrouping createDefaultCategory() {
			ArrayList2<ILangCompletionProposalComputer> computers = createDefaultCategoryComputers();
			return new CompletionProposalsGrouping("default", 
				LangUIMessages.ContentAssistProcessor_defaultProposalCategory, null, computers);
		}
		
		protected ArrayList2<ILangCompletionProposalComputer> createDefaultCategoryComputers() {
			ArrayList2<ILangCompletionProposalComputer> computers = new ArrayList2<>();
			computers.addIfNotNull(createDefaultSymbolsProposalComputer());
			computers.addIfNotNull(createSnippetsProposalComputer());
			return computers;
		}
		
		protected abstract ILangCompletionProposalComputer createDefaultSymbolsProposalComputer();
		
		protected CompletionProposalsGrouping createSnippetsCategory() {
			ArrayList2<ILangCompletionProposalComputer> computers = new ArrayList2<>();
			computers.addIfNotNull(createSnippetsProposalComputer());
			
			if(computers.isEmpty()) {
				return null;
			}
			
			return new CompletionProposalsGrouping("snippets", 
				LangUIMessages.ContentAssistProcessor_snippetsProposalCategory, null, computers);
		}
		
		protected ILangCompletionProposalComputer createSnippetsProposalComputer() {
			return new LangTemplateCompletionProposalComputer();
		}
		
	}
	
	/* -----------------  ----------------- */
	
	protected int invocationIteration = 0;
	protected boolean isAutoActivation = false;
	
	protected class CompletionSessionListener implements ICompletionListener, ICompletionListenerExtension {
		
		public CompletionSessionListener() {
		}
		
		@Override
		public void assistSessionStarted(ContentAssistEvent event) {
			if(event.processor != LangContentAssistProcessor.this)
				return;
			
			invocationIteration = 0;
			isAutoActivation = event.isAutoActivated;
			
			if (event.assistant instanceof IContentAssistantExtension2) {
				IContentAssistantExtension2 extension = (IContentAssistantExtension2) event.assistant;
				
				KeySequence binding = getGroupingIterationBinding();
				boolean repeatedModeEnabled = categories.size() > 1;
				
				setRepeatedModeStatus(extension, repeatedModeEnabled, binding);
			}
			
			listener_assistSessionStarted();
		}
		
		protected void setRepeatedModeStatus(IContentAssistantExtension2 caExt2, boolean enabled, KeySequence binding) {
			caExt2.setShowEmptyList(enabled);
			
			caExt2.setRepeatedInvocationMode(enabled);
			caExt2.setStatusLineVisible(enabled);
			if(enabled) {
				caExt2.setStatusMessage(createIterationMessage());
			}
			if (caExt2 instanceof IContentAssistantExtension3) {
				IContentAssistantExtension3 ext3 = (IContentAssistantExtension3) caExt2;
				ext3.setRepeatedInvocationTrigger(binding);
			}
		}
		
		@Override
		public void assistSessionRestarted(ContentAssistEvent event) {
			invocationIteration = 0;
		}
		
		@Override
		public void assistSessionEnded(ContentAssistEvent event) {
			if(event.processor != LangContentAssistProcessor.this)
				return;
			
			invocationIteration = 0;
			
			listener_assistSessionEnded();
		}
		
		@Override
		public void selectionChanged(ICompletionProposal proposal, boolean smartToggle) {
		}
		
	}
	
	protected CompletionProposalsGrouping getCurrentCategory() {
		return getCategory(invocationIteration);
	}
	
	protected CompletionProposalsGrouping getCategory(int categoryIndex) {
		assertTrue(categoryIndex >= 0);
		int cappedIndex = categoryIndex % categories.size();
		return categories.get(cappedIndex);
	}
	
	/* -----------------  ----------------- */
	
	protected void listener_assistSessionStarted() {
		for(CompletionProposalsGrouping cat : categories) {
			cat.sessionStarted();
		}
	}
	
	protected void listener_assistSessionEnded() {
		for(CompletionProposalsGrouping cat : categories) {
			cat.sessionEnded();
		}
	}
	
	@Override
	protected void resetComputeState() {
		super.resetComputeState();
		
		// These messages are iteration specific, so they need to be reset:
		contentAssistant.setStatusMessage(createIterationMessage());
		contentAssistant.setEmptyMessage(createEmptyMessage());
	}
	
	/* -----------------  ----------------- */
	
	@Override
	protected ICompletionProposal[] doComputeCompletionProposals(ITextViewer viewer, int offset) {
		
		CompletionProposalsGrouping cat = getCurrentCategory();
		invocationIteration++;
		
		Indexable<ICompletionProposal> proposals;
		try {
			proposals = cat.computeCompletionProposals(sourceBuffer, viewer, offset);
		} catch(CommonException ce) {
			handleExceptionInUI(ce);
			return null;
		}
		String errorMessage = cat.getErrorMessage();
		if(errorMessage != null) {
			if(isAutoActivation) {
				// don't popup, just display status line error
				setAndDisplayStatusLineErrorMessage("Error: " + errorMessage);
				return null;
			} else {
				Display.getCurrent().beep();
				return array(new ErrorCompletionProposal(errorMessage));
			}
		}
		
		return proposals.toArray(ICompletionProposal.class);
	}
	
	protected void handleExceptionInUI(CommonException ce) {
		UIOperationsStatusHandler.handleOperationStatus(LangUIMessages.ContentAssistProcessor_opName, ce);
	}
	
	@Override
	protected IContextInformation[] doComputeContextInformation(ITextViewer viewer, int offset) {
		
		CompletionProposalsGrouping cat = getCurrentCategory();
		invocationIteration++;
		
		Indexable<IContextInformation> proposals = cat.computeContextInformation(sourceBuffer, viewer, offset);
		setAndDisplayStatusLineErrorMessage(cat.getErrorMessage());
		
		return proposals.toArray(IContextInformation.class);
	}
	
	@Override
	public IContextInformationValidator getContextInformationValidator() {
		return null; // TODO: need to add proper support for this
	}
	
	protected void setAndDisplayStatusLineErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
		EditorUtils.setStatusLineErrorMessage(editor, errorMessage, null);
		Display.getCurrent().beep();
	}
	
	public class ErrorCompletionProposal 
		implements ICompletionProposal, ICompletionProposalExtension3, ICompletionProposalExtension4 {
		
		protected final String errorMessage;
		
		public ErrorCompletionProposal(String errorMessage) {
			this.errorMessage = assertNotNull(errorMessage);
		}
		
		@Override
		public Image getImage() {
			return LangImages.NAV_Error.createImage();
		}
		
		@Override
		public String getDisplayString() {
			return "An error occured during Content Assist.";
		}
		
		@Override
		public String getAdditionalProposalInfo() {
			return new HTMLHelper().wrapHTMLBody("<b>Error:</b><hr/> " + HTMLHelper.escapeToToHTML(errorMessage));
		}
		
		@Override
		public Point getSelection(IDocument document) {
			return null;
		}
		@Override
		public IContextInformation getContextInformation() {
			return null;
		}
		
		@Override
		public boolean isAutoInsertable() {
			return true;
		}
		
		@Override
		public void apply(IDocument document) {
			handleExceptionInUI(new CommonException(errorMessage));
		}
		
		@Override
		public IInformationControlCreator getInformationControlCreator() {
			return new BrowserControlCreator();
		}
		
		@Override
		public CharSequence getPrefixCompletionText(IDocument document, int completionOffset) {
			return null;
		}
		
		@Override
		public int getPrefixCompletionStart(IDocument document, int completionOffset) {
			return completionOffset;
		}
	}
	
	/* ----------------- Messages ----------------- */
	
	
	protected String createEmptyMessage() {
		if(invocationIteration == 0) {
			return MessageFormat.format(LangUIMessages.ContentAssistProcessor_emptyDefaultProposals, 
				getCurrentCategory().getName());
		}
		
		return MessageFormat.format(LangUIMessages.ContentAssistProcessor_empty_message, 
			getCurrentCategory().getName());
	}
	
	protected String createIterationMessage() {
		TriggerSequence binding = getGroupingIterationBinding();
		String nextCategoryLabel = getCategory(invocationIteration + 1).getName();
		
		if(binding == null) {
			return MessageFormat.format(LangUIMessages.ContentAssistProcessor_toggle_affordance_click_gesture, 
				getCurrentCategory().getName(), nextCategoryLabel, null);
		} else {
			return MessageFormat.format(LangUIMessages.ContentAssistProcessor_toggle_affordance_press_gesture, 
				getCurrentCategory().getName(), nextCategoryLabel, binding.format());
		}
	}
	
	protected KeySequence getGroupingIterationBinding() {
		IBindingService bindingSvc = (IBindingService) PlatformUI.getWorkbench().getAdapter(IBindingService.class);
		TriggerSequence binding = bindingSvc.getBestActiveBindingFor(
			ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);
		if(binding instanceof KeySequence)
			return (KeySequence) binding;
		return null;
    }
	
}