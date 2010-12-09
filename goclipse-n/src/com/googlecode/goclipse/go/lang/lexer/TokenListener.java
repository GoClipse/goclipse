package com.googlecode.goclipse.go.lang.lexer;

public interface TokenListener {
   
   void tokenFound(TokenType type, String value, boolean inComment, int linenumber, int start, int end);

}
