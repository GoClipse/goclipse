package com.googlecode.goclipse.go.lang.lexer;

import java.util.ArrayList;

/**
 * Provides more specific classification of a given token and passes it on to
 * listeners for further analysis and processing.
 * 
 * @author stanleysteel
 */
public class Tokenizer implements LexerListener {

   private ArrayList<TokenListener> listeners = new ArrayList<TokenListener>();
   private int linenumber = 0;
   
   public Tokenizer(Lexer lexer) {
      lexer.addLexerListener(this);
   }

   
   /**
    * 
    */
   public void addTokenListener(TokenListener l){
      if(!listeners.contains(l)){
         listeners.add(l);
      }
   }
   
   /**
    * 
    */
   public void removeTokenListsner(TokenListener l){
      listeners.remove(l);
   }
   
   @Override
   public void newline(int lineNumber) {
      this.linenumber = lineNumber;
   }

   /**
    * Further classifies the token to a more meaningful token type
    */
   @Override
   public void tokenFound(TokenType type, String value, boolean inComment, int start, int end) {
	  
      if (TokenType.IDENTIFIER == type) {
         test();
      }

      switch (type) {
         case IDENTIFIER:
            // BUILT-IN TYPES
            if (TokenType.UINT8.equals(value)) {
               type = TokenType.UINT8;
            }
            else if (TokenType.UINT16.equals(value)) {
               type = TokenType.UINT16;
            }
            else if (TokenType.UINT32.equals(value)) {
               type = TokenType.UINT32;
            }
            else if (TokenType.UINT64.equals(value)) {
               type = TokenType.UINT64;
            }
            else if (TokenType.INT8.equals(value)) {
               type = TokenType.INT8;
            }
            else if (TokenType.INT16.equals(value)) {
               type = TokenType.INT16;
            }
            else if (TokenType.INT32.equals(value)) {
               type = TokenType.INT32;
            }
            else if (TokenType.INT64.equals(value)) {
               type = TokenType.INT64;
            }
            else if (TokenType.FLOAT32.equals(value)) {
               type = TokenType.FLOAT32;
            }
            else if (TokenType.FLOAT64.equals(value)) {
               type = TokenType.FLOAT64;
            }
            else if (TokenType.BYTE.equals(value)) {
               type = TokenType.BYTE;
            }
            else if (TokenType.UINT.equals(value)) {
               type = TokenType.UINT;
            }
            else if (TokenType.INT.equals(value)) {
               type = TokenType.INT;
            }
            else if (TokenType.FLOAT.equals(value)) {
               type = TokenType.FLOAT;
            }
            else if (TokenType.UINTPTR.equals(value)) {
               type = TokenType.UINTPTR;
            }
            else if (TokenType.STRING.equals(value)) {
               type = TokenType.STRING;
            }
            else if (TokenType.BOOL.equals(value)) {
               type = TokenType.BOOL;
            }

            // VALUES
            else if (TokenType.NIL.equals(value)) {
               type = TokenType.NIL;
            }
            else if (TokenType.TRUE.equals(value)) {
               type = TokenType.TRUE;
            }
            else if (TokenType.FALSE.equals(value)) {
               type = TokenType.FALSE;
            }
            else if (TokenType.IOTA.equals(value)) {
               type = TokenType.IOTA;
            }

            // BUILTIN METHODS
            else if (TokenType.CAP.equals(value)) {
               type = TokenType.CAP;
            }
            else if (TokenType.CLOSE.equals(value)) {
               type = TokenType.CLOSE;
            }
            else if (TokenType.CLOSED.equals(value)) {
               type = TokenType.CLOSED;
            }
            else if (TokenType.LEN.equals(value)) {
               type = TokenType.LEN;
            }
            else if (TokenType.MAKE.equals(value)) {
               type = TokenType.MAKE;
            }
            else if (TokenType.NEW.equals(value)) {
               type = TokenType.NEW;
            }
            else if (TokenType.PANIC.equals(value)) {
               type = TokenType.NEW;
            }
            else if (TokenType.PANICLN.equals(value)) {
               type = TokenType.PANICLN;
            }
            else if (TokenType.PRINT.equals(value)) {
               type = TokenType.PRINT;
            }
            else if (TokenType.PRINTLN.equals(value)) {
               type = TokenType.PRINTLN;
            }

            // KEYWORDS
            else if (TokenType.BREAK.equals(value)) {
               type = TokenType.BREAK;
            }
            else if (TokenType.DEFAULT.equals(value)) {
               type = TokenType.DEFAULT;
            }
            else if (TokenType.FUNC.equals(value)) {
               type = TokenType.FUNC;
            }
            else if (TokenType.INTERFACE.equals(value)) {
               type = TokenType.INTERFACE;
            }
            else if (TokenType.SELECT.equals(value)) {
               type = TokenType.SELECT;
            }
            else if (TokenType.CASE.equals(value)) {
               type = TokenType.CASE;
            }
            else if (TokenType.DEFER.equals(value)) {
               type = TokenType.DEFER;
            }
            else if (TokenType.GO.equals(value)) {
               type = TokenType.GO;
            }
            else if (TokenType.MAP.equals(value)) {
               type = TokenType.MAP;
            }
            else if (TokenType.STRUCT.equals(value)) {
               type = TokenType.STRUCT;
            }
            else if (TokenType.CHAN.equals(value)) {
               type = TokenType.CHAN;
            }
            else if (TokenType.ELSE.equals(value)) {
               type = TokenType.ELSE;
            }
            else if (TokenType.GOTO.equals(value)) {
               type = TokenType.GOTO;
            }
            else if (TokenType.PACKAGE.equals(value)) {
               type = TokenType.PACKAGE;
            }
            else if (TokenType.SWITCH.equals(value)) {
               type = TokenType.SWITCH;
            }
            else if (TokenType.CONST.equals(value)) {
               type = TokenType.CONST;
            }
            else if (TokenType.FALLTHROUGH.equals(value)) {
               type = TokenType.FALLTHROUGH;
            }
            else if (TokenType.IF.equals(value)) {
               type = TokenType.IF;
            }
            else if (TokenType.RANGE.equals(value)) {
               type = TokenType.RANGE;
            }
            else if (TokenType.TYPE.equals(value)) {
               type = TokenType.TYPE;
            }
            else if (TokenType.CONTINUE.equals(value)) {
               type = TokenType.CONTINUE;
            }
            else if (TokenType.FOR.equals(value)) {
               type = TokenType.FOR;
            }
            else if (TokenType.IMPORT.equals(value)) {
               type = TokenType.IMPORT;
            }
            else if (TokenType.RETURN.equals(value)) {
               type = TokenType.RETURN;
            }
            else if (TokenType.VAR.equals(value)) {
               type = TokenType.VAR;
            }
            else if (TokenType.APPEND.equals(value)) {
               type = TokenType.APPEND;
            }
            else if (TokenType.COPY.equals(value)) {
               type = TokenType.COPY;
            }
            break;
      }
      
      for(TokenListener listener:listeners){
         listener.tokenFound(type, value, inComment, linenumber, start, end);
      }

   }

   boolean test() {
      return false;
   }
}
