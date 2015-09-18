/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.text;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants;

import _org.eclipse.cdt.internal.ui.text.TokenStore;
import _org.eclipse.cdt.ui.text.IColorManager;
import _org.eclipse.cdt.ui.text.ITokenStore;
import _org.eclipse.cdt.ui.text.ITokenStoreFactory;
import melnorme.lang.ide.core.TextSettings_Actual;
import melnorme.lang.ide.ui.CodeFormatterConstants;
import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.editor.ProjectionViewerExt;
import melnorme.lang.ide.ui.editor.ViewerColorUpdater;
import melnorme.lang.ide.ui.text.coloring.SingleTokenScanner;

/**
 * Abstract SourceViewConfiguration
 * Has code to help manage the configured scanners, and let respond to preference changes.
 */
public abstract class SimpleLangSourceViewerConfiguration extends TextSourceViewerConfiguration {
	
	protected final IColorManager colorManager;
	protected final IPreferenceStore preferenceStore;
	protected Map<String, AbstractLangScanner> scannersByContentType;
	
	
	public SimpleLangSourceViewerConfiguration(IPreferenceStore preferenceStore, IColorManager colorManager) {
		super(assertNotNull(preferenceStore));
		this.colorManager = colorManager;
		this.preferenceStore = preferenceStore;
		
		scannersByContentType = new HashMap<>();
		createScanners();
		assertTrue(scannersByContentType.size() == getConfiguredContentTypes(null).length);
		scannersByContentType = Collections.unmodifiableMap(scannersByContentType);
	}
	
	protected IColorManager getColorManager2() {
		return colorManager;
	}
	
	public IPreferenceStore getPreferenceStore() {
		return preferenceStore;
	}
	
	public void handlePropertyChange(PropertyChangeEvent event, IPreferenceStore prefStore,
			SourceViewer sourceViewer) {
		handleTextPresentationPropertyChangeEvent(event);
		
		assertTrue(prefStore == getPreferenceStore());
		String property = event.getProperty();
		updateIndentationSettings(sourceViewer, property);
	}
	
	/* ----------------- scanners / partitioning ----------------- */
	
