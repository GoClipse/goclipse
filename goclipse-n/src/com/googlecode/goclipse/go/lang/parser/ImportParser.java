package com.googlecode.goclipse.go.lang.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.googlecode.goclipse.SysUtils;
import com.googlecode.goclipse.go.lang.lexer.Lexer;
import com.googlecode.goclipse.go.lang.lexer.LexerListener;
import com.googlecode.goclipse.go.lang.lexer.TokenListener;
import com.googlecode.goclipse.go.lang.lexer.TokenType;
import com.googlecode.goclipse.go.lang.lexer.Tokenizer;
import com.googlecode.goclipse.model.Import;

/**
 * Receives a stream of tokens to determine the imports of a given source file
 * 
 * @author stanleysteel
 */
public class ImportParser implements TokenListener {

   private enum State {
      MULTIPLE, SINGLE, DETERMINING, IGNORING, ERROR
   }
   
   private enum PrefixType{
      NONE, ALIAS, INCLUDED, UNKNOWN
   }

   private State             previousState = State.IGNORING;
   private State             state         = State.IGNORING;
   private PrefixType        prefixType    = PrefixType.UNKNOWN;
   private ArrayList<Import> imports       = new ArrayList<Import>();
   private Import            currentImport = null;

   public ImportParser(Tokenizer tokenizer) {
      tokenizer.addTokenListener(this);
   }

   @Override
   public void tokenFound(TokenType type, String value, int start, int end) {

      /*
       * do not consider whitespace
       */
      if (type.isWhiteSpace()) {
         return;
      }

      switch (state) {
         case IGNORING:
            if (TokenType.IMPORT.equals(type)) {
               state = State.DETERMINING;
            }
            break;
         case DETERMINING:
            if (TokenType.LPAREN.equals(type)) {
               state = State.MULTIPLE;
            }
            else if (TokenType.LPAREN.equals(type)) {
               state = State.MULTIPLE;
            }
            else if (TokenType.QUOTE.equals(type)) {
               state = State.SINGLE;
               prefixType = PrefixType.NONE;
            }
            else if (TokenType.PERIOD.equals(type)) {
               prefixType = PrefixType.INCLUDED;
            }
            else if (TokenType.IDENTIFIER.equals(type)) {
               prefixType = PrefixType.ALIAS;
               testIdentifier(value);
            }
            break;
         case SINGLE:
            if (TokenType.IDENTIFIER.equals(type)) {
               testIdentifier(value);
            }
            else if(TokenType.DIVIDE.equals(type)){
               currentImport.path+=value;
            }
            else if(TokenType.SEMICOLON.equals(type)){
               currentImport = null;
               prefixType = PrefixType.UNKNOWN;
            }
            break;
         case MULTIPLE:
            if (TokenType.IDENTIFIER.equals(type)) {
               testIdentifier(value);
            }
            else if (TokenType.QUOTE.equals(type)) {
               if(prefixType==PrefixType.UNKNOWN){
                  prefixType = PrefixType.NONE;
               }
            }
            else if (TokenType.PERIOD.equals(type)) {
               if(prefixType==PrefixType.UNKNOWN){
                  prefixType= PrefixType.INCLUDED;
               }
            }
            else if(TokenType.DIVIDE.equals(type)){
               currentImport.path+=value;
            }
            else if(TokenType.SEMICOLON.equals(type)){
               currentImport = null;
               prefixType = PrefixType.UNKNOWN;
            }
            else if (TokenType.RPAREN.equals(type)) {
               state = State.IGNORING;
               currentImport = null;
               prefixType = PrefixType.UNKNOWN;
            }
            break;
      }
   }

   /**
    * @param value
    */
   private void testIdentifier(String value) {
      if(currentImport==null){
         currentImport = new Import();
         imports.add(currentImport);
         currentImport.prefix = value;
      }
      else{
         currentImport.path+=value;
      }
   }
   
   public static void main(String[] args) {
      
      Lexer lexer = new Lexer();
      Tokenizer tokenizer = new Tokenizer(lexer);
      ImportParser importParser = new ImportParser(tokenizer);
      try {
         lexer.addLexerListener(new LexerListener(){

            @Override
            public void tokenFound(TokenType type, String value, int start, int end) {
               System.out.print("\t["+type.name()+", "+value+" "+start+":"+end+" "+"]");
               System.out.println();
            }

            @Override
            public void newline(int lineNumber) {
               SysUtils.debug("LINE "+lineNumber+" ==========================");
            }
            
         });
         
         lexer.scan(new File("test_go/import_test.go"));
         
         for(Import imp:importParser.imports){
            SysUtils.debug(imp.path);
         }
      }
      catch (IOException e) {
         SysUtils.severe(e);
      }
   }
}
