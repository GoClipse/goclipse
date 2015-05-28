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
package melnorme.lang.ide.ui.editor;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.text.SimpleLangSourceViewerConfiguration;
import melnorme.lang.ide.ui.text.completion.ContentAssistantExt;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.ownership.IDisposable;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.information.IInformationPresenter;
import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;

public class LangSourceViewer extends JavaSourceViewer_Mod implements ISourceViewerExt {
	
	public LangSourceViewer(Composite parent, IVerticalRuler verticalRuler, IOverviewRuler overviewRuler,
			boolean showAnnotationsOverview, int styles, IPreferenceStore store) {
		super(parent, verticalRuler, overviewRuler, showAnnotationsOverview, styles, store);
	}
	
	protected final ArrayList2<IDisposable> configurationDisposables = new ArrayList2<>();
	
	public <T extends IDisposable> T addConfigurationOwned(T ownedObject) {
		configurationDisposables.add(ownedObject);
		return ownedObject;
	}
	
	protected SimpleLangSourceViewerConfiguration sourceViewerConfig;
	
	@Override
	public void configure(SourceViewerConfiguration configuration) {
		assertTrue(sourceViewerConfig == null);
		
		super.configure(configuration);
		
		if(configuration instanceof SimpleLangSourceViewerConfiguration) {
			sourceViewerConfig = (SimpleLangSourceViewerConfiguration) configuration;
			
			StyledText textWidget = getTextWidget();
			if (textWidget != null) {
				textWidget.setFont(JFaceResources.getFont(sourceViewerConfig.getFontPropertyPreferenceKey()));
			}
			
			sourceViewerConfig.setupCustomConfiguration(this);
		}
	}
	
	@Override
	protected void configure_beforeViewerColors(SourceViewerConfiguration configuration) {
		super.configure_beforeViewerColors(configuration);
	}
	
	@Override
	public void unconfigure() {
		for(IDisposable disposable : configurationDisposables) {
			disposable.dispose();
		}
		configurationDisposables.clear();
		
		super.unconfigure();
		sourceViewerConfig = null;
	}
	
	/* -----------------  ----------------- */
	
	@Override
	protected int getEmptySelectionChangedEventDelay() {
		// Change selection delay when moving caret. 
		// This could have some unintendend consequences, but seems to be working well so far.
		return 0; 
	}
	
	/* ----------------- Quick Outline ----------------- */
	
	protected IInformationPresenter outlinePresenter;
	
	public void setOutlinePresenter(IInformationPresenter outlinePresenter) {
		this.outlinePresenter = outlinePresenter;
	}
	
	{
		addConfigurationOwned(new IDisposable() {
			@Override
			public void dispose() {
				if(outlinePresenter != null) {
					outlinePresenter.uninstall();
					outlinePresenter = null;
				}
			}
		});		
	}
	
	@Override
	public void showOutline() throws CoreException {
		if(outlinePresenter != null) {
			outlinePresenter.showInformation();
		} else {
			throw LangCore.createCoreException("Outline not available. ", null);
		}
	}
	
	
	/* ----------------- Content Assist ----------------- */
	
	// Override to grant public access to field.
	@Override
	public IContentAssistant getContentAssistant() {
		return fContentAssistant;
	}
	
	@Override
	public void handlePropertyChangeEvent_2(PropertyChangeEvent event, IPreferenceStore prefStore) {
		sourceViewerConfig.reconfigureForPropertyChange(event, prefStore, this);
		
		if(getContentAssistant() instanceof ContentAssistantExt) {
			ContentAssistantExt caExt = (ContentAssistantExt) getContentAssistant();
			caExt.handlePrefChange(event, prefStore);
		}
	}
	
}