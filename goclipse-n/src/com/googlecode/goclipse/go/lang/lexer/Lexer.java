package com.googlecode.goclipse.go.lang.lexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Lexer {

	/**
	 * notify interested parties
	 */
	private List<LexerListener> listeners = new ArrayList<LexerListener>();
	private List<TokenUnit> tokenStream   = new ArrayList<TokenUnit>();
	
	private static boolean DEBUG = false;

	/**
	 * Count the line numbers
	 */
	private int lineNumber = 0;
	private int columnCount = -1;
	private boolean lineCommentState = false;
	private boolean blockCommentState = false;


	
	/**
	 * Identifier accumulator;
	 */
	StringBuilder identifier;

	/**
    * 
    */
	public Lexer() {
		addLexerListener(new LexerListener() {
			
			@Override
			public void tokenFound(TokenType type, String value, boolean inComment,
					int start, int end) {
				TokenUnit unit = new TokenUnit();
				unit.tokenType = type;
				unit.text 	   = value;
				unit.start     = start;
				unit.end       = end;
				tokenStream.add(unit);
			}
			
			@Override
			public void newline(int lineNumber) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean isWhitespaceParser() {
				return false;
			}
		});
	}

	/**
    * 
    */
	public void addLexerListener(LexerListener l) {
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}

	/**
    * 
    */
	public void removeLexerListsner(LexerListener l) {
		listeners.remove(l);
	}

	/**
	 * @param type
	 * @param value
	 */
	public void fireTokenFound(TokenType type, String value) {

		if (type != TokenType.IDENTIFIER) {
			flush();
		} else {
			for (LexerListener listener : listeners) {
				listener.tokenFound(type, value, lineCommentState
						|| blockCommentState, columnCount - value.length(),
						columnCount);
			}
			return;
		}
		
		for (LexerListener listener : listeners) {
			listener.tokenFound(type, value, lineCommentState
					|| blockCommentState, columnCount + 1 - value.length(),
					columnCount + 1);
		}
	}

	/**
	 * @param type
	 * @param value
	 */
	public void fireNewline() {
		lineCommentState = false;
		lineNumber++;
		for (LexerListener listener : listeners) {
			listener.newline(lineNumber);
		}
	}

	/**
	 * @param file
	 * @throws IOException
	 */
	public void scan(File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));

		char behind = 0, ahead = 0, current = 0;
		int val;
		int index = 0;

		boolean eatToken = true;
		identifier = new StringBuilder();
		fireNewline();
		while ((val = br.read()) != -1) {
			columnCount++;

			behind = current;
			current = ahead;
			ahead = (char) val;

			if (eatToken) {
				eatToken = false;
				continue;
			}

			processCharacter(ahead, current);
		}

		// process last character
		current = ahead;
		processCharacter(ahead, current);
	}

	/**
	 * @param file
	 * @throws IOException
	 */
	public void scan(String text) throws IOException {

		char behind = 0, ahead = 0, current = 0;
		int index = 0;

		boolean eatToken 		  = true;
		boolean skipNextCharacter = false;
		
		identifier = new StringBuilder();
		fireNewline();
		
		for (char val : text.toCharArray()) {
			columnCount++;

			behind = current;
			current = ahead;
			ahead = (char) val;

			if (eatToken) {
				eatToken = false;
				continue;
			}
			
			if(skipNextCharacter){
				skipNextCharacter = false;
			}
			else{
				skipNextCharacter = processCharacter(ahead, current);
			}
		}

		// process last character
		current = ahead;
		processCharacter(ahead, current);
	}

	/**
	 * @param ahead
	 * @param current
	 */
	private boolean processCharacter(char ahead, char current) {
		if (DEBUG) {
			System.out.print(current);
		}

		// if(lineNumber==3){
		// System.out.println("BREAK");
		// }
		switch (current) {

		case '~':
			fireTokenFound(TokenType.TILDA, "~");
			break;

		case '`':
			fireTokenFound(TokenType.GRAVE_ACCENT, TokenType.GRAVE_ACCENT.op);
			break;

		case '!':
			if (ahead == '=') {
				fireTokenFound(TokenType.NOT_EQ, TokenType.NOT_EQ.op);
			} else {
				fireTokenFound(TokenType.NOT, TokenType.NOT.op);
			}
			break;

		case '@':
			fireTokenFound(TokenType.AT, TokenType.AT.op);
			break;

		case '#':
			fireTokenFound(TokenType.POUND, TokenType.POUND.op);
			break;

		case '$':
			fireTokenFound(TokenType.DOLLAR, TokenType.DOLLAR.op);
			break;

		case '%':
			if (ahead == '=') {
				fireTokenFound(TokenType.REMAINDER_ASSIGN,
						TokenType.REMAINDER_ASSIGN.op);
			} else {
				fireTokenFound(TokenType.REMAINDER, TokenType.REMAINDER.op);
			}
			break;

		case '^':
			if (ahead == '=') {
				fireTokenFound(TokenType.BITWISE_XOR_ASSIGN,
						TokenType.BITWISE_XOR_ASSIGN.op);
			} else {
				fireTokenFound(TokenType.BITWISE_XOR, TokenType.BITWISE_XOR.op);
			}
			break;

		case '&':
			if (ahead == '&') {
				fireTokenFound(TokenType.AND, TokenType.AND.op);
			} else if (ahead == '=') {
				fireTokenFound(TokenType.BITWISE_AND_ASSIGN,
						TokenType.BITWISE_AND_ASSIGN.op);
			} else {
				fireTokenFound(TokenType.BITWISE_AND, TokenType.BITWISE_AND.op);
			}
			break;

		case '*':
			if (ahead == '=') {
				fireTokenFound(TokenType.MULTIPLY_ASSIGN,
						TokenType.MULTIPLY_ASSIGN.op);
			} else if (ahead == '/') {
				blockCommentState = false;
				fireTokenFound(TokenType.BLOCK_COMMENT_END,
						TokenType.BLOCK_COMMENT_END.op);
			} else {
				fireTokenFound(TokenType.MULTIPLY, TokenType.MULTIPLY.op);
			}
			break;

		case '/':
			if (ahead == '/') {
				lineCommentState = true;
				fireTokenFound(TokenType.COMMENT, TokenType.COMMENT.op);
				return true;
			} else if (ahead == '*') {
				blockCommentState = true;
				fireTokenFound(TokenType.BLOCK_COMMENT_START,
						TokenType.BLOCK_COMMENT_START.op);
				return true;
			} else {
				fireTokenFound(TokenType.DIVIDE, TokenType.DIVIDE.op);
			}
			break;

		case '(':
			fireTokenFound(TokenType.LPAREN, TokenType.LPAREN.op);
			break;

		case ')':
			fireTokenFound(TokenType.RPAREN, TokenType.RPAREN.op);
			break;

		case '-':
			if (ahead == '=') {
				fireTokenFound(TokenType.SUBTRACT_ASSIGN,
						TokenType.SUBTRACT_ASSIGN.op);
			} else {
				fireTokenFound(TokenType.SUBTRACT, TokenType.SUBTRACT.op);
			}
			break;

		case '_':
			if (Character.isJavaIdentifierPart(ahead)) {
				identifier.append(current);
			} else {
				fireTokenFound(TokenType.UNDERSCORE, TokenType.UNDERSCORE.op);
			}
			break;

		case '+':
			if (ahead == '+') {
				fireTokenFound(TokenType.INCREMENT, TokenType.INCREMENT.op);
			} else if (ahead == '=') {
				fireTokenFound(TokenType.ADD_ASSIGN, TokenType.ADD_ASSIGN.op);
			} else {
				fireTokenFound(TokenType.ADD, TokenType.ADD.op);
			}
			break;

		case '=':

			if (ahead == '=') {
				fireTokenFound(TokenType.EQUAL, TokenType.EQUAL.op);
			} else {
				fireTokenFound(TokenType.ASSIGN, TokenType.ASSIGN.op);
			}
			break;

		case '{':
			fireTokenFound(TokenType.LBRACE, TokenType.LBRACE.op);
			break;

		case '[':
			fireTokenFound(TokenType.LBRACKET, TokenType.LBRACKET.op);
			break;

		case '}':
			fireTokenFound(TokenType.RBRACE, TokenType.RBRACE.op);
			break;

		case ']':
			fireTokenFound(TokenType.RBRACKET, TokenType.RBRACKET.op);
			break;

		case '|':
			if (ahead == '=') {
				fireTokenFound(TokenType.BITWISE_OR_ASSIGN,
						TokenType.BITWISE_OR_ASSIGN.op);
			} else if (ahead == '|') {
				fireTokenFound(TokenType.BITWISE_XOR, TokenType.BITWISE_XOR.op);
			} else {
				fireTokenFound(TokenType.ASSIGN, TokenType.ASSIGN.op);
			}
			break;

		case '\\':
			fireTokenFound(TokenType.BACKSLASH, TokenType.BACKSLASH.op);
			break;

		case ':':
			fireTokenFound(TokenType.COLON, TokenType.COLON.op);
			break;

		case ';':
			fireTokenFound(TokenType.SEMICOLON, TokenType.SEMICOLON.op);
			break;

		case '"':
			fireTokenFound(TokenType.QUOTE, TokenType.QUOTE.op);
			break;

		case '\'':
			fireTokenFound(TokenType.APOSTROPHE, TokenType.APOSTROPHE.op);
			break;

		case '<':
			if (ahead == '=') {
				fireTokenFound(TokenType.LESS_EQ, TokenType.LESS_EQ.op);
			} else {
				fireTokenFound(TokenType.LESS, TokenType.LESS.op);
			}
			break;

		case ',':
			fireTokenFound(TokenType.COMMA, TokenType.COMMA.op);
			break;

		case '>':
			if (ahead == '=') {
				fireTokenFound(TokenType.GREATER_EQ, TokenType.GREATER_EQ.op);
			} else {
				fireTokenFound(TokenType.GREATER, TokenType.GREATER.op);
			}
			break;

		case '.':
			fireTokenFound(TokenType.PERIOD, TokenType.PERIOD.op);
			break;

		case '?':
			fireTokenFound(TokenType.QUESTION, TokenType.QUESTION.op);
			break;

		case ' ':
			flush();
			fireTokenFound(TokenType.SPACE, TokenType.SPACE.op);
			break;
		case '\t':
			flush();
			fireTokenFound(TokenType.TAB, TokenType.TAB.op);
			break;
		case '\n':
			flush();
			fireTokenFound(TokenType.NEWLINE, TokenType.NEWLINE.op);
			fireNewline();
			columnCount = 0;

			break;
		case '\f':
			flush();
//			fireTokenFound(TokenType.FORMFEEDLINE, TokenType.FORMFEEDLINE.op);
			break;
		case '\r':
			flush();
//			fireTokenFound(TokenType.RETURN, TokenType.RETURN.op);
			break;
		case '\b':
			flush();
//			fireTokenFound(TokenType.BACKSPACE, TokenType.BACKSPACE.op);
			break;

		// case '1':
		// case '2':
		// case '3':
		// case '4':
		// case '5':
		// case '6':
		// case '7':
		// case '8':
		// case '9':
		// case '0':
		// break;

		// case 'A': break;
		// case 'a': break;
		// case 'B': break;
		// case 'b': break;
		// case 'C': break;

		default:
			identifier.append(current);
			break;
		}
		
		return false;
	}

	public void flush() {
		if (identifier.length() > 0) {
			fireTokenFound(TokenType.IDENTIFIER, identifier.toString());
			identifier = new StringBuilder();
		}
	}

	public static void main(String... args) {
		Lexer lexer = new Lexer();
		DEBUG = true;
		try {
			lexer.addLexerListener(new LexerListener() {

				@Override
				public void tokenFound(TokenType type, String value,
						boolean inComment, int start, int end) {
					System.out.print("\t[" + type.name() + ", " + value + " "
							+ start + ":" + end + " " + "]");
					System.out.println();
				}

				@Override
				public void newline(int lineNumber) {
					System.out.println("LINE " + lineNumber
							+ " ==========================");

				}

				@Override
				public boolean isWhitespaceParser() {
					// TODO Auto-generated method stub
					return true;
				}

			});
			lexer.scan(new File("test.go"));

			lexer.scan("module test\nimport fmt \"fmt\"");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the tokenStream
	 */
	public List<TokenUnit> getTokenStream() {
		return tokenStream;
	}

	public void reset() {
		lineNumber = 0;
		
	}
}
