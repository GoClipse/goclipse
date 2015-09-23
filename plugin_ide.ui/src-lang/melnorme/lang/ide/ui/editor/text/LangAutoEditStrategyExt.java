/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.editor.text;


import static melnorme.utilbox.core.CoreUtil.array;
import melnorme.lang.ide.core.text.BlockHeuristicsScannner;
import melnorme.lang.ide.core.text.BlockHeuristicsScannner.BlockTokenRule;
import melnorme.lang.ide.ui.LangAutoEditPreferenceConstants;
import melnorme.utilbox.misc.ArrayUtil;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;

public class LangAutoEditStrategyExt extends LangAutoEditStrategy {
	
	protected final String partitioning;
	protected final String contentType;
	protected boolean parenthesesAsBlocks;
	
	public LangAutoEditStrategyExt(String partitioning, String contentType, ITextViewer viewer) {
		super(viewer);
		this.partitioning = partitioning;
		this.contentType = contentType;
	}
	
	@Override
	public void customizeDocumentCommand(IDocument doc, DocumentCommand cmd) {
		parenthesesAsBlocks = LangAutoEditPreferenceConstants.AE_PARENTHESES_AS_BLOCKS.get();
		
		super.customizeDocumentCommand(doc, cmd);
	}
	
	@Override
	protected BlockHeuristicsScannner createBlockHeuristicsScanner(IDocument doc) {
		BlockTokenRule[] blockTokens = array(new BlockTokenRule('{', '}'));
		if(parenthesesAsBlocks) {
			blockTokens = ArrayUtil.concat(blockTokens, new BlockTokenRule('(', ')'));
		}
		return new BlockHeuristicsScannner(doc, partitioning, contentType, blockTokens);
	}
	
	@Override
	protected void smartIndentOnKeypress(IDocument doc, DocumentCommand cmd) throws BadLocationException {
//		rubyAutoEditStrategy.smartIndentOnKeypress(doc, cmd);
	}
	
	@Override
	protected void smartPaste(IDocument doc, DocumentCommand cmd) throws BadLocationException {
		//rubyAutoEditStrategy.smartPaste(doc, cmd);
	}
	
}
