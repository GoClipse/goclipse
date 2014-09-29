/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
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
import static melnorme.utilbox.core.CoreUtil.array;
import melnorme.lang.ide.ui.text.util.JavaWordFinder;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.hyperlink.AbstractHyperlinkDetector;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.texteditor.ITextEditor;

public abstract class LangHyperlinkDetector extends AbstractHyperlinkDetector {
	
	@Override
	public IHyperlink[] detectHyperlinks(ITextViewer textViewer, IRegion region, boolean canShowMultipleHyperlinks) {
		if (region == null)
			return null;
		ITextEditor textEditor = (ITextEditor) getAdapter(ITextEditor.class);
		
		IDocument document = EditorUtils.getEditorDocument(textEditor);
		
		IRegion elemRegion = JavaWordFinder.findWord(document, region.getOffset());
		
		return array(createHyperlink(region, textEditor, elemRegion));
	}
	
	protected abstract AbstractLangElementHyperlink createHyperlink(IRegion region, ITextEditor textEditor,
			IRegion elemRegion);
	
	public abstract class AbstractLangElementHyperlink implements IHyperlink {

		protected final IRegion region;
		protected final ITextEditor textEditor;
		protected final int offset;

		public AbstractLangElementHyperlink(int offset, IRegion region, ITextEditor textEditor) {
			this.offset = offset;
			this.region = assertNotNull(region);
			this.textEditor = assertNotNull(textEditor);
		}
		
		@Override
		public IRegion getHyperlinkRegion() {
			return region;
		}
		
		@Override
		public String getTypeLabel() {
			return getHyperlinkText();
		}
		
	}
	
	public static int getEndPos(IRegion region) {
		return region.getOffset() + region.getLength();
	}
	
}