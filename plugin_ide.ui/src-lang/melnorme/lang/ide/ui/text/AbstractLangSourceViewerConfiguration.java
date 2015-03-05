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
import static melnorme.utilbox.core.CoreUtil.array;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import melnorme.lang.ide.ui.EditorSettings_Actual;
import melnorme.lang.ide.ui.LangUIPlugin_Actual;
import melnorme.lang.ide.ui.TextSettings_Actual;
import melnorme.lang.ide.ui.text.coloring.AbstractLangScanner;
import melnorme.lang.ide.ui.text.coloring.SingleTokenScanner;

import org.eclipse.cdt.internal.ui.text.TokenStore;
import org.eclipse.cdt.ui.text.IColorManager;
import org.eclipse.cdt.ui.text.ITokenStore;
import org.eclipse.cdt.ui.text.ITokenStoreFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * Abstract SourceViewConfiguration
 * Has code to help manage the configured scanners, and let respond to preference changes.
 */
public abstract class AbstractLangSourceViewerConfiguration extends TextSourceViewerConfiguration {
	
	protected final IColorManager colorManager;
	protected final IPreferenceStore preferenceStore;
	protected final AbstractDecoratedTextEditor editor;
	protected Map<String, AbstractLangScanner> scannersByContentType;
	
	
	public AbstractLangSourceViewerConfiguration(IPreferenceStore preferenceStore, IColorManager colorManager, 
			AbstractDecoratedTextEditor editor) {
		super(assertNotNull(preferenceStore));
		this.colorManager = colorManager;
		this.preferenceStore = preferenceStore;
		this.editor = editor;
		
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
	
	public AbstractDecoratedTextEditor getEditor() {
		return editor;
	}
	
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
	
	public void handlePropertyChangeEvent(PropertyChangeEvent event) {
		for (AbstractLangScanner scanner : getScanners()) {
			if (scanner.affectsBehavior(event)) {
				scanner.adaptToPreferenceChange(event);
			}
		}
	}
	
	public void setupViewerForTextPresentationPrefChanges(SourceViewer viewer) {
		setupViewerForTextPresentationPrefChanges(viewer, this, getPreferenceStore());
	}
	
	public void setupViewerForTextPresentationPrefChanges(final SourceViewer viewer, 
			final AbstractLangSourceViewerConfiguration configuration, final IPreferenceStore preferenceStore) {
		final IPropertyChangeListener propertyChangeListener = new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if (configuration.affectsTextPresentation(event)) {
					configuration.handlePropertyChangeEvent(event);
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
	
	/* ----------------- Navigation operations ----------------- */
	
	@Override 
	protected Map<String, ITextEditor> getHyperlinkDetectorTargets(ISourceViewer sourceViewer) {
		Map<String, ITextEditor> targets = super.getHyperlinkDetectorTargets(sourceViewer);
		targets.put(EditorSettings_Actual.EDITOR_CODE_TARGET, editor); 
		return targets;
	}
	
	/* ----------------- Modification operations ----------------- */
	
	@Override
	public IAutoEditStrategy[] getAutoEditStrategies(ISourceViewer sourceViewer, String contentType) {
		if(IDocument.DEFAULT_CONTENT_TYPE.equals(contentType)) {
			return array(LangUIPlugin_Actual.createAutoEditStrategy(sourceViewer, contentType));
		} else {
			return super.getAutoEditStrategies(sourceViewer, contentType);
		}
	}
	
}