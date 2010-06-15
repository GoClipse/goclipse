package com.googlecode.goclipse.go.lang.parser.states.imports;

import com.googlecode.goclipse.go.lang.lexer.TokenType;
import com.googlecode.goclipse.go.lang.parser.states.ParseException;
import com.googlecode.goclipse.go.lang.parser.states.ParserState;

public class LParenState implements ParserState {

   public static final ParserState INSTANCE = new LParenState();
   
   private LParenState(){
      
   }
   
   
   @Override
   public ParserState handle(TokenType type, String value, int start, int end) throws ParseException {
      
      return null;
   }

}
