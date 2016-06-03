/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.editor.hover;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.information.IInformationProvider;
import org.eclipse.jface.text.information.IInformationProviderExtension;
import org.eclipse.jface.text.information.IInformationProviderExtension2;


public class HoverInformationProvider 
	implements IInformationProvider, IInformationProviderExtension, IInformationProviderExtension2 {
	
	protected final AbstractEditorTextHover editorHover;
	
	public HoverInformationProvider(AbstractEditorTextHover editorHover) {
		this.editorHover = assertNotNull(editorHover);
	}

	@Override
	public IRegion getSubject(ITextViewer textViewer, int offset) {
		return editorHover.getHoverRegion(textViewer, offset);
	}
	
	@Deprecated
	@Override
	public String getInformation(ITextViewer textViewer, IRegion subject) {
		return editorHover.getHoverInfo(textViewer, subject);
	}
	
	@Override
	public Object getInformation2(ITextViewer textViewer, IRegion subject) {
		return editorHover.getHoverInfo2(textViewer, subject);
	}
	
	@Override
	public IInformationControlCreator getInformationPresenterControlCreator() {
		if(editorHover instanceof IInformationProviderExtension2) {
			IInformationProviderExtension2 infProviderControlCreator = (IInformationProviderExtension2) editorHover;
			return infProviderControlCreator.getInformationPresenterControlCreator();
		}
		return editorHover.getHoverControlCreator();
	}
	
}