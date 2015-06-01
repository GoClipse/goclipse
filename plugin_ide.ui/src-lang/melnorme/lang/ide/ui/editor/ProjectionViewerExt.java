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


import melnorme.lang.ide.ui.text.SimpleLangSourceViewerConfiguration;
import melnorme.utilbox.ownership.IDisposable;
import melnorme.utilbox.ownership.OwnedArraylist;

import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.swt.widgets.Composite;

public class ProjectionViewerExt extends ProjectionViewer {
	
	protected final OwnedArraylist owned = new OwnedArraylist();
	protected final OwnedArraylist configurationDisposables = new OwnedArraylist();
	
	protected boolean isConfigured;
	
	public ProjectionViewerExt(Composite parent, IVerticalRuler ruler, IOverviewRuler overviewRuler,
			boolean showsAnnotationOverview, int styles) {
		super(parent, ruler, overviewRuler, showsAnnotationOverview, styles);
	}
	
	public <T extends IDisposable> T addConfigurationOwned(T ownedObject) {
		configurationDisposables.add(ownedObject);
		return ownedObject;
	}
	
	public <T extends IDisposable> T addOwned(T ownedObject) {
		owned.add(ownedObject);
		return ownedObject;
	}
	
	@Override
	public final void configure(SourceViewerConfiguration configuration) {
		doConfigure(configuration);
		
		isConfigured = true;
	}
	
	public void doConfigure(SourceViewerConfiguration configuration) {
		super.configure(configuration);
		
		if(configuration instanceof SimpleLangSourceViewerConfiguration) {
			SimpleLangSourceViewerConfiguration svc = (SimpleLangSourceViewerConfiguration) configuration;
			svc.configureViewer(this);
		}
	}
	
	@Override
	public void unconfigure() {
		configurationDisposables.disposeAll();
		
		super.unconfigure();
		
		isConfigured = false;
	}
	
	@Override
	protected void handleDispose() {
		owned.disposeAll();
		
		super.handleDispose();
	}
	
	/* -----------------  ----------------- */
	
	@Override
	protected int getEmptySelectionChangedEventDelay() {
		// Change selection delay when moving caret. 
		// This could have some unintendend consequences, but seems to be working well so far.
		return 0; 
	}
	
}