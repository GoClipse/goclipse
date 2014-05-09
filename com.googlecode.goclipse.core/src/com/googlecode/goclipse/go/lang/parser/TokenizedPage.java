package com.googlecode.goclipse.go.lang.parser;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.goclipse.go.lang.lexer.TokenType;
import com.googlecode.goclipse.go.lang.lexer.TokenUnit;

/**
 * 
 * @author steel
 */
public class TokenizedPage {
	
	ArrayList<ArrayList<TokenUnit>> page = 
		new ArrayList<ArrayList<TokenUnit>>();
	
	/**
	 * 
	 * @param tokens
	 */
	public TokenizedPage(List<TokenUnit> tokens) {
		ArrayList<TokenUnit> list = new ArrayList<TokenUnit>();
		
		for(TokenUnit unit:tokens){
			if(unit.tokenType.equals(TokenType.NEWLINE)){
				list.add(unit);
				page.add(list);
				list = new ArrayList<TokenUnit>();
			}
			else{
				list.add(unit);
			}
		}
	}
	
	/**
	 * 
	 * @param linenumber
	 * @return
	 */
	public List<TokenUnit> getTokensForLine(int linenumber){
		int zeroIndexedLineNumber = linenumber-1;
		if(page.size() > zeroIndexedLineNumber){
			return page.get(zeroIndexedLineNumber);
		}
		return new ArrayList<TokenUnit>();
	}

}
