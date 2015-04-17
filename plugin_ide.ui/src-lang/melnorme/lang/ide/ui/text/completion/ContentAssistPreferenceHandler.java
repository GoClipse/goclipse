/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.text.completion;

import melnorme.lang.ide.ui.ContentAssistConstants;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.text.SimpleLangSourceViewerConfiguration;

import org.eclipse.cdt.ui.text.IColorManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;


public class ContentAssistPreferenceHandler implements ContentAssistConstants {
	
	public ContentAssistPreferenceHandler() {
	}
	
	protected IColorManager getColorManager() {
		return LangUIPlugin.getInstance().getColorManager();
	}
	
	protected Color getColor(IPreferenceStore store, String key) {
		RGB rgb = PreferenceConverter.getColor(store, key);
		return getColorManager().getColor(rgb);
	}
	
	/* -----------------  ----------------- */
	
	public void changeConfiguration(ContentAssistant assistant, IPreferenceStore store, PropertyChangeEvent event) {
		setConfigurationOption(assistant, store, event.getProperty());
	}
	
	public void configure(ContentAssistant assistant, IPreferenceStore store) {
		
		setConfigurationOption(assistant, store, AUTOACTIVATION);
		setConfigurationOption(assistant, store, AUTOACTIVATION_DELAY);
		setConfigurationOption(assistant, store, PROPOSALS_FOREGROUND);
		setConfigurationOption(assistant, store, PROPOSALS_BACKGROUND);
		setConfigurationOption(assistant, store, PARAMETERS_FOREGROUND);
		setConfigurationOption(assistant, store, PARAMETERS_BACKGROUND);
		setConfigurationOption(assistant, store, AUTOINSERT);
		setConfigurationOption(assistant, store, PREFIX_COMPLETION);
		
		setConfigurationOption(assistant, store, AUTOACTIVATION_TRIGGERS);
	}
	
	protected void setConfigurationOption(ContentAssistant assistant, IPreferenceStore store, String key) {
		
		if(AUTOACTIVATION.equals(key)) {
			assistant.enableAutoActivation(store.getBoolean(AUTOACTIVATION));
		} else if(AUTOACTIVATION_DELAY.equals(key)) {
			assistant.setAutoActivationDelay(store.getInt(AUTOACTIVATION_DELAY));
		} else if(PROPOSALS_FOREGROUND.equals(key)) {
			assistant.setProposalSelectorForeground(getColor(store, PROPOSALS_FOREGROUND));
		} else if(PROPOSALS_BACKGROUND.equals(key)) {
			assistant.setProposalSelectorBackground(getColor(store, PROPOSALS_BACKGROUND));
		} else if(PARAMETERS_FOREGROUND.equals(key)) {
			Color paramsFg = getColor(store, PARAMETERS_FOREGROUND);
			assistant.setContextInformationPopupForeground(paramsFg);
			assistant.setContextSelectorForeground(paramsFg);
		} else if(PARAMETERS_BACKGROUND.equals(key)) {
			Color paramsBg = getColor(store, PARAMETERS_BACKGROUND);
			assistant.setContextInformationPopupBackground(paramsBg);
			assistant.setContextSelectorBackground(paramsBg);
		} else if(AUTOINSERT.equals(key)) {
			assistant.enableAutoInsert(store.getBoolean(AUTOINSERT));
		} else if(PREFIX_COMPLETION.equals(key)) {
			assistant.enablePrefixCompletion(store.getBoolean(PREFIX_COMPLETION));
		}
		
		changeLangContentAssistProcessor(assistant, store, key);
	}
	
	protected void changeLangContentAssistProcessor(ContentAssistant assistant, IPreferenceStore store, String key) {
		LangContentAssistProcessor jcp = getLangContentAssistProcessor(assistant);
		if(jcp == null)
			return;
		
		if(AUTOACTIVATION_TRIGGERS.equals(key)) {
			String triggers = store.getString(AUTOACTIVATION_TRIGGERS);
			if(triggers != null) {
				jcp.setCompletionProposalAutoActivationCharacters(triggers.toCharArray());
			}
		}
	}
	
	protected LangContentAssistProcessor getLangContentAssistProcessor(ContentAssistant assistant) {
		IContentAssistProcessor cap = assistant.getContentAssistProcessor(IDocument.DEFAULT_CONTENT_TYPE);
		
		if(cap instanceof LangContentAssistProcessor)
			return (LangContentAssistProcessor) cap;
		return null;
	}
	
	/* -----------------  ----------------- */
	
	public String getAdditionalInfoAffordanceString() {
		return SimpleLangSourceViewerConfiguration.getAdditionalInfoAffordanceString();
	}
	
}