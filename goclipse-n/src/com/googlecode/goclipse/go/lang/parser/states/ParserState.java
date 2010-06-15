package com.googlecode.goclipse.go.lang.parser.states;

import com.googlecode.goclipse.go.lang.lexer.TokenType;

public interface ParserState {

   ParserState handle(TokenType type, String value, int start, int end) throws ParseException;
}
