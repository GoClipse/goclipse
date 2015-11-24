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


import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.ITextViewerExtension;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.widgets.Event;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.text.TextSourceUtils;

public abstract class AbstractAutoEditStrategy implements IAutoEditStrategy {
	
	protected Event lastKeyEvent;
	
	public AbstractAutoEditStrategy(ITextViewer viewer) {
		lastKeyEvent = new Event();
		if (viewer instanceof ITextViewerExtension) {
			VerifyKeyRecorder verifyKeyRecorder = new VerifyKeyRecorder();
			((ITextViewerExtension) viewer).appendVerifyKeyListener(verifyKeyRecorder);
			// Minor leak issue: we should remove verifyKeyRecorder if viewer is unconfigured
		} else {
			// allways use blank event in lastKeyEvent
		}
	}
	
	public final class VerifyKeyRecorder implements VerifyKeyListener {
		@Override
		public void verifyKey(VerifyEvent event) {
			lastKeyEvent.character = event.character;
			lastKeyEvent.keyCode = event.keyCode;
			lastKeyEvent.stateMask = event.stateMask;
		}
	}
	
	protected boolean keyWasBackspace() {
		return lastKeyEvent.character == SWT.BS;
	}
	
	protected boolean keyWasDelete() {
		return lastKeyEvent.character == SWT.DEL;
	}
	
	protected boolean keyWasEnter() {
		return lastKeyEvent.character == SWT.CR;
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