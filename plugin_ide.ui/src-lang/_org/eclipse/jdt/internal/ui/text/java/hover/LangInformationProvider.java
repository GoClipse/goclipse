/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package _org.eclipse.jdt.internal.ui.text.java.hover;

import melnorme.lang.ide.ui.editor.BestMatchHover;
import melnorme.lang.ide.ui.text.util.WordFinder;

import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.information.IInformationProvider;
import org.eclipse.jface.text.information.IInformationProviderExtension;
import org.eclipse.jface.text.information.IInformationProviderExtension2;
import org.eclipse.ui.IEditorPart;


public class LangInformationProvider implements IInformationProvider, IInformationProviderExtension,
		IInformationProviderExtension2 {
	
	protected BestMatchHover fImplementation;
	
	public LangInformationProvider(IEditorPart editor) {
		if (editor != null) {
			fImplementation= new BestMatchHover(editor);
			fImplementation.setEditor(editor);
		}
	}
	
	@Override
	public IRegion getSubject(ITextViewer textViewer, int offset) {
		
		if (textViewer != null)
			return WordFinder.findWord(textViewer.getDocument(), offset);
		
		return null;
	}
	
	/**
	 * @see IInformationProvider#getInformation(ITextViewer, IRegion)
	 * @deprecated
	 */
	@Override
	public String getInformation(ITextViewer textViewer, IRegion subject) {
		if (fImplementation != null) {
			String s= fImplementation.getHoverInfo(textViewer, subject);
			if (s != null && s.trim().length() > 0) {
				return s;
			}
		}
		return null;
	}
	
	@Override
	public Object getInformation2(ITextViewer textViewer, IRegion subject) {
		if (fImplementation == null)
			return null;
		return fImplementation.getHoverInfo2(textViewer, subject);
	}
	
	@Override
	public IInformationControlCreator getInformationPresenterControlCreator() {
		if (fImplementation == null)
			return null;
		return fImplementation.getInformationPresenterControlCreator();
	}
}
