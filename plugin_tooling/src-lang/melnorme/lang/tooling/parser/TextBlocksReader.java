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
package melnorme.lang.tooling.parser;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import static melnorme.utilbox.core.CoreUtil.areEqual;
import static melnorme.utilbox.core.CoreUtil.arrayC;

import java.text.MessageFormat;

import melnorme.lang.utils.parse.BasicCharSource_CommonExceptionAdapter;
import melnorme.lang.utils.parse.IBasicCharSource;
import melnorme.lang.utils.parse.LexingUtils;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.ArrayUtil;

public class TextBlocksReader {
	
	public enum TokenKind {
		TEXT,
		BLOCK_OPEN,
		EOS,
	}
	
	/* -----------------  ----------------- */
	
	protected final BasicCharSource_CommonExceptionAdapter charSource;
	
	protected final char[] blockOpens;
	protected final char[] blockCloses;
	
	protected String lastValue;
	protected TokenKind lastKind;
	
	public TextBlocksReader(IBasicCharSource<? extends Exception> charSource) {
		this(charSource, 
			arrayC('{', '(', '['), 
			arrayC('}', ')', ']')
		);
	}
	
	public TextBlocksReader(IBasicCharSource<? extends Exception> charSource, 
			char[] blockOpens, char[] blockCloses) {
		this.charSource = new BasicCharSource_CommonExceptionAdapter(assertNotNull(charSource));
		this.blockOpens = assertNotNull(blockOpens);
		this.blockCloses = assertNotNull(blockCloses);
		assertTrue(blockOpens.length == blockCloses.length);
	}
	
	public int peekTokenStart() throws CommonException {
		LexingUtils.skipWhitespace(charSource);
		return charSource.lookahead();
	}
	
	protected char consumeChar() throws CommonException {
		lastKind = null;
		lastValue = null;
		return charSource.consume();
	}
	
	/* -----------------  ----------------- */
	
	public String setTokenResult(String value, TokenKind kind) {
		this.lastValue = value;
		this.lastKind = kind;
		return value;
	}
	
	public TokenKind tokenAhead() throws CommonException {
		int tokenStartChar = peekTokenStart();
		if(tokenStartChar == -1) {
			return TokenKind.EOS;
		}
		char ch = (char) tokenStartChar;
		
		if(charIsBlockOpen(ch)) {
			return TokenKind.BLOCK_OPEN;
		}
		if(charIsBlockClose(ch)) {
			return TokenKind.EOS;
		}
		return TokenKind.TEXT;
	}
	
	protected boolean charIsBlockOpen(char ch) {
		return ArrayUtil.indexOf(blockOpens, ch) != -1;
	}
	
	protected boolean charIsBlockClose(char ch) {
		return ArrayUtil.indexOf(blockCloses, ch) != -1;
	}
	
	public boolean aheadIsEnd() throws CommonException {
		return tokenAhead() == TokenKind.EOS;
	}
	
	public boolean aheadIsBlockStart() throws CommonException {
		return tokenAhead() == TokenKind.BLOCK_OPEN;
	}
	
	public boolean aheadIsText() throws CommonException {
		return tokenAhead() == TokenKind.TEXT;
	}
	
	public String consumeText() throws CommonException {
		if(tokenAhead() != TokenKind.TEXT) {
			throw createParseException("Expected text, {0}.", errorAtTokenStart());
		}
		return parseTextValue();
	}
	
	protected String parseTextValue() throws CommonException {
		peekTokenStart();
		
		StringBuilder sb = new StringBuilder();
		
		while(charSource.hasCharAhead()) {
			
			char ahead = charSource.lookaheadChar();
			if(Character.isWhitespace(ahead) || charIsBlockClose(ahead) || charIsBlockOpen(ahead)) {
				break;
			}
			
			char ch = consumeChar();
			if(ch == '"') {
				boolean isEOS = LexingUtils.consumeUntilDelimiter_intoStringBuilder(charSource, '"', '\\', sb);
				if(isEOS) {
					throw createParseException("Unterminated text `{0}`.", sb.toString());
				}
			} else {
				sb.append(ch);
			}
		}
		
		return setTokenResult(sb.toString(), TokenKind.TEXT);
	}
	
	/* -----------------  ----------------- */
	
	public TextBlocksSubReader enterBlock() throws CommonException {
		if(tokenAhead() != TokenKind.BLOCK_OPEN) {
			throw createParseException("Expected block open, {0}.", errorAtTokenStart());
		}
		
		return parseBlock();
	}
	
	protected TextBlocksSubReader parseBlock() throws CommonException {
		char ch = consumeChar();
		int ix = ArrayUtil.indexOf(blockOpens, ch);
		char expectedClose = blockCloses[ix];
		return new TextBlocksSubReader(this.charSource, this.blockOpens, this.blockCloses, expectedClose);
	}
	
	public static class TextBlocksSubReader extends TextBlocksReader implements AutoCloseable {
		
		protected final char expectedClose;
		
		public TextBlocksSubReader(IBasicCharSource<? extends Exception> charSource, char[] blockOpens,
				char[] blockCloses, char expectedClose) {
			super(charSource, blockOpens, blockCloses);
			this.expectedClose = expectedClose;
		}
		
		@Override
		public void close() throws CommonException {
			int ahead = peekTokenStart();
			if(ahead == expectedClose) {
				char ch = charSource.consume();
				assertTrue(ch == ahead);
			} else {
				throw createParseException("Expected BLOCK_CLOSE `{0}`, {1}.", expectedClose, errorAtTokenStart());
			}
		}
		
	}
	
	/* ----------------- Error handling  ----------------- */
	
	protected CommonException createParseException(String pattern, Object... arguments) {
		return new CommonException(MessageFormat.format(pattern, arguments));
	}
	
	public String errorAtTokenStart() throws CommonException {
		return found(peekTokenStart()); 
	}
	
	protected String found(int ahead) {
		if(ahead == -1) {
			return "found EOS";
		}
		char ch = (char) ahead;
		return "found `" + ch + "`";
	}
	
	/* ----------------- Helpers ----------------- */
	
	public void expectText(String expectedText) throws CommonException {
		if(tokenAhead() != TokenKind.TEXT) {
			throw createParseException("Expected text `{0}`, {1}.", expectedText, errorAtTokenStart());
		}
		
		String text = parseTextValue();
		if(!areEqual(text, expectedText)) {
			throw createParseException("Expected text `{0}`, found text `{1}`.", expectedText, text);
		}
	}
	
	public <RET, EXC extends Exception> RET consumeBlock(BlockVisitorX<RET, EXC> visitor) 
			throws CommonException, EXC {
		try(TextBlocksSubReader subReader = enterBlock()) {
			return visitor.consumeChildren(subReader);
		}
	}
	
	public interface BlockVisitorX<RET, EXC extends Exception> {
		
		public RET consumeChildren(TextBlocksSubReader subReader) throws EXC;
		
	}
	
	public void skipNextElement() throws CommonException {
		if(aheadIsEnd()) {
			throw createParseException("Expected `{0}`, {1}.", "element", errorAtTokenStart());
		} else if(aheadIsText()) {
			consumeText();
		} else {
			consumeBlock((subReader) -> {
				subReader.skipToEnd();
				return null;
			});
		} 
	}
	
	public void skipToEnd() throws CommonException {
		while(!aheadIsEnd()) {
			skipNextElement();
		}
	}
	
}