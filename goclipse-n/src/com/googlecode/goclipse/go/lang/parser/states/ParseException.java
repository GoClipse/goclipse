package com.googlecode.goclipse.go.lang.parser.states;

import com.googlecode.goclipse.go.lang.lexer.TokenType;

public class ParseException extends Exception {
   /**
    * 
    */
   private static final long serialVersionUID = 1L;
   
   private TokenType type; 
   private String value;
   private int start;
   private int end;
   
   public ParseException(TokenType type, String value, int start, int end) {
      super();
      this.type = type;
      this.value = value;
      this.start = start;
      this.end = end;
   }

   /**
    * @return the type
    */
   public TokenType getType() {
      return type;
   }

   /**
    * @return the value
    */
   public String getValue() {
      return value;
   }

   /**
    * @return the start
    */
   public int getStart() {
      return start;
   }

   /**
    * @return the end
    */
   public int getEnd() {
      return end;
   }
   
   

}
