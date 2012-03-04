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

import java.util.Stack;

/**
 * @author steel
 */
public class ScopeParser implements TokenListener {

	private int     tokenOnLineCount = 0;
	private Scope   root_scope       = null;
	private Scope   currentScope     = null;
	private int     linenumber       = 0;
	private Stack<TokenType> stack   = new Stack<TokenType>();
	
	/**
	 * @param tokenizer
	 */
	public ScopeParser(Tokenizer tokenizer) {
		root_scope = new Scope(null, "root");
		currentScope = root_scope;
		tokenizer.addTokenListener(this);
	}
	
	/**
	 * @return
	 */
	public Scope getRootScope(){
		return root_scope;
	}

	/**
	 * 
	 */
	@Override
	public void tokenFound(TokenType type, String value, boolean inComment,	int linenumber, int start, int end) {
		try {
			if (inComment) {
				return;
			}
			
			if(!type.isWhiteSpace()){
				//System.out.println(value);
			}
			
			if (TokenType.CASE.equals(type) || TokenType.DEFAULT.equals(type)||TokenType.RBRACE.equals(type)) {
				TokenType ptype = stack.pop();
				TokenType peek  = stack.peek();
				currentScope = currentScope.getParent();
				switch (peek) {
				case FUNC:
				case IF:
				case ELSE:
				case TYPE:
				case DEFAULT:
				case SWITCH:
					ptype = stack.pop();
					currentScope = currentScope.getParent();
				}
			}
			
			switch (type) {
			case FUNC:
			case IF:
			case ELSE:
			case TYPE:
			case CASE:
			case DEFAULT:
			case LBRACE:
			case SWITCH:
				stack.push(type);
				Scope s = new Scope(currentScope, value+":"+linenumber);
				currentScope = s;
				return;
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

	/**
	 * 
	 */
	@Override
	public boolean isWhitespaceParser() {
		return true;
	}
	
	/**
	 * @param var
	 */
	public void addVariable(Var var){
		currentScope.addVariable(var);
	}
	
	/**
	 * @param func
	 */
	public void addFunction(Function func){
		root_scope.addFunction(func);
	}
	
	/**
	 * @param method
	 */
	public void addMethod(Method method){
		root_scope.addMethod(method);
	}
	
	/**
	 * @param type
	 */
	public void addType(Type type){
		root_scope.addType(type);
	}
	
	/**
	 * 
	 */
	public void print(){
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		recursePrint("", root_scope);
	}
	
	/**
	 * @param indent
	 * @param scope
	 */
	private void recursePrint(String indent, Scope scope){
		scope.print(indent);
		for(Scope s: scope.getChildren()){
			recursePrint(indent+"   ", s);
		}
	}
}

