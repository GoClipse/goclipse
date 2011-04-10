package com.googlecode.goclipse.go.lang.lexer;

/**
 * 
 * @author steel
 */
public interface TokenListener {
   
	/**
	 * 
	 * @param type
	 * @param value
	 * @param inComment
	 * @param linenumber
	 * @param start
	 * @param end
	 */
   void tokenFound(TokenType type, String value, boolean inComment, int linenumber, int start, int end);
   
   /**
    * 
    * @return
    */
   boolean isWhitespaceParser();
}
