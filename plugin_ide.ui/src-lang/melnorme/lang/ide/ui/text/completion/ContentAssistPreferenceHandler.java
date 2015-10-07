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

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.Color;

import melnorme.lang.ide.core.utils.prefs.PreferenceHelper;
import melnorme.lang.ide.ui.ContentAssistConstants;
import melnorme.lang.ide.ui.ContentAssistPreferences;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.editor.ProjectionViewerExt;
import melnorme.lang.ide.ui.editor.SourceViewerConfigurer;
import melnorme.lang.ide.ui.preferences.ColorPreference;
import melnorme.util.swt.jface.text.ColorManager2;
import melnorme.utilbox.fields.IFieldValueListener;
import melnorme.utilbox.ownership.IOwner;


public class ContentAssistPreferenceHandler extends SourceViewerConfigurer 
	implements ContentAssistConstants, ContentAssistPreferences {
	
	protected final ContentAssistantExt assistant;
	
	public ContentAssistPreferenceHandler(ContentAssistantExt contentAssistantExt, IPreferenceStore prefStore,
			ProjectionViewerExt sourceViewer) {
		super(prefStore, sourceViewer);
		this.assistant = assertNotNull(contentAssistantExt);
	}
	
	protected ColorManager2 getColorManager() {
		return LangUIPlugin.getInstance().getColorManager();
	}
	
	protected Color getColor(ColorPreference colorPref) {
		return getColorManager().getColor(colorPref.get());
	}
	
	/* -----------------  ----------------- */
	
	@Override
	protected void doConfigureViewer() {
		assistant.enableAutoActivation(true);
		
		listenToField(AUTO_INSERT__SingleProposals, () -> {
			assistant.enableAutoInsert(AUTO_INSERT__SingleProposals.get());
		});
		
		listenToField(AUTO_INSERT__CommonPrefixes, () -> {
			assistant.enablePrefixCompletion(AUTO_INSERT__CommonPrefixes.get());
		});
		
		listenToField(AUTO_ACTIVATE__DotTrigger, () -> {
			setAutoActivationTriggers(assistant, store);
		});
		listenToField(AUTO_ACTIVATE__DoubleColonTrigger, () -> {
			setAutoActivationTriggers(assistant, store);
		});
		listenToField(AUTO_ACTIVATE__AlphaNumericTrigger, () -> {
			setAutoActivationTriggers(assistant, store);
		});
		
		listenToField(AUTO_ACTIVATE__Delay, () -> {
			assistant.setAutoActivationDelay(AUTO_ACTIVATE__Delay.get());
		});
		
		
		listenToField(PROPOSALS_FOREGROUND_2, () -> {
			assistant.setProposalSelectorForeground(getColor(PROPOSALS_FOREGROUND_2));
		});
		
		listenToField(PROPOSALS_BACKGROUND_2, () -> {
			assistant.setProposalSelectorBackground(getColor(PROPOSALS_BACKGROUND_2));
		});
		
		listenToField(PARAMETERS_BACKGROUND_2, () -> {
			Color paramsFg = getColor(PARAMETERS_FOREGROUND_2);
			assistant.setContextInformationPopupForeground(paramsFg);
			assistant.setContextSelectorForeground(paramsFg);
		});
		
		listenToField(PROPOSALS_FOREGROUND_2, () -> {
			Color paramsBg = getColor(PARAMETERS_BACKGROUND_2);
			assistant.setContextInformationPopupBackground(paramsBg);
			assistant.setContextSelectorBackground(paramsBg);
		});
		
	}
	
	public <T> void listenToField(PreferenceHelper<T> colorPreference, IFieldValueListener listener) {
		IOwner configurationOwned = sourceViewer.getConfigurationOwned();
		
		colorPreference.getGlobalField().addOwnedListener(configurationOwned, true, listener);
	}
	
	@Override
	protected void handlePropertyChange(PropertyChangeEvent event) {
	}
	
	@Override
	protected void doUnconfigureViewer() {
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