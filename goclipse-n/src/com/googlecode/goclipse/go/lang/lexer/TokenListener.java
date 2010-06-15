package com.googlecode.goclipse.go.lang.lexer;

public interface TokenListener {
   
   void tokenFound(TokenType type, String value, int start, int end);

}