	@Override
	public String getConfiguredDocumentPartitioning(ISourceViewer sourceViewer) {
		return TextSettings_Actual.PARTITIONING_ID;
	}
	
	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return TextSettings_Actual.PARTITION_TYPES;
	}
	
	protected abstract void createScanners();
	
	protected void addScanner(AbstractLangScanner scanner, String... contentTypes) {
		assertNotNull(scanner);
		for (String contentType : contentTypes) {
			scannersByContentType.put(contentType, scanner);
		}
	}
	
	protected SingleTokenScanner createSingleTokenScanner(String tokenProperty) {
		return new SingleTokenScanner(getTokenStoreFactory(), tokenProperty);
	}
	
	protected ITokenStoreFactory getTokenStoreFactory() {
		return new ITokenStoreFactory() {
			@Override
			public ITokenStore createTokenStore(String[] propertyColorNames) {
				return new TokenStore(getColorManager2(), getPreferenceStore(), propertyColorNames);
			}
		};
	}
	
	public Collection<AbstractLangScanner> getScanners() {
		return scannersByContentType.values();
	}
	
	
	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = createPresentationReconciler();
		reconciler.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));
		setupPresentationReconciler(reconciler);
		return reconciler;
	}
	
	protected PresentationReconciler createPresentationReconciler() {
		return new PresentationReconciler();
	}
	
	protected void setupPresentationReconciler(PresentationReconciler reconciler) {
		for (Entry<String, AbstractLangScanner> entry : scannersByContentType.entrySet()) {
			String contentType = entry.getKey();
			AbstractLangScanner scanner = entry.getValue();
			DefaultDamagerRepairer dr = new DefaultDamagerRepairer(scanner);
			reconciler.setDamager(dr, contentType);
			reconciler.setRepairer(dr, contentType);
		}
	}
	
	public boolean affectsTextPresentation(PropertyChangeEvent event) {
		for (AbstractLangScanner scanner : getScanners()) {
			if(scanner.affectsBehavior(event))
				return true;
		}
		return false;
	}
	
	public void handleTextPresentationPropertyChangeEvent(PropertyChangeEvent event) {
		for (AbstractLangScanner scanner : getScanners()) {
			if (scanner.affectsBehavior(event)) {
				scanner.adaptToPreferenceChange(event);
			}
		}
	}
	
	public void setupViewerForTextPresentationPrefChanges(final SourceViewer viewer) {
		final IPropertyChangeListener propertyChangeListener = new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if(affectsTextPresentation(event)) {
					handleTextPresentationPropertyChangeEvent(event);
					viewer.invalidateTextPresentation();
				}
			}
		};
		viewer.getTextWidget().addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				preferenceStore.removePropertyChangeListener(propertyChangeListener);
			}
		});
		
		preferenceStore.addPropertyChangeListener(propertyChangeListener);
	}
	
	public String getFontPropertyPreferenceKey() {
		return JFaceResources.TEXT_FONT;
	}
	
	/* ----------------- TextViewer ----------------- */
	
	@Override
	public int getTabWidth(ISourceViewer sourceViewer) {
		if (fPreferenceStore == null)
			return super.getTabWidth(sourceViewer);
		return fPreferenceStore.getInt(CodeFormatterConstants.FORMATTER_TAB_SIZE.key);
	}
	
	protected void updateIndentationSettings(SourceViewer sourceViewer, String property) {
		if(CodeFormatterConstants.FORMATTER_TAB_SIZE.keyEquals(property)) {
			StyledText textWidget = sourceViewer.getTextWidget();
			int tabWidth = getTabWidth(sourceViewer);
			textWidget.setTabs(tabWidth);
		}
	}
	
	/* ----------------- Presentation UI controls ----------------- */
	
	@Override
	public IInformationControlCreator getInformationControlCreator(ISourceViewer sourceViewer) {
		return new IInformationControlCreator() {
			@Override
			public IInformationControl createInformationControl(Shell parent) {
				return new DefaultInformationControl(parent, false);
			}
		};
	}
	
	protected IInformationControlCreator getInformationControl_ContentAsssist(final String statusFieldText) {
		return new IInformationControlCreator() {
			@Override
			public IInformationControl createInformationControl(Shell parent) {
				return new DefaultInformationControl(parent, statusFieldText);
			}
		};
	}
	
	/* -----------------  ----------------- */
	
	public static final String getAdditionalInfoAffordanceString() {
		IPreferenceStore store = EditorsUI.getPreferenceStore();
		if(!store.getBoolean(AbstractDecoratedTextEditorPreferenceConstants.EDITOR_SHOW_TEXT_HOVER_AFFORDANCE)) {
			return null;
		}
		
		return LangUIMessages.SourceHover_additionalInfo_affordance;
	}
	
	public static ContentAssistant setupSimpleContentAssistant(IContentAssistProcessor cap, String[] contentTypes) {
		ContentAssistant ca = new ContentAssistant();
		ca.enableAutoActivation(true);
		ca.enableAutoInsert(true);
		
		for (String partitionType : contentTypes) {
			ca.setContentAssistProcessor(cap, partitionType);
		}
		
		return ca;
	}
	
	/* -----------------  ----------------- */
	
	public void configureViewer(ProjectionViewerExt sourceViewer) {
		StyledText textWidget = sourceViewer.getTextWidget();
		if (textWidget != null) {
			textWidget.setFont(JFaceResources.getFont(getFontPropertyPreferenceKey()));
			// TODO: respond to font changes
		}
		
		new ViewerColorUpdater(fPreferenceStore, sourceViewer).configureViewer();
	}
	
}