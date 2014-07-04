/**
 * 
 */
package com.googlecode.goclipse.go.lang.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.googlecode.goclipse.go.lang.lexer.Lexer;
import com.googlecode.goclipse.go.lang.lexer.TokenListener;
import com.googlecode.goclipse.go.lang.lexer.TokenType;
import com.googlecode.goclipse.go.lang.lexer.Tokenizer;
import com.googlecode.goclipse.go.lang.model.Var;

/**
 * 
 * @author steel
 */
public class InferenceVariableParser implements TokenListener {

	private enum State {
		START, ACCEPTED_ID, ACCEPTED_INFERENCE, ACCEPTED_ID_EXPR, ACCEPTED_EXPR, ACCEPTED_EXPR_COMMA
	}

	private File	          file	               = null;
	private ScopeParser	      scopeParser	       = null;
	private ArrayList<Var>	  vars	               = new ArrayList<Var>();
	private State	          state	               = State.START;
	private Var	              var	               = new Var();
	private StringBuffer	  comment	           = new StringBuffer();
	private int	              lastCommentLine	   = 0;
	private ArrayList<Var>	  varBuffer	           = new ArrayList<Var>();
	private ArrayList<String> typeBuffer           = new ArrayList<String>();
	private FunctionParser    functionParser       = null;
	
	/**
	 * @param tokenizer
	 */
	public InferenceVariableParser(Tokenizer tokenizer, File file, FunctionParser functionParser) {
		tokenizer.addTokenListener(this);
		this.file = file;
		var.setFile(file);
		this.functionParser = functionParser;
	}

	/**
	 * @param scopeParser
	 */
	public void setScopeParser(ScopeParser scopeParser) {
		this.scopeParser = scopeParser;
	}

	@Override
	public void tokenFound(TokenType type, String value, boolean inComment, int linenumber, int start, int end) {

		if (inComment) {
			if (!TokenType.COMMENT.equals(type) && !TokenType.BLOCK_COMMENT_START.equals(type)
			        && !TokenType.BLOCK_COMMENT_END.equals(type)) {

				if (linenumber > lastCommentLine && TokenType.DIVIDE.equals(type)) {
					lastCommentLine = linenumber;
				} else {
					comment.append(value);
					lastCommentLine = linenumber;
				}
			}
			return;
		}
		
		// return on some whitespace
		switch (type) {
			case SPACE:
			case TAB:
				return;
		}
		
		switch(state) {
			
			case START:
				
				if (TokenType.IDENTIFIER.equals(type)) {
					Var v = new Var();
					v.setName(value);
					v.setFile(file);
					v.setLine(linenumber);
					v.setInsertionText(value);
					varBuffer.add(v);
					state = State.ACCEPTED_ID;
				
				} else {
					varBuffer.clear();
				}
				
				break;
				
			case ACCEPTED_ID:
				if (TokenType.COMMA.equals(type)) {
					state = State.START;
					
				} else if (TokenType.INFERENCE.equals(type)) {
					state = State.ACCEPTED_INFERENCE;
				
				} else {
					varBuffer.clear();
				}
				
				break;
						
			case ACCEPTED_INFERENCE:
				
				System.out.println(value);
				
				// look for common patterns to determine type
				if (TokenType.NUMBER.equals(type)) {
					
					if (value.contains(".") || value.contains("e") || value.contains("E")){
						typeBuffer.add("float");
					
					} else {
						typeBuffer.add("int");
					}
					
					state = State.ACCEPTED_EXPR;
				
				} else if (TokenType.STRING.equals(type)) {
					typeBuffer.add("string");
					
				} else if (TokenType.IDENTIFIER.equals(type)) {
					String s = typeBuffer.get(typeBuffer.size()-1);
					s += value;
					
				} else if (TokenType.MAP.equals(type)) {
				
				} else if (TokenType.TRUE.equals(type) || TokenType.FALSE.equals(type) ) {
				
				} else if (TokenType.FUNC.equals(type)) {
					
				} else if (TokenType.LBRACE.equals(type)) {
					
				} else if (TokenType.RBRACE.equals(type)) {
					
				} else if (TokenType.MULTIPLY.equals(type)) {
					
				} else {
					state = State.START;
				}
				
				break;
			
			case ACCEPTED_ID_EXPR:
				if (TokenType.LPAREN.equals(type)) {
					typeBuffer.add("{func_call}");
				}
				break;
				
			case ACCEPTED_EXPR:
				break;
			
			case ACCEPTED_EXPR_COMMA:
				break;
		}
	}

	/**
	 * Handle the times we flush the buffer. We do this so those times we
	 * declare a var within a statement like a for loop, the new var falls into
	 * the inner scope and not the outer.
	 * 
	 * @param type
	 */
	private void handleVarBufferFlush(TokenType type) {
		switch (type) {
			case LBRACE:
			case RBRACE:
			case IF:
			case ELSE:
			case SWITCH:
			case FOR:
			case FUNC:
			case TYPE:
			case GO:
			case CASE:
			case DEFAULT:
				if (scopeParser != null) {
					for (Var var : varBuffer) {
						scopeParser.addVariable(var);
					}
					varBuffer.clear();
				}
		}
	}

	@Override
	public boolean isWhitespaceParser() {
		return false;
	}

	/**
	 * @return the vars
	 */
	public ArrayList<Var> getVars() {
		return vars;
	}

	/**
	 * @param vars
	 *            the vars to set
	 */
	public void setVars(ArrayList<Var> vars) {
		this.vars = vars;
	}

	public static void main(String[] args) {

		Lexer lexer = new Lexer();
		Tokenizer tokenizer = new Tokenizer(lexer);
		InferenceVariableParser fparser = new InferenceVariableParser(tokenizer, null, null);

		try {
			//lexer.scan(new File("test/test_go/import_test.go"));
			lexer.scan("i := \"0\"");
			for (Var var : fparser.vars) {
				System.out.println("=================================================");
				System.out.println(var.getDocumentation());
				System.out.println("-------------------------------------------------");
				System.out.println(var.getName());
				System.out.println(var.getInsertionText());
				System.out.println("-------------------------------------------------");
			}

		} catch (IOException e) {
			System.err.println(e);
		}
	}

}
