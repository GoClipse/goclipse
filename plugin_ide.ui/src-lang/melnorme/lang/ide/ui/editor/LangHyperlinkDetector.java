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
import melnorme.lang.ide.ui.editor.actions.AbstractOpenElementOperation;
import melnorme.lang.ide.ui.text.util.JavaWordFinder;
import melnorme.lang.tooling.ast.SourceRange;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.hyperlink.AbstractHyperlinkDetector;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.texteditor.ITextEditor;

public abstract class LangHyperlinkDetector extends AbstractHyperlinkDetector {
	
	@Override
	public IHyperlink[] detectHyperlinks(ITextViewer textViewer, IRegion requestedRegion,
			boolean canShowMultipleHyperlinks) {
		if (requestedRegion == null)
			return null;
		ITextEditor textEditor = (ITextEditor) getAdapter(ITextEditor.class);
		
		IDocument document = EditorUtils.getEditorDocument(textEditor);
		
		IRegion wordRegion = JavaWordFinder.findWord(document, requestedRegion.getOffset());
		
		return array(createHyperlink(requestedRegion, textEditor, wordRegion));
	}
	
	protected abstract AbstractLangElementHyperlink createHyperlink(IRegion requestedRegion, ITextEditor textEditor,
			IRegion wordRegion);
	
	public abstract class AbstractLangElementHyperlink implements IHyperlink {
		
		protected final IRegion region;
		protected final ITextEditor textEditor;
		
		public AbstractLangElementHyperlink(IRegion region, ITextEditor textEditor) {
			this.region = assertNotNull(region);
			this.textEditor = assertNotNull(textEditor);
		}
		
		public int getOffset() {
			return region.getOffset();
		}
		
		protected SourceRange getElementRange() {
			return new SourceRange(region.getOffset(), region.getLength());
		}
		
		@Override
		public IRegion getHyperlinkRegion() {
			return region;
		}
		
		@Override
		public String getTypeLabel() {
			return getHyperlinkText();
		}
		
		@Override
		public String getHyperlinkText() {
			return AbstractOpenElementOperation.OPEN_DEFINITION_OpName;
		}
		
	}
	
	public static int getEndPos(IRegion region) {
		return region.getOffset() + region.getLength();
	}
	
}