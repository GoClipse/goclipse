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
package melnorme.lang.ide.ui.editor;


import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.information.IInformationPresenter;
import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

import melnorme.lang.ide.core.text.TextSourceUtils;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.NumberUtil;
import melnorme.utilbox.ownership.IDisposable;

public class LangSourceViewer extends ProjectionViewerExt implements ISourceViewerExt {
	
	public LangSourceViewer(Composite parent, IVerticalRuler verticalRuler, int styles) {
		super(parent, verticalRuler, null, false, styles);
	}
	
	public LangSourceViewer(Composite parent, IVerticalRuler verticalRuler, IOverviewRuler overviewRuler,
			boolean showAnnotationsOverview, int styles) {
		super(parent, verticalRuler, overviewRuler, showAnnotationsOverview, styles);
	}
	
	/* -----------------  Shift operation  ----------------- */
	
	@Override
	public boolean canDoOperation(int operation) {
		if (getTextWidget() == null)
			return false;
		
		switch (operation) {
		case SHIFT_LEFT:
			return isEditable() && fIndentChars != null;
		case SHIFT_RIGHT:
			return isEditable() && fIndentChars != null && (areMultipleLinesSelected() || isCursorAtIndent());
		}
		
		return super.canDoOperation(operation);
	}
	
	protected boolean isCursorAtIndent() {
		IDocument document = getDocument();
		if(document == null) {
			return false;
		}
		
		Point sel = getSelectedRange();
		try {
			int startLine = document.getLineOfOffset(sel.x);
			IRegion line = document.getLineInformation(startLine);
			int lineStart = line.getOffset();
			
			String indent = TextSourceUtils.getLineIndentForLine(document.get(), line);
			return NumberUtil.isInRange(lineStart, sel.x, lineStart + indent.length());
		} catch(BadLocationException x) {
		}
		return false;
	}
	
	@Override
	protected void shift(boolean useDefaultPrefixes, boolean right, boolean ignoreWhitespace) {
		Point selection = getSelectedRange();
		
		super.shift(useDefaultPrefixes, right, ignoreWhitespace);
		
		Point newSelection = getSelectedRange();
		if(selection.y == 0 && selection.x == newSelection.x) {
			// Prevent the shift from creating a selection, this happens due to a quirk in shift operation	
			setSelectedRange(selection.x + newSelection.y, 0);
		}
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
	public void showOutline() throws CommonException {
		if(outlinePresenter != null) {
			outlinePresenter.showInformation();
		} else {
			throw new CommonException("Outline not available. ", null);
		}
	}
	
	
	/* ----------------- Content Assist ----------------- */
	
	// Override to grant public access to field.
	@Override
	public IContentAssistant getContentAssistant() {
		return fContentAssistant;
	}
	
}