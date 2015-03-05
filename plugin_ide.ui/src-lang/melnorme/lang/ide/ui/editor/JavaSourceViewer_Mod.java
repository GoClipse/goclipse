/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.editor;

import java.util.ArrayList;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextPresentationListener;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants;
import org.eclipse.ui.texteditor.AbstractTextEditor;


/**
 * Partial clone of org.eclipse.jdt.internal.ui.javaeditor.JavaSourceViewer
 * 
 * Contains methods/code that seems useful or necessary, but whose operation or usage is not fully understood,
 * or simply hasn't been refactored yet.
 */
public abstract class JavaSourceViewer_Mod extends ProjectionViewer implements IPropertyChangeListener {

	/**
	 * Is this source viewer configured?
	 *
	 * @since 3.0
	 */
	private boolean fIsConfigured;
	
	public JavaSourceViewer_Mod(Composite parent, IVerticalRuler verticalRuler, IOverviewRuler overviewRuler, boolean showAnnotationsOverview, int styles, IPreferenceStore store) {
		super(parent, verticalRuler, overviewRuler, showAnnotationsOverview, styles);
		setPreferenceStore(store);
	}
	
	/**
	 * Sets the preference store on this viewer.
	 *
	 * @param store the preference store
	 *
	 * @since 3.0
	 */
	public void setPreferenceStore(IPreferenceStore store) {
		if (fIsConfigured && fPreferenceStore != null)
			fPreferenceStore.removePropertyChangeListener(this);

		fPreferenceStore= store;

		if (fIsConfigured && fPreferenceStore != null) {
			fPreferenceStore.addPropertyChangeListener(this);
			initializeViewerColors();
		}
	}

	/* ----------------- handling color preference changes ----------------- */
	// This is normally done by AbstractTextEditor, but we want this functionality even when
	// the SourceView is not attached to any editor
	
	/**
	 * This viewer's foreground color.
	 * @since 3.0
	 */
	private Color fForegroundColor;
	/**
	 * The viewer's background color.
	 * @since 3.0
	 */
	private Color fBackgroundColor;
	/**
	 * This viewer's selection foreground color.
	 * @since 3.0
	 */
	private Color fSelectionForegroundColor;
	/**
	 * The viewer's selection background color.
	 * @since 3.0
	 */
	private Color fSelectionBackgroundColor;
	/**
	 * The preference store.
	 *
	 * @since 3.0
	 */
	protected IPreferenceStore fPreferenceStore;

	
	@Override
	public void configure(SourceViewerConfiguration configuration) {

		/*
		 * Prevent access to colors disposed in unconfigure(), see:
		 *   https://bugs.eclipse.org/bugs/show_bug.cgi?id=53641
		 *   https://bugs.eclipse.org/bugs/show_bug.cgi?id=86177
		 */
		StyledText textWidget= getTextWidget();
		if (textWidget != null && !textWidget.isDisposed()) {
			Color foregroundColor= textWidget.getForeground();
			if (foregroundColor != null && foregroundColor.isDisposed())
				textWidget.setForeground(null);
			Color backgroundColor= textWidget.getBackground();
			if (backgroundColor != null && backgroundColor.isDisposed())
				textWidget.setBackground(null);
		}

		super.configure(configuration);
//		if (configuration instanceof JavaSourceViewerConfiguration) {
//			JavaSourceViewerConfiguration javaSVCconfiguration= (JavaSourceViewerConfiguration)configuration;
//			fOutlinePresenter= javaSVCconfiguration.getOutlinePresenter(this, false);
//			if (fOutlinePresenter != null)
//				fOutlinePresenter.install(this);
//
//			fStructurePresenter= javaSVCconfiguration.getOutlinePresenter(this, true);
//			if (fStructurePresenter != null)
//				fStructurePresenter.install(this);
//
//			fHierarchyPresenter= javaSVCconfiguration.getHierarchyPresenter(this, true);
//			if (fHierarchyPresenter != null)
//				fHierarchyPresenter.install(this);
//
//		}
		
		configure_beforeViewerColors(configuration);

		if (fPreferenceStore != null) {
			fPreferenceStore.addPropertyChangeListener(this);
			initializeViewerColors();
		}

		fIsConfigured= true;
	}
	
