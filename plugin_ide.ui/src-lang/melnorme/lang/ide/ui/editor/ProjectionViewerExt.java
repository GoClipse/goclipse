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


import melnorme.lang.ide.ui.text.completion.ContentAssistantExt;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Composite;

public class ProjectionViewerExt extends ProjectionViewer implements ISourceViewerExt {
	
	public ProjectionViewerExt(Composite parent, IVerticalRuler ruler, IOverviewRuler overviewRuler,
			boolean showsAnnotationOverview, int styles) {
		super(parent, ruler, overviewRuler, showsAnnotationOverview, styles);
	}
	
	// public access to field.
	@Override
	public IContentAssistant getContentAssistant() {
		return fContentAssistant;
	}
	
	@Override
	public void handlePropertyChangeEvent_2(PropertyChangeEvent event, IPreferenceStore prefStore) {
		if(getContentAssistant() instanceof ContentAssistantExt) {
			ContentAssistantExt caExt = (ContentAssistantExt) getContentAssistant();
			caExt.handlePrefChange(event, prefStore);
		}
	}
	
}