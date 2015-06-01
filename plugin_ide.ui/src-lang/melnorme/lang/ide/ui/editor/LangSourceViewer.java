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


import melnorme.lang.ide.core.LangCore;
import melnorme.utilbox.ownership.IDisposable;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.information.IInformationPresenter;
import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.swt.widgets.Composite;

public class LangSourceViewer extends ProjectionViewerExt implements ISourceViewerExt {
	
	public LangSourceViewer(Composite parent, IVerticalRuler verticalRuler, IOverviewRuler overviewRuler,
			boolean showAnnotationsOverview, int styles) {
		super(parent, verticalRuler, overviewRuler, showAnnotationsOverview, styles);
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
	
}