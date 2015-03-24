/*******************************************************************************
 * Copyright (c) 2000, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Tom Eicher <eclipse@tom.eicher.name> - [formatting] 'Format Element' in JavaDoc does also format method body - https://bugs.eclipse.org/bugs/show_bug.cgi?id=238746
 *     Tom Eicher (Avaloq Evolution AG) - block selection mode
 *******************************************************************************/
package melnorme.lang.ide.ui.editor;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;

/**
 * Editor utils, code copied from JDT.
 */
public class EditorUtils_JDT {
	
	/**
	 * Copy of {@link org.eclipse.jface.text.source.MatchingCharacterPainter#getSignedSelection()}
	 */
	public static final IRegion getSignedSelection(ITextViewer sourceViewer) {
		Point viewerSelection= sourceViewer.getSelectedRange();
	
		StyledText text= sourceViewer.getTextWidget();
		Point selection= text.getSelectionRange();
		if (text.getCaretOffset() == selection.x) {
			viewerSelection.x= viewerSelection.x + viewerSelection.y;
			viewerSelection.y= -viewerSelection.y;
		}
	
		return new Region(viewerSelection.x, viewerSelection.y);
	}
	
}