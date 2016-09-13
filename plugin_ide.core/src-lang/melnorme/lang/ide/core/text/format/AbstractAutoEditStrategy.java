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
package melnorme.lang.ide.core.text.format;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.text.TextSourceUtils;
import melnorme.lang.ide.core.text.format.ILastKeyInfoProvider.KeyCommand;

public abstract class AbstractAutoEditStrategy implements IAutoEditStrategy {
	
	protected final ILastKeyInfoProvider lastKeyInfoProvider;
	
	public AbstractAutoEditStrategy(ILastKeyInfoProvider lastKeyInfoProvider) {
		this.lastKeyInfoProvider = assertNotNull(lastKeyInfoProvider);
	}
	
	public KeyCommand getLastPressedKey() {
		return assertNotNull(lastKeyInfoProvider.getLastPressedKey());
	}
	
	protected boolean keyWasBackspace() {
		return getLastPressedKey() == KeyCommand.BACKSPACE;
	}
	
	protected boolean keyWasDelete() {
		return getLastPressedKey() == KeyCommand.DELETE;
	}
	
	protected boolean keyWasEnter() {
		return getLastPressedKey() == KeyCommand.ENTER;
	}
	
	/* -----------------  ----------------- */
	
	protected String docContents;
	
	@Override
	public void customizeDocumentCommand(IDocument doc, DocumentCommand cmd) {
		if (cmd.doit == false)
			return;
		
		docContents = doc.get();
		try {
			doCustomizeDocumentCommand(doc, cmd);
		} catch (BadLocationException e) {
			LangCore.logError("BadLocationException in " + getClass().getSimpleName() + ".", e);
		}
	}
	
	protected abstract void doCustomizeDocumentCommand(IDocument doc, DocumentCommand cmd) 
			throws BadLocationException;
	
	@SuppressWarnings("unused")
	public boolean isSimpleNewLineKeyPress(DocumentCommand cmd) {
		return keyWasEnter();
	}
	
	public static boolean isSimpleKeyPressCommand(DocumentCommand cmd) {
		return cmd.length == 0 && cmd.text.length() == 1;
	}
	
	/* -----------------  utils  ----------------- */
	
	public String getLineIndentForOffset(int offset) {
		return TextSourceUtils.getLineIndentForOffset(docContents, offset);
	}
	
	public String getLineIndentForLineStart(int lineStart) {
		return TextSourceUtils.getLineIndentForLineStart(docContents, lineStart);
	}
	
	public String getLineIndentForLineStart(int lineStart, int end) {
		return TextSourceUtils.getLineIndentForLineStart(docContents, lineStart, end);
	}
	
	public int findEndOfIndent(int offset) {
		return TextSourceUtils.findEndOfIndent(docContents, offset);
	}
	
	/* -----------------  ----------------- */
	
	public static int getRegionEnd(IRegion region) {
		return region.getOffset() + region.getLength();
	}
	
	protected static boolean equalsDocumentString(String expectedIndentStr, IDocument doc, IRegion lineRegion)
			throws BadLocationException {
		int length = Math.min(lineRegion.getLength(), expectedIndentStr.length());
		String lineIndent = doc.get(lineRegion.getOffset(), length);
		return expectedIndentStr.equals(lineIndent);
	}
	
	
}