	@SuppressWarnings("unused")
	protected void configure_beforeViewerColors(SourceViewerConfiguration configuration) {
	}
	
	protected void initializeViewerColors() {
		if (fPreferenceStore != null) {

			StyledText styledText= getTextWidget();

			// ----------- foreground color --------------------
			Color color= fPreferenceStore.getBoolean(AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND_SYSTEM_DEFAULT)
			? null
			: createColor(fPreferenceStore, AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND, styledText.getDisplay());
			styledText.setForeground(color);

			if (fForegroundColor != null)
				fForegroundColor.dispose();

			fForegroundColor= color;

			// ---------- background color ----------------------
			color= fPreferenceStore.getBoolean(AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND_SYSTEM_DEFAULT)
			? null
			: createColor(fPreferenceStore, AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND, styledText.getDisplay());
			styledText.setBackground(color);

			if (fBackgroundColor != null)
				fBackgroundColor.dispose();

			fBackgroundColor= color;

			// ----------- selection foreground color --------------------
			color= fPreferenceStore.getBoolean(AbstractDecoratedTextEditorPreferenceConstants.EDITOR_SELECTION_FOREGROUND_DEFAULT_COLOR)
				? null
				: createColor(fPreferenceStore, AbstractDecoratedTextEditorPreferenceConstants.EDITOR_SELECTION_FOREGROUND_COLOR, styledText.getDisplay());
			styledText.setSelectionForeground(color);

			if (fSelectionForegroundColor != null)
				fSelectionForegroundColor.dispose();

			fSelectionForegroundColor= color;

			// ---------- selection background color ----------------------
			color= fPreferenceStore.getBoolean(AbstractDecoratedTextEditorPreferenceConstants.EDITOR_SELECTION_BACKGROUND_DEFAULT_COLOR)
				? null
				: createColor(fPreferenceStore, AbstractDecoratedTextEditorPreferenceConstants.EDITOR_SELECTION_BACKGROUND_COLOR, styledText.getDisplay());
			styledText.setSelectionBackground(color);

			if (fSelectionBackgroundColor != null)
				fSelectionBackgroundColor.dispose();

			fSelectionBackgroundColor= color;
		}
	}


    /**
     * Creates a color from the information stored in the given preference store.
     * Returns <code>null</code> if there is no such information available.
     *
     * @param store the store to read from
     * @param key the key used for the lookup in the preference store
     * @param display the display used create the color
     * @return the created color according to the specification in the preference store
     * @since 3.0
     */
    protected Color createColor(IPreferenceStore store, String key, Display display) {

        RGB rgb= null;

        if (store.contains(key)) {

            if (store.isDefault(key))
                rgb= PreferenceConverter.getDefaultColor(store, key);
            else
                rgb= PreferenceConverter.getColor(store, key);

            if (rgb != null)
                return new Color(display, rgb);
        }

        return null;
    }

	@Override
	public void unconfigure() {
//		if (fOutlinePresenter != null) {
//			fOutlinePresenter.uninstall();
//			fOutlinePresenter= null;
//		}
//		if (fStructurePresenter != null) {
//			fStructurePresenter.uninstall();
//			fStructurePresenter= null;
//		}
//		if (fHierarchyPresenter != null) {
//			fHierarchyPresenter.uninstall();
//			fHierarchyPresenter= null;
//		}
		if (fForegroundColor != null) {
			fForegroundColor.dispose();
			fForegroundColor= null;
		}
		if (fBackgroundColor != null) {
			fBackgroundColor.dispose();
			fBackgroundColor= null;
		}

		if (fPreferenceStore != null)
			fPreferenceStore.removePropertyChangeListener(this);

		super.unconfigure();

		fIsConfigured= false;
	}

