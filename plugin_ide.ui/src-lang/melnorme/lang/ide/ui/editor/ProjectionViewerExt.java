/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.editor;


import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.widgets.Composite;

import melnorme.lang.ide.ui.text.AbstractSimpleLangSourceViewerConfiguration;
import melnorme.utilbox.ownership.IDisposable;
import melnorme.utilbox.ownership.IOwner;
import melnorme.utilbox.ownership.OwnedObjects;

public class ProjectionViewerExt extends ProjectionViewer {
	
	protected final OwnedObjects owned = new OwnedObjects();
	protected OwnedObjects configurationOwned = new OwnedObjects();
	
	protected boolean isConfigured;
	
	public ProjectionViewerExt(Composite parent, IVerticalRuler ruler, IOverviewRuler overviewRuler,
			boolean showsAnnotationOverview, int styles) {
		super(parent, ruler, overviewRuler, showsAnnotationOverview, styles);
	}
	
	public <T extends IDisposable> T addConfigurationOwned(T ownedObject) {
		configurationOwned.add(ownedObject);
		return ownedObject;
	}
	
	public <T extends IDisposable> T addOwned(T ownedObject) {
		owned.add(ownedObject);
		return ownedObject;
	}
	
	public IOwner getConfigurationOwned() {
		return configurationOwned;
	}
	
	@Override
	public final void configure(SourceViewerConfiguration configuration) {
		doConfigure(configuration);
		
		isConfigured = true;
	}
	
	public void doConfigure(SourceViewerConfiguration configuration) {
		super.configure(configuration);
		
		if(configuration instanceof AbstractSimpleLangSourceViewerConfiguration) {
			AbstractSimpleLangSourceViewerConfiguration svc = (AbstractSimpleLangSourceViewerConfiguration) configuration;
			svc.configureViewer(this);
		}
	}
	
	@Override
	public void unconfigure() {
		configurationOwned.disposeAll();
		configurationOwned = new OwnedObjects(); 
		
		super.unconfigure();
		
		isConfigured = false;
	}
	
	@Override
	protected void handleDispose() {
		owned.disposeAll();
		// Note: super.handleDispose will call unconfigure
		super.handleDispose();
	}
	
	/* -----------------  ----------------- */
	
	/**
	 * Add given {@link IPropertyChangeListener} to given store.
	 * The listener will be automatically removed if viewer is unconfigured or disposed.
	 */
	public void addConfigurationScoped_PrefStoreListener(IPreferenceStore prefStore, IPropertyChangeListener propertyChangeListener) {
		prefStore.addPropertyChangeListener(propertyChangeListener);
		
		addConfigurationOwned(new IDisposable() {
			@Override
			public void dispose() {
				prefStore.removePropertyChangeListener(propertyChangeListener);
			}
		});
	}
	
	/* -----------------  ----------------- */
	
	@Override
	protected int getEmptySelectionChangedEventDelay() {
		// Change selection delay when moving caret. 
		// This could have some unintendend consequences, but seems to be working well so far.
		return 0; 
	}
	
}