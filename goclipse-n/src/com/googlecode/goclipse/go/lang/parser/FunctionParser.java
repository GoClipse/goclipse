package com.googlecode.goclipse.go.lang.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.go.lang.lexer.Lexer;
import com.googlecode.goclipse.go.lang.lexer.TokenListener;
import com.googlecode.goclipse.go.lang.lexer.TokenType;
import com.googlecode.goclipse.go.lang.lexer.Tokenizer;
import com.googlecode.goclipse.model.Function;
import com.googlecode.goclipse.model.Method;

/**
 * 
 * @author steel
 * 
 */
public class FunctionParser implements TokenListener {

	private enum State {
		START, DETERMINE_TYPE, CONSUME_FUNCTION_HEADER, CONSUME_METHOD_HEADER, FINISHED, ERROR
	}

	private State state = State.START;
	private ArrayList<FunctionParserListener> funcListeners = new ArrayList<FunctionParserListener>();
	private ArrayList<Function> funcs = new ArrayList<Function>();
	private ArrayList<Method> methods = new ArrayList<Method>();
	private Function func = new Function();
	private Method method = new Method();
	private StringBuffer comment = new StringBuffer();
	private StringBuffer text = new StringBuffer();
	private int lastCommentLine = 0;
	private int tokenOnLineCount = 0;
	private int afterReceiver = 0;
	private boolean exportsOnly = true;
	private int scope_tracker = 0;

	/**
	 * 
	 * @param tokenizer
	 */
	public FunctionParser(boolean parseExportsOnly, Tokenizer tokenizer) {
		tokenizer.addTokenListener(this);
		exportsOnly = parseExportsOnly;
	}
	
	/**
	 * @param listener
	 */
	public void addFuncListeners(FunctionParserListener listener ){
		if(!funcListeners.contains(listener)){
			funcListeners.add(listener);
		}
	}
	
	/**
	 * 
	 */
	public void onFunctionEnter(){
		for(FunctionParserListener listener: funcListeners){
			listener.onEnterFunction(func);
		}
	}
	
	/**
	 * 
	 */
	public void onFunctionExit(){
		for(FunctionParserListener listener: funcListeners){
			listener.onExitFunction(func);
		}
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
				if (TokenType.FUNC.equals(type) && scope_tracker == 0
						&& tokenOnLineCount == 1) {

					if (linenumber - lastCommentLine > 1) {
						comment = new StringBuffer();
					}

					state = State.DETERMINE_TYPE;
				}
				break;

			case CONSUME_METHOD_HEADER:
				if (TokenType.IDENTIFIER.equals(type)) {
					text.append(value);
					if (afterReceiver == 1) {
						method.setInsertionText(value + "()");
						afterReceiver++;
					}
				} else if (TokenType.RPAREN.equals(type)) {
					text.append(value);
					afterReceiver++;
				} else if (TokenType.NEWLINE.equals(type)) {

					if (text.toString().lastIndexOf('{') != -1) {
						method.setName(text.toString().substring(0,
								text.toString().lastIndexOf('{')));
					} else {
						method.setName(text.toString());
					}
					method.setLine(linenumber);
					method.setDocumentation(comment.toString());
					text = new StringBuffer();

					// sometimes we only wanted exported methods
					if (method != null && method.getInsertionText() != null) {
						if ((exportsOnly && Character.isUpperCase(method
								.getInsertionText().charAt(0))) || !exportsOnly) {
							methods.add(method);
						}
					}

					method = new Method();
					comment = new StringBuffer();
					state = State.START;
					afterReceiver = 0;
				} else {
					text.append(value);
				}
				break;
			case CONSUME_FUNCTION_HEADER:
				if (TokenType.IDENTIFIER.equals(type)) {
					text.append(value);

					if (func.getInsertionText() == null) {
						func.setInsertionText(value + "()");
					}
				} else if (TokenType.NEWLINE.equals(type)) {

					if (text.toString().lastIndexOf('{') != -1) {
						func.setName(text.toString().substring(0,
								text.toString().lastIndexOf('{')));
					} else {
						func.setName(text.toString());
					}
					func.setDocumentation(comment.toString());
					text = new StringBuffer();

					// sometimes we only wanted exported functions
					if (func != null && func.getInsertionText() != null) {
						if ((exportsOnly && Character.isUpperCase(func
								.getInsertionText().charAt(0))) || !exportsOnly) {
							funcs.add(func);
						}
					}

					func = new Function();
					comment = new StringBuffer();
					state = State.START;
				} else {
					text.append(value);
				}
				break;
			case DETERMINE_TYPE:
				if (TokenType.LPAREN.equals(type)) {
					state = State.CONSUME_METHOD_HEADER;
					text.append(value);
				} else if (TokenType.IDENTIFIER.equals(type)) {
					state = State.CONSUME_FUNCTION_HEADER;

					text.append(value);
					func.setLine(linenumber);

					if (func.getInsertionText() == null) {
						func.setInsertionText(value + "()");
					}
				}
				break;
			case FINISHED:
				break;
			}
		} catch (RuntimeException e) {
			System.out.println(e);
		}
	}

	public static void main(String[] args) {

		Lexer lexer = new Lexer();
		Tokenizer tokenizer = new Tokenizer(lexer);
		FunctionParser fparser = new FunctionParser(false, tokenizer);

		try {
			lexer.scan(new File("test_go/import_test.go"));
			for (Function func : fparser.funcs) {
				Activator.logInfo("=================================================");
				Activator.logInfo(func.getDocumentation());
				Activator.logInfo("-------------------------------------------------");
				Activator.logInfo(func.getName());
				Activator.logInfo(func.getInsertionText());
				Activator.logInfo("-------------------------------------------------");
			}

			Activator.logInfo("\n");
			Activator.logInfo("=================================================");
			Activator.logInfo("[METHODS]");

			for (Method method : fparser.methods) {
				Activator.logInfo("=================================================");
				Activator.logInfo(method.getDocumentation());
				Activator.logInfo("-------------------------------------------------");
				Activator.logInfo(method.getName());
				Activator.logInfo(method.getInsertionText());
				Activator.logInfo("-------------------------------------------------");
			}

		} catch (IOException e) {
			Activator.logError(e);
		}
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
