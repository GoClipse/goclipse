package com.googlecode.goclipse.go.lang.parser;

import com.googlecode.goclipse.go.lang.lexer.TokenListener;
import com.googlecode.goclipse.go.lang.lexer.TokenType;

/**
 * 
 * @author steel
 */
public class InterfaceParser implements TokenListener {

	private enum State {
		START, CONSUME_INTERFACE, CONSUME_INTERFACE_SIGNATURE, FINISHED, ERROR
	}

	private State state = State.START;
	private StringBuffer comment = new StringBuffer();
	private StringBuffer text = new StringBuffer();
	private int lastCommentLine = 0;

	@Override
	public void tokenFound(TokenType type, String value, boolean inComment,
			int linenumber, int start, int end) {

		if (inComment) {
			if (!TokenType.COMMENT.equals(type)
					&& !TokenType.BLOCK_COMMENT_START.equals(type)
					&& !TokenType.BLOCK_COMMENT_END.equals(type)) {

				if (linenumber - lastCommentLine > 1) {
					comment = new StringBuffer();
				}

				if (linenumber > lastCommentLine
						&& TokenType.DIVIDE.equals(type)) {
					lastCommentLine = linenumber;
				} else {
					comment.append(value);
					lastCommentLine = linenumber;
				}
			}
			return;
		}

		switch (state) {
		case START:
			if (TokenType.TYPE.equals(type)) {

				if (linenumber - lastCommentLine > 1) {
					comment = new StringBuffer();
				}

				state = State.CONSUME_INTERFACE;
			}
			break;
		case CONSUME_INTERFACE:
			if (TokenType.IDENTIFIER.equals(type)) {

				if (linenumber - lastCommentLine > 1) {
					comment = new StringBuffer();
				}

				state = State.CONSUME_INTERFACE;
			}
			break;

		}

	}

	@Override
	public boolean isWhitespaceParser() {
		return true;
	}
}
