package com.googlecode.goclipse.go.lang.parser;

import java.util.ArrayList;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.go.lang.lexer.TokenListener;
import com.googlecode.goclipse.go.lang.lexer.TokenType;
import com.googlecode.goclipse.go.lang.lexer.Tokenizer;
import com.googlecode.goclipse.go.lang.model.Function;
import com.googlecode.goclipse.go.lang.model.Method;

/**
 * 
 * @author steel
 */
public class ScopeParser implements TokenListener {

	private enum State {
		START, MODULE_SCOPE, CONSUME_METHOD_SCOPE, CONSUME_FUNCTION_SCOPE, SCOPE, FINISHED, ERROR
	}

	private State               state            = State.START;
	private ArrayList<Function> funcs            = new ArrayList<Function>();
	private ArrayList<Method>   methods          = new ArrayList<Method>();
	private Function            func             = new Function();
	private Method              method           = new Method();
	private StringBuffer        comment          = new StringBuffer();
	private StringBuffer        text             = new StringBuffer();
	private int 		        lastCommentLine  = 0;
	private int                 tokenOnLineCount = 0;
	private int                 afterReceiver    = 0;
	private boolean             exportsOnly      = true;
	private int                 scope_tracker    = 0;

	/**
	 * 
	 * @param tokenizer
	 */
	public ScopeParser(boolean parseExportsOnly, Tokenizer tokenizer) {
		tokenizer.addTokenListener(this);
		exportsOnly = parseExportsOnly;
	}

	@Override
	public void tokenFound(TokenType type, String value, boolean inComment,
			int linenumber, int start, int end) {
		try {
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

			// Parsing top level functions only
			if (TokenType.LBRACE.equals(type)) {
				scope_tracker++;
			}

			if (TokenType.RBRACE.equals(type)) {
				scope_tracker--;
			}

			// guard against identifiers named 'func'
			if (!(type.isWhiteSpace())) {
				tokenOnLineCount++;
			}

			if (TokenType.NEWLINE.equals(type)) {
				tokenOnLineCount = 0;
			}

			switch (state) {

			case START:				
				break;

			case MODULE_SCOPE:
				break;
				
			case CONSUME_METHOD_SCOPE:
				break;
				
			case CONSUME_FUNCTION_SCOPE:
				break;
				
			case SCOPE:
				break;
				
			case FINISHED:
				break;
			}
		} catch (RuntimeException e) {
			Activator.logError(e);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		
	}

	@Override
	public boolean isWhitespaceParser() {
		return true;
	}

	/**
	 * @return
	 */
	public ArrayList<Method> getMethods() {
		return methods;
	}

	/**
	 * @return
	 */
	public ArrayList<Function> getFunctions() {
		return funcs;
	}
}

