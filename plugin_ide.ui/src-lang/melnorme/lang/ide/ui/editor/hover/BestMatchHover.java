/*******************************************************************************
 * Copyright (c) 2002, 2009 QNX Software Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     QNX Software Systems - Initial API and implementation
 *     Anton Leherbauer (Wind River Systems)
 *     Ericsson             - Fix improper hover order (Bug 294812)
 *******************************************************************************/
package melnorme.lang.ide.ui.editor.hover;

import java.util.ArrayList;
import java.util.List;

import melnorme.lang.ide.ui.LangEditorTextHoversRegistry;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.text.util.WordFinder;

import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.ui.IEditorPart;

/**
 * 'Fake' hover used to choose the best available hover.
 * This hover is always the first hover used and will delegate the hover
 * request to the best of the real hovers.  The 'best' hover is the first 
 * hover that returns some text for the specified parameters.
 * 
 */
public class BestMatchHover extends AbstractLangEditorTextHover {
	
	protected List<ILangEditorTextHover<?>> fInstantiatedTextHovers;
	protected ILangEditorTextHover<?> fBestHover;
	
	public BestMatchHover(IEditorPart editor) {
		setEditor(editor);
		prepareTextHovers();
	}
	
	protected void installTextHovers() {
		
		List<Class<? extends ILangEditorTextHover<?>>> textHoverSpecs = 
				LangEditorTextHoversRegistry.getTextHoversSpecifications();
		fInstantiatedTextHovers = new ArrayList<>(textHoverSpecs.size());
		
		for (int i= 0; i < textHoverSpecs.size(); i++) {
			Class<? extends ILangEditorTextHover<?>> klass = textHoverSpecs.get(i);
			try {
				fInstantiatedTextHovers.add(klass.newInstance());
			} catch (InstantiationException e) {
				LangUIPlugin.logInternalError(e);
			} catch (IllegalAccessException e) {
				LangUIPlugin.logInternalError(e);
			}
		}
	}
	
	protected void prepareTextHovers() {
		installTextHovers();
		
		for (ILangEditorTextHover<?> textHover : fInstantiatedTextHovers) {
			textHover.setEditor(fEditor);
		}
	}
	
	@Override
	public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
		return doGetHoverRegion(textViewer, offset);
	}
	
	public static IRegion doGetHoverRegion(ITextViewer textViewer, int offset) {
		return WordFinder.findWord(textViewer.getDocument(), offset);
	}
	
	@Override
	public Object getHoverInfo2(ITextViewer textViewer, IRegion hoverRegion) {
		fBestHover = null;
		
		for (ILangEditorTextHover<?> hover : fInstantiatedTextHovers) {
			if (hover == null) 
				continue;
			
			Object info = hover.getHoverInfo2(textViewer, hoverRegion);
			if (info != null) {
				fBestHover = hover;
				return info;
			}
		}
		
		return null;
	}
	
	@Override
	public IInformationControlCreator getHoverControlCreator() {
		if(fBestHover != null) {
			return fBestHover.getHoverControlCreator();
		}
		return null;
	}
	
	public IInformationControlCreator getHoverControlCreatorForInformationProvider() {
		if(fBestHover instanceof BrowserControlHover) {
			// Create a hover that is already focused.
			return new BrowserControlHover.BrowserControlCreator();
		}
		return null;
	}
	
}