	@Override
	public Point rememberSelection() {
		return super.rememberSelection();
	}

	@Override
	public void restoreSelection() {
		super.restoreSelection();
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		String property = event.getProperty();
		if (AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND.equals(property)
				|| AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND_SYSTEM_DEFAULT.equals(property)
				|| AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND.equals(property)
				|| AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND_SYSTEM_DEFAULT.equals(property)
				|| AbstractDecoratedTextEditorPreferenceConstants.EDITOR_SELECTION_FOREGROUND_COLOR.equals(property)
				|| AbstractDecoratedTextEditorPreferenceConstants.EDITOR_SELECTION_FOREGROUND_DEFAULT_COLOR.equals(property)
				|| AbstractDecoratedTextEditorPreferenceConstants.EDITOR_SELECTION_BACKGROUND_COLOR.equals(property)
				|| AbstractDecoratedTextEditorPreferenceConstants.EDITOR_SELECTION_BACKGROUND_DEFAULT_COLOR.equals(property))
		{
			initializeViewerColors();
		}
	}

	/* ----------------- setVisibleDocument optimization ----------------- */
	
	/**
	 * Whether to delay setting the visual document until the projection has been computed.
	 * <p>
	 * Added for performance optimization.
	 * </p>
	 * @see #prepareDelayedProjection()
	 * @since 3.1
	 */
	private boolean fIsSetVisibleDocumentDelayed= false;

	/**
	 * Delays setting the visual document until after the projection has been computed.
	 * This method must only be called before the document is set on the viewer.
	 * <p>
	 * This is a performance optimization to reduce the computation of
	 * the text presentation triggered by <code>setVisibleDocument(IDocument)</code>.
	 * </p>
	 *
	 * @see #setVisibleDocument(IDocument)
	 * @since 3.1
	 */
	public void prepareDelayedProjection() {
		Assert.isTrue(!fIsSetVisibleDocumentDelayed);
		fIsSetVisibleDocumentDelayed= true;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This is a performance optimization to reduce the computation of
	 * the text presentation triggered by {@link #setVisibleDocument(IDocument)}
	 * </p>
	 * @see #prepareDelayedProjection()
	 * @since 3.1
	 */
	@Override
	protected void setVisibleDocument(IDocument document) {
		if (fIsSetVisibleDocumentDelayed) {
			fIsSetVisibleDocumentDelayed= false;
			IDocument previous= getVisibleDocument();
			enableProjection(); // will set the visible document if anything is folded
			IDocument current= getVisibleDocument();
			// if the visible document was not replaced, continue as usual
			if (current != null && current != previous)
				return;
		}

		super.setVisibleDocument(document);
	}
	
	/* ----------------- helpers / optimization ----------------- */
	
	/**
	 * Prepends the text presentation listener at the beginning of the viewer's
	 * list of text presentation listeners.  If the listener is already registered
	 * with the viewer this call moves the listener to the beginning of
	 * the list.
	 *
	 * @param listener the text presentation listener
	 * @since 3.0
	 */
	public void prependTextPresentationListener(ITextPresentationListener listener) {

		Assert.isNotNull(listener);

		if (fTextPresentationListeners == null)
			fTextPresentationListeners= new ArrayList<ITextPresentationListener>();

		fTextPresentationListeners.remove(listener);
		fTextPresentationListeners.add(0, listener);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Performance optimization: since we know at this place
	 * that none of the clients expects the given range to be
	 * untouched we reuse the given range as return value.
	 * </p>
	 */
	@Override
	protected StyleRange modelStyleRange2WidgetStyleRange(StyleRange range) {
		IRegion region= modelRange2WidgetRange(new Region(range.start, range.length));
		if (region != null) {
			// don't clone the style range, but simply reuse it.
			range.start= region.getOffset();
			range.length= region.getLength();
			return range;
		}
		return null;
	}

}