package com.googlecode.goclipse.go.lang.parser;

import java.io.File;
import java.util.Stack;

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
 * @author steel
 */
public class ScopeParser implements TokenListener {
	private File    file             = null;
	private int     tokenOnLineCount = 0;
	private Scope   root_scope       = null;
	private Scope   currentScope     = null;
	private int     linenumber       = 0;
	//private Stack<TokenType> stack   = new Stack<TokenType>();
	private Stack<Scope>  stack     = new Stack<Scope>();
	
	/**
	 * @param tokenizer
	 */
	public ScopeParser(Tokenizer tokenizer, File file) {
		tokenizer.addTokenListener(this);
		root_scope   = new Scope(null, "root");
		this.file    = file;
		currentScope = root_scope;
		currentScope.setFile(file);
		stack.push(currentScope);
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
			
			if (TokenType.LBRACE.equals(type)){
				stack.push(currentScope);
				currentScope = new Scope(currentScope, value+":"+linenumber);
				currentScope.setStart(linenumber);
				currentScope.setFile(file);
				return;
			}
			
			if (TokenType.RBRACE.equals(type)){
				currentScope.setEnd(linenumber);
				currentScope = stack.pop();
				return;
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
		var.setScope(currentScope);
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

