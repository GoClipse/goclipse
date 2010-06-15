package com.googlecode.goclipse.go.lang.lexer;


public interface LexerListener {

   void tokenFound(TokenType type, String value, int start, int end);
   void newline(int lineNumber);

}
