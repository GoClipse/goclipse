package com.googlecode.goclipse.go.lang.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.go.lang.lexer.Lexer;
import com.googlecode.goclipse.go.lang.lexer.TokenListener;
import com.googlecode.goclipse.go.lang.lexer.TokenType;
import com.googlecode.goclipse.go.lang.lexer.Tokenizer;
import com.googlecode.goclipse.go.lang.model.TypeClass;
import com.googlecode.goclipse.go.lang.model.Var;

/**
 * 
 * @author steel
 */
public class VariableParser implements TokenListener {

	private enum State {
		START, SIMPLE_VAR, SIMPLE_TYPE, FINISHED, ERROR
	}
	
	private ArrayList<Var>   vars              = new ArrayList<Var>();
	private State            state 			   = State.START;
	private TokenType        previousTokenType = TokenType.NEWLINE;
	private String           previousTextValue = "\n";
	private Var              var 			   = new Var();
	private StringBuffer     comment 		   = new StringBuffer();
	private int 			 lastCommentLine   = 0;
	private FunctionParser   functionParser    = null;
	
	/**
	 * @param tokenizer
	 */
	public VariableParser(Tokenizer tokenizer, FunctionParser functionParser) {
		tokenizer.addTokenListener(this);
		this.functionParser = functionParser;
	}
	
	@Override
	public void tokenFound(TokenType type, String value, boolean inComment,
			int linenumber, int start, int end) {
		
		if (inComment) {
			if (!TokenType.COMMENT.equals(type)
					&& !TokenType.BLOCK_COMMENT_START.equals(type)
					&& !TokenType.BLOCK_COMMENT_END.equals(type)) {

				if (linenumber > lastCommentLine
						&& TokenType.DIVIDE.equals(type)) {
					lastCommentLine = linenumber;
				} 
				else {
					comment.append(value);
					lastCommentLine = linenumber;
				}
			}
			return;
		}
		
		if (linenumber - lastCommentLine > 1) {
			comment = new StringBuffer();
		}
		
		if (!inComment) {
			
			switch (state) {
			
			case START:
				if (TokenType.VAR.equals(type)) {
					state = State.SIMPLE_VAR;
				}
				break;			
			
			case SIMPLE_VAR:
				if(TokenType.IDENTIFIER.equals(type)){
					var.setName(value);
					var.setInsertionText(value);
					var.setDocumentation(comment.toString());
					var.setLine(linenumber);
					state = State.SIMPLE_TYPE;
				}
				break;
				
			case SIMPLE_TYPE:
				
				boolean found = false;
				
				// this isn't very clean, I'll have to come back a rethink all this
				// is TypeClass enough???
				if(TokenType.IDENTIFIER.equals(type))    {var.setTypeClass(TypeClass.USER);    var.setName(var.getName()+" : "+value);          found = true;}
				else if(TokenType.UINT.equals(type))	 {var.setTypeClass(TypeClass.UINT);    var.setName(var.getName()+" : "+type.getText()); found = true;}
				else if(TokenType.UINT8.equals(type))	 {var.setTypeClass(TypeClass.UINT8);   var.setName(var.getName()+" : "+type.getText()); found = true;}
				else if(TokenType.UINT16.equals(type))	 {var.setTypeClass(TypeClass.UINT16);  var.setName(var.getName()+" : "+type.getText()); found = true;}
				else if(TokenType.UINT32.equals(type))	 {var.setTypeClass(TypeClass.UINT32);  var.setName(var.getName()+" : "+type.getText()); found = true;}
				else if(TokenType.UINT64.equals(type))	 {var.setTypeClass(TypeClass.UINT64);  var.setName(var.getName()+" : "+type.getText()); found = true;}
				else if(TokenType.INT.equals(type))	     {var.setTypeClass(TypeClass.INT);     var.setName(var.getName()+" : "+type.getText()); found = true;}
				else if(TokenType.INT8.equals(type))	 {var.setTypeClass(TypeClass.INT8);    var.setName(var.getName()+" : "+type.getText()); found = true;}
				else if(TokenType.INT16.equals(type))	 {var.setTypeClass(TypeClass.INT16);   var.setName(var.getName()+" : "+type.getText()); found = true;}
				else if(TokenType.INT32.equals(type))	 {var.setTypeClass(TypeClass.INT32);   var.setName(var.getName()+" : "+type.getText()); found = true;}
				else if(TokenType.INT64.equals(type))	 {var.setTypeClass(TypeClass.INT64);   var.setName(var.getName()+" : "+type.getText()); found = true;}
				else if(TokenType.FLOAT32.equals(type))  {var.setTypeClass(TypeClass.FLOAT32); var.setName(var.getName()+" : "+type.getText()); found = true;}
				else if(TokenType.FLOAT64.equals(type))  {var.setTypeClass(TypeClass.FLOAT64); var.setName(var.getName()+" : "+type.getText()); found = true;}
				else if(TokenType.BYTE.equals(type))	 {var.setTypeClass(TypeClass.BYTE);    var.setName(var.getName()+" : "+type.getText()); found = true;}
				else if(TokenType.UINTPTR.equals(type))  {var.setTypeClass(TypeClass.UINTPTR); var.setName(var.getName()+" : "+type.getText()); found = true;}
				else if(TokenType.STRING.equals(type))   {var.setTypeClass(TypeClass.STRING);  var.setName(var.getName()+" : "+type.getText()); found = true;}
				else if(TokenType.BOOL.equals(type))	 {var.setTypeClass(TypeClass.BOOL);    var.setName(var.getName()+" : "+type.getText()); found = true;}
				else if(TokenType.CHAN.equals(type))	 {var.setTypeClass(TypeClass.CHAN);    var.setChan(true); found = true;}
				else if(TokenType.MAP.equals(type))      {var.setTypeClass(TypeClass.MAP);     var.setMap(true);  found = true;}
				else if(TokenType.LBRACKET.equals(type)) {var.setTypeClass(TypeClass.ARRAY);   var.setArray(true);found = false;}
				else if(TokenType.MULTIPLY.equals(type)) {var.setPointer(true);                                   found = false;}
				
				if(found){
					state = State.START;
					vars.add(var);
					var = new Var();
					comment = new StringBuffer();
				}
				
				break;
			}
			
		}
		
		
		previousTokenType = type;
		previousTextValue = value;
	}
	
	@Override
	public boolean isWhitespaceParser() {
		return false;
	}
	
	
	/**
	 * @return the vars
	 */
	public ArrayList<Var> getVars() {
		return vars;
	}

	/**
	 * @param vars the vars to set
	 */
	public void setVars(ArrayList<Var> vars) {
		this.vars = vars;
	}

	public static void main(String[] args) {

		Lexer lexer = new Lexer();
		Tokenizer tokenizer = new Tokenizer(lexer);
		VariableParser fparser = new VariableParser(tokenizer, null);

		try {
			lexer.scan(new File("test_go/import_test.go"));
			for (Var var : fparser.vars) {
				Activator.logInfo("=================================================");
				Activator.logInfo(var.getDocumentation());
				Activator.logInfo("-------------------------------------------------");
				Activator.logInfo(var.getName());
				Activator.logInfo(var.getInsertionText());
				Activator.logInfo("-------------------------------------------------");
			}			

		} catch (IOException e) {
			Activator.logError(e);
		}
	}
}
