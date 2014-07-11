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
import melnorme.lang.ide.ui.text.coloring.SingleTokenScanner;

import org.eclipse.cdt.internal.ui.text.TokenStore;
import org.eclipse.cdt.ui.text.IColorManager;
import org.eclipse.cdt.ui.text.ITokenStore;
import org.eclipse.cdt.ui.text.ITokenStoreFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

public abstract class AbstractLangSourceViewerConfiguration extends TextSourceViewerConfiguration {
	
	protected final IColorManager fColorManager;
	protected final IPreferenceStore preferenceStore;
	
	public AbstractLangSourceViewerConfiguration(IPreferenceStore preferenceStore, IColorManager colorManager) {
		super(assertNotNull(preferenceStore));
		this.preferenceStore = preferenceStore;
		this.fColorManager = colorManager;
	}
	
	protected org.eclipse.cdt.ui.text.IColorManager getColorManager2() {
		return fColorManager;
	}
	
	public IPreferenceStore getPreferenceStore() {
		return preferenceStore;
	}
	
	protected SingleTokenScanner createSingleTokenScanner(String tokenProperty) {
		return new SingleTokenScanner(getTokenStoreFactory(), tokenProperty);
	}
	
	protected ITokenStoreFactory getTokenStoreFactory() {
		return new ITokenStoreFactory() {
			@Override
			public ITokenStore createTokenStore(String[] propertyColorNames) {
				return new TokenStore(getColorManager2(), fPreferenceStore, propertyColorNames);
			}
		};
	}
	
	@SuppressWarnings("unused")
	public boolean affectsTextPresentation(PropertyChangeEvent event) {
		return false;
	}
	
	@SuppressWarnings("unused")
	public void handlePropertyChangeEvent(PropertyChangeEvent event) {
		
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
	
}