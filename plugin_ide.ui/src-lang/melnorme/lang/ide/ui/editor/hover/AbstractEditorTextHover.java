/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.editor.hover;

import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextHoverExtension;
import org.eclipse.jface.text.ITextHoverExtension2;
import org.eclipse.jface.text.ITextViewer;

import melnorme.lang.ide.core.text.JavaWordFinder;

public abstract class AbstractEditorTextHover implements ITextHover, ITextHoverExtension, ITextHoverExtension2 
{
	
	public AbstractEditorTextHover() {
		super();
	}
	
	
	/* -----------------  ----------------- */
	
	@Override
	public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
		return JavaWordFinder.findWord(textViewer.getDocument(), offset);
	}
	
	@Deprecated
	@Override
	public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
		return null;
	}
	
	@Override
	public abstract Object getHoverInfo2(ITextViewer textViewer, IRegion hoverRegion);
	
	@Override
	public abstract IInformationControlCreator getHoverControlCreator();
	
}