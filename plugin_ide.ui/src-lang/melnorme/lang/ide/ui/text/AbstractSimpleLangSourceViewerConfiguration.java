/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
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
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants;

import melnorme.lang.ide.core.TextSettings_Actual;
import melnorme.lang.ide.core.TextSettings_Actual.LangPartitionTypes;
import melnorme.lang.ide.ui.CodeFormatterConstants;
import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.editor.ProjectionViewerExt;
import melnorme.lang.ide.ui.editor.ViewerColorUpdater;
import melnorme.lang.ide.ui.text.coloring.StylingPreferences;
import melnorme.lang.ide.ui.text.coloring.TokenRegistry;
import melnorme.util.swt.jface.text.ColorManager2;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.misc.ArrayUtil;
import melnorme.utilbox.ownership.IDisposable;

/**
 * Abstract SourceViewConfiguration
 * Has code to help manage the configured scanners, and let respond to preference changes.
 */
public abstract class AbstractSimpleLangSourceViewerConfiguration extends TextSourceViewerConfiguration {
	
	protected final IPreferenceStore preferenceStore;
	protected final ColorManager2 colorManager;
	protected final StylingPreferences stylingPrefs;
	
	public AbstractSimpleLangSourceViewerConfiguration(IPreferenceStore preferenceStore, 
			ColorManager2 colorManager, StylingPreferences stylingPrefs) {
		super(assertNotNull(preferenceStore));
		this.preferenceStore = preferenceStore;
		this.colorManager = assertNotNull(colorManager);
		this.stylingPrefs = assertNotNull(stylingPrefs);
	}
	
	protected ColorManager2 getColorManager() {
		return colorManager;
	}
	
	public IPreferenceStore getPreferenceStore() {
		return preferenceStore;
	}
	
	public void handlePropertyChange(PropertyChangeEvent event, IPreferenceStore prefStore,
			SourceViewer sourceViewer) {
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
		return ArrayUtil.map(getPartitionTypes(), obj -> obj.getId(), String.class);
	}
	
	public LangPartitionTypes[] getPartitionTypes() {
		return TextSettings_Actual.LangPartitionTypes.values();
	}
	
	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = createPresentationReconciler();
		reconciler.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));
		setupPresentationReconciler(reconciler, sourceViewer);
		return reconciler;
	}
	
	protected PresentationReconciler createPresentationReconciler() {
		return new PresentationReconciler();
	}
	
	protected void setupPresentationReconciler(PresentationReconciler reconciler, ISourceViewer sourceViewer) {
		// Must be called from UI thread
		assertTrue(Display.getCurrent() != null);
		
		// Create a token registry for given sourceViewer
		TokenRegistry tokenRegistry = new TokenRegistry(colorManager, stylingPrefs) {
			@Override
			protected void handleTokenModified(Token token) {
				sourceViewer.invalidateTextPresentation();
			}
		};
		addConfigurationScopedOwned(sourceViewer, tokenRegistry);
		
		ArrayList2<AbstractLangScanner> scanners = new ArrayList2<>();
		
		for(LangPartitionTypes partitionType : getPartitionTypes()) {
			
			String contentType = partitionType.getId();
			AbstractLangScanner scanner = createScannerFor(Display.getCurrent(), partitionType, tokenRegistry);
			scanners.add(scanner);
			
			DefaultDamagerRepairer dr = getDamagerRepairer(scanner, contentType);
			reconciler.setDamager(dr, contentType);
			reconciler.setRepairer(dr, contentType);
		}
		
	}
	
	@SuppressWarnings("unused")
	protected DefaultDamagerRepairer getDamagerRepairer(AbstractLangScanner scanner, String contentType) {
		return new DefaultDamagerRepairer(scanner);
	}
	
	protected void addConfigurationScopedOwned(ISourceViewer sourceViewer, IDisposable tokenStore) {
		if(sourceViewer instanceof ProjectionViewerExt) {
			ProjectionViewerExt viewerExt = (ProjectionViewerExt) sourceViewer;
			viewerExt.addConfigurationOwned(tokenStore);
		} else {
			sourceViewer.getTextWidget().addDisposeListener(e -> tokenStore.dispose());
		}
	}
	
	protected abstract AbstractLangScanner createScannerFor(Display current, LangPartitionTypes partitionType, 
			TokenRegistry tokenStore);
	
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
		if(CodeFormatterConstants.FORMATTER_TAB_SIZE.key.equals(property)) {
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