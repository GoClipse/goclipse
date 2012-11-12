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
		START, SIMPLE_VAR, SIMPLE_TYPE, INFERENCE, FINISHED, ERROR,
	}
	
	private File              file              = null;
	private ScopeParser 	  scopeParser       = null;
	private ArrayList<Var>    vars              = new ArrayList<Var>();
	private State             state			    = State.START;
	//private TokenType        previousTokenType = TokenType.NEWLINE;
	//private String           previousTextValue = "\n";
	private Var               var 			    = new Var();
	private StringBuffer      comment 		    = new StringBuffer();
	private int 			  lastCommentLine   = 0;
	private FunctionParser    functionParser    = null;
	private ArrayList<String> previousIdentifier= new ArrayList<String>();
	private ArrayList<Var>    varBuffer         = new ArrayList<Var>();
	
	/**
	 * @param tokenizer
	 */
	public VariableParser(Tokenizer tokenizer, File file, FunctionParser functionParser) {
		tokenizer.addTokenListener(this);
		this.file = file;
		var.setFile(file);
		this.functionParser = functionParser;
	}
	
	/**
	 * @param scopeParser
	 */
	public void setScopeParser(ScopeParser scopeParser){
		this.scopeParser = scopeParser;
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
		
		handleVarBufferFlush(type);
		
		if (linenumber - lastCommentLine > 1) {
			comment = new StringBuffer();
		}
		
		//System.out.println(state);
		
		
		if (!inComment) {
			
			switch (state) {
			
			case START:
				if (TokenType.VAR.equals(type)) {
					state = State.SIMPLE_VAR;
				} else if((TokenType.INFERENCE.equals(type))){
					// TODO this inference parsing does not work
					//System.out.println("State Changed On: "+ type +":"+ value);
					state = State.INFERENCE;
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
					var.setLine(linenumber);
					scopeParser.addVariable(var);
					var = new Var();
					var.setFile(file);
					comment = new StringBuffer();
				}
				
				break;
				
			case INFERENCE:
				
				for(String s:previousIdentifier){
					if(var.getInsertionText()==null){
						var.setInsertionText(s);
					}
				
					state = State.START;
					vars.add(var);
					var.setLine(linenumber);
					scopeParser.addVariable(var);
					var = new Var();
					var.setFile(file);
					comment = new StringBuffer();
				}
				previousIdentifier.clear();
				break;
				
			}
			
		}
//		if(linenumber==43){
//			//System.out.println("");
//		}
		// keep track of the last identifier for inferenced variables
		if (TokenType.IDENTIFIER.equals(type)){
			previousIdentifier.add(value);
			
		} else if(!TokenType.COMMA.equals(type)&&
				  !TokenType.SPACE.equals(type)&&
				  !TokenType.NEWLINE.equals(type)&&
				  !TokenType.TAB.equals(type)&&
				  !TokenType.TYPE.equals(type)){
			
			previousIdentifier.clear();
		}
	}

	/**
	 * Handle the times we flush the buffer.  We do this so those
	 * times we declare a var within a statement like a for loop,
	 * the new var falls into the inner scope and not the outer.
	 * @param type
	 */
	private void handleVarBufferFlush(TokenType type) {
		switch (type) {
		case LBRACE:
		case RBRACE:
		case IF:
		case ELSE:
		case SWITCH:
		case FOR:
		case FUNC:
		case TYPE:
		case GO:
		case CASE:
		case DEFAULT:
			if (scopeParser!=null){
				for(Var var:varBuffer){
					scopeParser.addVariable(var);
				}
				varBuffer.clear();
			}
		}
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
		VariableParser fparser = new VariableParser(tokenizer, null, null);

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
