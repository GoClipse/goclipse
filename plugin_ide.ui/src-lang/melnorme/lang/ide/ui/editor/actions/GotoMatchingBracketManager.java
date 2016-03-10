/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Bruno Medeiros - copied from org.eclipse.jdt.internal.ui.javaeditor.JavaEditor.gotoMatchingBracket()
 *******************************************************************************/
package melnorme.lang.ide.ui.editor.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.ITextViewerExtension5;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.source.ICharacterPairMatcher;

import melnorme.lang.ide.ui.editor.AbstractLangEditor;
import melnorme.lang.ide.ui.editor.EditorUtils;
import melnorme.lang.ide.ui.editor.LangEditorMessages;
import melnorme.lang.ide.ui.editor.text.LangPairMatcher;
import melnorme.utilbox.ownership.IDisposable;

public class GotoMatchingBracketManager implements IDisposable {
	
	protected final AbstractLangEditor langEditor;
	
	/**
	 * Previous location history for goto matching bracket action.
	 * 
	 * @since 3.8
	 */
	private List<IRegion> fPreviousSelections;
	
	public GotoMatchingBracketManager(AbstractLangEditor abstractLangEditor) {
		this.langEditor = abstractLangEditor;
	}
	
	protected LangPairMatcher getBracketMatcher() {
		return langEditor.getBracketMatcher();
	}
	
	protected void initializePreviousSelectionList() {
		fPreviousSelections= new ArrayList<IRegion>(3);
	}
	
	@Override
	public void dispose() {
		fPreviousSelections.clear();
	}
	
	public void gotoMatchingBracket() {
		ITextViewer sourceViewer = langEditor.getSourceViewer_();
		
		IDocument document= sourceViewer.getDocument();
		if (document == null)
			return;
		
		IRegion selection= EditorUtils.getSignedSelection(sourceViewer);
		if (fPreviousSelections == null)
			initializePreviousSelectionList();
		
		IRegion region= getBracketMatcher().match(document, selection.getOffset(), selection.getLength());
		if (region == null) {
			region= getBracketMatcher().findEnclosingPeerCharacters(document, selection.getOffset(), selection.getLength());
			initializePreviousSelectionList();
			fPreviousSelections.add(selection);
		} else {
			if (fPreviousSelections.size() == 2) {
				if (!selection.equals(fPreviousSelections.get(1))) {
					initializePreviousSelectionList();
				}
			} else if (fPreviousSelections.size() == 3) {
				if (selection.equals(fPreviousSelections.get(2)) && !selection.equals(fPreviousSelections.get(0))) {
					IRegion originalSelection= fPreviousSelections.get(0);
					sourceViewer.setSelectedRange(originalSelection.getOffset(), originalSelection.getLength());
					sourceViewer.revealRange(originalSelection.getOffset(), originalSelection.getLength());
					initializePreviousSelectionList();
					return;
				}
				initializePreviousSelectionList();
			}
		}
		
		if (region == null) {
			langEditor.setStatusLineErrorMessage(LangEditorMessages.GotoMatchingBracket_error_noMatchingBracket);
			sourceViewer.getTextWidget().getDisplay().beep();
			return;
		}
		
		int offset= region.getOffset();
		int length= region.getLength();
		
		if (length < 1)
			return;
		
		int anchor= getBracketMatcher().getAnchor();
		// http://dev.eclipse.org/bugs/show_bug.cgi?id=34195
		int targetOffset= (ICharacterPairMatcher.RIGHT == anchor) ? offset + 1 : offset + length - 1;
		
		boolean visible= false;
		if (sourceViewer instanceof ITextViewerExtension5) {
			ITextViewerExtension5 extension= (ITextViewerExtension5) sourceViewer;
			visible= (extension.modelOffset2WidgetOffset(targetOffset) > -1);
		} else {
			IRegion visibleRegion= sourceViewer.getVisibleRegion();
			// http://dev.eclipse.org/bugs/show_bug.cgi?id=34195
			visible= (targetOffset >= visibleRegion.getOffset() && targetOffset <= visibleRegion.getOffset() + visibleRegion.getLength());
		}
		
		if (!visible) {
			langEditor.setStatusLineErrorMessage(LangEditorMessages.GotoMatchingBracket_error_bracketOutsideSelectedElement);
			sourceViewer.getTextWidget().getDisplay().beep();
			return;
		}
		
		int adjustment= getBracketMatcher().getOffsetAdjustment(document, selection.getOffset() + selection.getLength(), selection.getLength());
		targetOffset+= adjustment;
		int direction= (selection.getLength() == 0) ? 0 : ((selection.getLength() > 0) ? 1 : -1);
		if (fPreviousSelections.size() == 1 && direction < 0) {
			targetOffset++;
		}
		
		if (fPreviousSelections.size() > 0) {
			fPreviousSelections.add(new Region(targetOffset, direction));
		}
		sourceViewer.setSelectedRange(targetOffset, direction);
		sourceViewer.revealRange(targetOffset, direction);
	}
	
}