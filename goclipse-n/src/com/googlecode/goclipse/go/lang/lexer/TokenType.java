/**
 * 
 */
package com.googlecode.goclipse.go.lang.lexer;

public enum TokenType{
   // 
   IDENTIFIER, 
   NUMBER,
	/*
	 * break default func interface select case defer go map struct chan else
	 * goto package switch const fallthrough if range type continue for import
	 * return var
	 * 
	 * 
	 */
   
   // BUILT-IN TYPES
   UINT8("uint8"),      // the set of all unsigned  8-bit integers (0 to 255)
   UINT16("uint16"),    // the set of all unsigned 16-bit integers (0 to 65535)
   UINT32("uint32"),    // the set of all unsigned 32-bit integers (0 to 4294967295)
   UINT64("uint64"),    // the set of all unsigned 64-bit integers (0 to 18446744073709551615)
   INT8("int8"),        // the set of all signed  8-bit integers (-128 to 127)
   INT16("int16"),      // the set of all signed 16-bit integers (-32768 to 32767)
   INT32("int32"),      // the set of all signed 32-bit integers (-2147483648 to 2147483647)
   INT64("int64"),      // the set of all signed 64-bit integers (-9223372036854775808 to 9223372036854775807)
   FLOAT32("float32"),  // the set of all IEEE-754 32-bit floating-point numbers
   FLOAT64("float64"),  // the set of all IEEE-754 64-bit floating-point numbers
   BYTE("byte"),        // familiar alias for uint8
   UINT("uint"),        // either 32 or 64 bits
   INT("int"),          // either 32 or 64 bits
   FLOAT("float"),      // either 32 or 64 bits
   UINTPTR("uintptr"),  // an unsigned integer large enough to store the uninterpreted bits of a pointer value
   STRING("string"),
   BOOL("bool"),
   
   // VALUES
   NIL("nil"),
   TRUE("true"),
   FALSE("false"),
   IOTA("iota"),
   
   // BUILTIN METHODS
   CAP("cap"), 
   CLOSE("close") , 
   CLOSED("closed"), 
   LEN("len"), 
   MAKE("make"), 
   NEW("new"), 
   PANIC("panic"), 
   PANICLN("panicln"), 
   PRINT("print"), 
   PRINTLN("println"),
   APPEND("append"),
   COPY("copy"),
   
   // KEYWORDS
   BREAK("break"),
   DEFAULT("default"),      
   FUNC("func"),         
   INTERFACE("interface"),    
   SELECT("select"),       
   CASE("case"),         
   DEFER("defer"),        
   GO("go"),           
   MAP("map"),          
   STRUCT("struct"),       
   CHAN("chan"),         
   ELSE("else"),         
   GOTO("goto"),         
   PACKAGE("package"),      
   SWITCH("switch"),       
   CONST("const"),        
   FALLTHROUGH("fallthrough"),  
   IF("if"),           
   RANGE("range"),        
   TYPE("type"),         
   CONTINUE("continue"),     
   FOR("for"),          
   IMPORT("import"),       
   RETURN("return"),       
   VAR("var"),
   
   // COMMENT
   COMMENT("//"),
   BLOCK_COMMENT_START("/*"),
   BLOCK_COMMENT_END("*/"),
   


   
   // DEFAULT already specified previously
   
   // RELATIONAL
   LESS("<"),
   GREATER(">"),
   GREATER_EQ(">="),
   LESS_EQ("<="),
   NOT_EQ("!="),
   DOUBLE_EQUAL("=="),
   ASSIGN(":="),
   
   // LOGIC OPERATORS
   AND("&&"), 
   OR("||"), 
   NOT("!"),
   
   // BITWISE OPERATORS
   BITWISE_XOR("^"), 
   BITWISE_AND("&"),
   BITWISE_OR("|"),
   SHIFT_LEFT("<<"),
   SHIFT_RIGHT(">>"),
   
   // UNARY OPERATORS
   INCREMENT("++"),
   DECREMENT("--"),
   
   // ASSIGNMENT
   EQUAL("="),
   ADD_ASSIGN("+="),
   SUBTRACT_ASSIGN("-="),
   MULTIPLY_ASSIGN("*="),
   DIVIDE_ASSIGN("/="),
   REMAINDER_ASSIGN("%="),
   BITWISE_AND_ASSIGN("&="),
   BITWISE_XOR_ASSIGN("^="),
   BITWISE_OR_ASSIGN("|="),
   SHIFT_LEFT_ASSIGN("<<="),
   SHIFT_RIGHT_ASSIGN(">>="),
   
   // OPERATORS
   ADD("+"), 
   SUBTRACT("-"), 
   MULTIPLY("*"), 
   DIVIDE("/"), 
   REMAINDER("%"),
   
   LPAREN("("),
   RPAREN(")"),
   DOLLAR("$"),
   COLON(":"),
   QUESTION("?"),
   LBRACE("{"),
   RBRACE("}"),
   LBRACKET("["),
   RBRACKET("]"),
   BACKSLASH("\\"),
   QUOTE("\""),
   APOSTROPHE("'"),
   UNDERSCORE("_"),
   AT("@"),
   POUND("#"),
   COMMA(","),
   PERIOD("."),
   SEMICOLON(";"),
   
   // NON_TOKENS
   TILDA("~"),
   GRAVE_ACCENT("`"),   
   
   // WHITESPACE
   SPACE(" "),
   TAB("\t"),
   NEWLINE("\n"),
   FORMFEEDLINE("\f"),
   RETURNLINE("\r"),
   BACKSPACE("\b");
   
   String op;
   
   TokenType(String val){
      op = val;
   }
   
   TokenType(){
      op = "";
   }
   
   public boolean isWhiteSpace(){
      if(this.equals(SPACE)||this.equals(TAB)||this.equals(NEWLINE)||
         this.equals(FORMFEEDLINE)||this.equals(RETURNLINE)||this.equals(BACKSPACE)){
         return true;
      }
      
      return false;
   }
   
   public boolean equals(String str){
      if(op.equals(str)){
         return true;
      }
      
      return false;
   }
}