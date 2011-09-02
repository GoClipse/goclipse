package com.googlecode.goclipse.go.lang.parser;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.go.lang.lexer.TokenListener;
import com.googlecode.goclipse.go.lang.lexer.TokenType;
import com.googlecode.goclipse.go.lang.lexer.Tokenizer;
import com.googlecode.goclipse.go.lang.model.Function;
import com.googlecode.goclipse.go.lang.model.Method;
import com.googlecode.goclipse.go.lang.model.Scope;
import com.googlecode.goclipse.go.lang.model.Type;
import com.googlecode.goclipse.go.lang.model.Var;

/**
 * 
 * @author steel
 */
public class ScopeParser implements TokenListener {

	private int   tokenOnLineCount = 0;
	private int   scope_tracker    = 0;
	private Scope root_scope       = null;
	private Scope currentScope     = null;
	private int   linenumber       = 0;

	/**
	 * 
	 * @param tokenizer
	 */
	public ScopeParser(Tokenizer tokenizer) {
		root_scope = new Scope(null);
		currentScope = root_scope;
		tokenizer.addTokenListener(this);
	}
	
	public Scope getRootScope(){
		return root_scope;
	}

	@Override
	public void tokenFound(TokenType type, String value, boolean inComment,	int linenumber, int start, int end) {
		try {
			if (inComment) {
				return;
			}

			// push new scope
			if (TokenType.LBRACE.equals(type)) {
				scope_tracker++;
				Scope s = new Scope(currentScope);
				currentScope = s;
			}

			if (TokenType.RBRACE.equals(type)) {
				scope_tracker--;
				currentScope = currentScope.getParent();
			}

			// guard against identifiers named 'func'
			if (!(type.isWhiteSpace())) {
				tokenOnLineCount++;
			}

			if (TokenType.NEWLINE.equals(type)) {
				tokenOnLineCount = 0;
			}			
		} catch (RuntimeException e) {
			Activator.logError(e);
		}
	}

	@Override
	public boolean isWhitespaceParser() {
		return true;
	}
	
	public void addVariable(Var var){
		// INFO: not for production
		//System.out.println(">>>>>>>>>>>.. "+var.getLine());
		currentScope.addVariable(var);
	}
	
	public void addFunction(Function func){
		currentScope.addFunction(func);
	}
	
	public void addMethod(Method method){
		currentScope.addMethod(method);
	}
	
	public void addType(Type type){
		currentScope.addType(type);
	}

}

