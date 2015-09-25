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
import melnorme.lang.ide.ui.ContentAssistConstants;
import melnorme.lang.ide.ui.ContentAssistPreferences;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.editor.ProjectionViewerExt;
import melnorme.lang.ide.ui.editor.SourceViewerConfigurer;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

import _org.eclipse.cdt.ui.text.IColorManager;


public class ContentAssistPreferenceHandler extends SourceViewerConfigurer 
	implements ContentAssistConstants, ContentAssistPreferences {
	
	protected final ContentAssistantExt assistant;
	
	public ContentAssistPreferenceHandler(ContentAssistantExt contentAssistantExt, IPreferenceStore prefStore,
			ProjectionViewerExt sourceViewer) {
		super(prefStore, sourceViewer);
		this.assistant = assertNotNull(contentAssistantExt);
	}
	
	protected IColorManager getColorManager() {
		return LangUIPlugin.getInstance().getColorManager();
	}
	
	protected Color getColor(IPreferenceStore store, String key) {
		RGB rgb = PreferenceConverter.getColor(store, key);
		return getColorManager().getColor(rgb);
	}
	
	@Override
	protected void handlePropertyChange(PropertyChangeEvent event) {
		setConfigurationOption(assistant, store, event.getProperty());
	}
	
	/* -----------------  ----------------- */
	
	@Override
	protected void doConfigureViewer() {
		assistant.enableAutoActivation(true);
		
		setConfigurationOption(assistant, store, AUTO_INSERT__SingleProposals.key);
		setConfigurationOption(assistant, store, AUTO_INSERT__CommonPrefixes.key);
		
		setConfigurationOption(assistant, store, AUTO_ACTIVATE__DotTrigger.key);
		setConfigurationOption(assistant, store, AUTO_ACTIVATE__DoubleColonTrigger.key);
		setConfigurationOption(assistant, store, AUTO_ACTIVATE__AlphaNumericTrigger.key);
		setConfigurationOption(assistant, store, AUTO_ACTIVATE__Delay.key);
		
		setConfigurationOption(assistant, store, PROPOSALS_FOREGROUND);
		setConfigurationOption(assistant, store, PROPOSALS_BACKGROUND);
		setConfigurationOption(assistant, store, PARAMETERS_FOREGROUND);
		setConfigurationOption(assistant, store, PARAMETERS_BACKGROUND);
	}
	
	@Override
	protected void doUnconfigureViewer() {
		// No need to do anything
	}
	
	protected void setConfigurationOption(ContentAssistant assistant, IPreferenceStore store, String key) {
		// store is not used for most pref keys, in any case it should be the standard UI pref store.
		// TODO: this store usage/non-usage is not very elegant/safe, best if this could be cleaned up.
		
		if(equalsKey(AUTO_INSERT__SingleProposals.key, key)) {
			assistant.enableAutoInsert(AUTO_INSERT__SingleProposals.get());
		}
		else if(equalsKey(AUTO_INSERT__CommonPrefixes.key, key)) {
			assistant.enablePrefixCompletion(AUTO_INSERT__CommonPrefixes.get());
		}
		
		else if(
				equalsKey(AUTO_ACTIVATE__DotTrigger.key, key) ||
				equalsKey(AUTO_ACTIVATE__DoubleColonTrigger.key, key) ||
				equalsKey(AUTO_ACTIVATE__AlphaNumericTrigger.key, key)) {
			setAutoActivationTriggers(assistant, store);
		}
		else if(equalsKey(AUTO_ACTIVATE__Delay.key, key)) {
			assistant.setAutoActivationDelay(AUTO_ACTIVATE__Delay.get());
		}
		
		else if(equalsKey(PROPOSALS_FOREGROUND, key)) {
			assistant.setProposalSelectorForeground(getColor(store, PROPOSALS_FOREGROUND));
		}
		else if(equalsKey(PROPOSALS_BACKGROUND, key)) {
			assistant.setProposalSelectorBackground(getColor(store, PROPOSALS_BACKGROUND));
		}
		else if(equalsKey(PARAMETERS_FOREGROUND, key)) {
			Color paramsFg = getColor(store, PARAMETERS_FOREGROUND);
			assistant.setContextInformationPopupForeground(paramsFg);
			assistant.setContextSelectorForeground(paramsFg);
		}
		else if(equalsKey(PARAMETERS_BACKGROUND, key)) {
			Color paramsBg = getColor(store, PARAMETERS_BACKGROUND);
			assistant.setContextInformationPopupBackground(paramsBg);
			assistant.setContextSelectorBackground(paramsBg);
		}
		
	}
	
	protected static boolean equalsKey(String keyA, String keyB) {
		return keyA.equals(keyB);
	}
	
	protected void setAutoActivationTriggers(ContentAssistant assistant, 
			@SuppressWarnings("unused") IPreferenceStore store) {
		LangContentAssistProcessor jcp = getLangContentAssistProcessor(assistant);
		if(jcp == null)
			return;
		
		String triggers = "";
		if(AUTO_ACTIVATE__DotTrigger.get()) {
			triggers += ".";
		}
		if(AUTO_ACTIVATE__DoubleColonTrigger.get()) {
			triggers += ":"; // Not perfect match, will match single colons too...
		}
		if(AUTO_ACTIVATE__AlphaNumericTrigger.get()) {
			triggers +="abcdefghijklmnopqrstuvwxyz";
			triggers +="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		}
		
		if(triggers != null) {
			jcp.setCompletionProposalAutoActivationCharacters(triggers.toCharArray());
		}
	}
	
	protected LangContentAssistProcessor getLangContentAssistProcessor(ContentAssistant assistant) {
		IContentAssistProcessor cap = assistant.getContentAssistProcessor(IDocument.DEFAULT_CONTENT_TYPE);
		
		if(cap instanceof LangContentAssistProcessor)
			return (LangContentAssistProcessor) cap;
		return null;
	}
	
}