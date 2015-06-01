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


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import melnorme.util.swt.SWTUtil;
import melnorme.utilbox.ownership.IDisposable;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.custom.StyledText;

/**
 * 
 * Base class for a helper that configures SourceViewer aspects from a preference store.
 * 
 */
public abstract class SourceViewerConfigurer implements IPropertyChangeListener {
	
	protected final IPreferenceStore store;
	protected final ProjectionViewerExt sourceViewer;
	protected StyledText styledText;
	
	public SourceViewerConfigurer(IPreferenceStore prefStore, ProjectionViewerExt sourceViewer) {
		this.store = assertNotNull(prefStore);
		this.sourceViewer = assertNotNull(sourceViewer);
	}
	
	public void configureViewer() {
		this.styledText = sourceViewer.getTextWidget();
		assertTrue(SWTUtil.isOkToUse(styledText));
		
		sourceViewer.addConfigurationOwned(new IDisposable() {
			@Override
			public void dispose() {
				unconfigureViewer();
			}
		});
		
		store.addPropertyChangeListener(this);
		
		doConfigureViewer();
	}
	
	protected abstract void doConfigureViewer();
	
	public final void unconfigureViewer() {
		store.removePropertyChangeListener(this);
		
		doUnconfigureViewer();
	}
	
	protected abstract void doUnconfigureViewer();
	
	@Override
	public final void propertyChange(PropertyChangeEvent event) {
		assertTrue(SWTUtil.isOkToUse(styledText));
		
		handlePropertyChange(event);
	}
	
	protected abstract void handlePropertyChange(PropertyChangeEvent event);
	
}