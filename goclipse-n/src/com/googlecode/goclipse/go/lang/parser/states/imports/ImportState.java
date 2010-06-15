package com.googlecode.goclipse.go.lang.parser.states.imports;

import com.googlecode.goclipse.go.lang.lexer.TokenType;
import com.googlecode.goclipse.go.lang.parser.states.ParseException;
import com.googlecode.goclipse.go.lang.parser.states.ParserState;

public class ImportState implements ParserState{
   
   public static final ParserState INSTANCE = new ImportState();
   
   
   private ImportState(){
      
   }


   @Override
   public ParserState handle(TokenType type, String value, int start, int end) throws ParseException {
      if(TokenType.LPAREN.equals(type)){
         return LParenState.INSTANCE.handle(type, value, start, end);
      }
      else if(TokenType.QUOTE.equals(type)){
         return Quote1State.INSTANCE.handle(type, value, start, end);
      }
      else if(TokenType.IDENTIFIER.equals(type)){
         return AliasState.INSTANCE;
      }
      else{
         throw new ParseException(type, value, start, end);
      }
   }
   

}
