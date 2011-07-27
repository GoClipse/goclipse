package com.googlecode.goclipse.go.lang.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.go.lang.lexer.Lexer;
import com.googlecode.goclipse.go.lang.lexer.TokenListener;
import com.googlecode.goclipse.go.lang.lexer.TokenType;
import com.googlecode.goclipse.go.lang.lexer.Tokenizer;
import com.googlecode.goclipse.model.Type;
import com.googlecode.goclipse.model.TypeClass;
import com.googlecode.goclipse.model.Var;

/**
 * 
 * @author steel
 */
public class TypeParser implements TokenListener {

	private enum State{START, CONSUME_NAME, DETERMINE, CONSUME_ALIAS, CONSUME_INTERFACE, CONSUME_STRUCT, FINISHED}

	private State 		    state 		     = State.START;
	private StringBuffer    comment          = new StringBuffer();
	private StringBuffer    text 	         = new StringBuffer();
	private ArrayList<Type> types            = new ArrayList<Type>();
	private int             lastCommentLine  = 0;
	private int             tokenOnLineCount = 0;
	private int             scope_tracker 	 = 0;
	private boolean         exportsOnly      = true;
	private Type            type 			 = new Type();
	private Var             var              = new Var();

	/**
	 * 
	 * @param tokenizer
	 */
	public TypeParser(boolean parseExportsOnly, Tokenizer tokenizer) {
		tokenizer.addTokenListener(this);
		exportsOnly = parseExportsOnly;
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<Type> getTypes(){
		return types;
	}
	
	/**
	 * @param tokenType
	 * @param value
	 * @param inComment
	 * @param linenumber
	 * @param start
	 * @param end
	 */
	public void tokenFound(TokenType tokenType, String value,
			boolean inComment, int linenumber, int start, int end) {

		if (inComment) {
			if (!TokenType.COMMENT.equals(tokenType)
					&& !TokenType.BLOCK_COMMENT_START.equals(tokenType)
					&& !TokenType.BLOCK_COMMENT_END.equals(tokenType)) {

				if (linenumber - lastCommentLine > 1) {
					comment = new StringBuffer();
				}

				if (linenumber > lastCommentLine
						&& TokenType.DIVIDE.equals(tokenType)) {
					lastCommentLine = linenumber;
				} else {
					comment.append(value);
					lastCommentLine = linenumber;
				}
			}
			return;
		}

		// Parsing top level functions only
		if (TokenType.LBRACE.equals(tokenType)) {
			scope_tracker++;
		}

		if (TokenType.RBRACE.equals(tokenType)) {
			scope_tracker--;
		}

		// guard against identifiers named 'func'
		if (!(tokenType.isWhiteSpace())) {
			tokenOnLineCount++;
		}
		else if (TokenType.NEWLINE.equals(tokenType)) {
			tokenOnLineCount = 0;
			return;
		}
		else if(tokenType.isWhiteSpace()){
			return;
		}

		switch (state) {
		case START:
			if (TokenType.TYPE.equals(tokenType) && scope_tracker == 0
					&& tokenOnLineCount == 1) {

				if (linenumber - lastCommentLine > 1) {
					comment = new StringBuffer();
				}
				
				type.setDocumentation(comment.toString());
				state = State.CONSUME_NAME;
			}
			break;

		case CONSUME_NAME:
			if (TokenType.IDENTIFIER.equals(tokenType)) {
				text.append(value);
				type.setInsertionText(value);
				state = State.DETERMINE;
				type.setLine(linenumber);
			}
			break;
				
		case DETERMINE:
			if(TokenType.STRUCT.equals(tokenType)){
				state = State.CONSUME_STRUCT;
				type.setTypeClass(TypeClass.STRUCT);
			}
			else if(TokenType.INTERFACE.equals(tokenType)){
				state = State.CONSUME_INTERFACE;
				type.setTypeClass(TypeClass.INTERFACE);
			}
			else if(TokenType.IDENTIFIER.equals(tokenType)){
				type.setTypeClass(TypeClass.USER);
			}
			else if(TokenType.UINT.equals(tokenType))	{type.setTypeClass(TypeClass.UINT);    text.append(": "+TokenType.UINT.getText());}
			else if(TokenType.UINT8.equals(tokenType))	{type.setTypeClass(TypeClass.UINT8);   text.append(": "+TokenType.UINT8.getText());}
			else if(TokenType.UINT16.equals(tokenType))	{type.setTypeClass(TypeClass.UINT16);  text.append(": "+TokenType.UINT16.getText());}
			else if(TokenType.UINT32.equals(tokenType))	{type.setTypeClass(TypeClass.UINT32);  text.append(": "+TokenType.UINT32.getText());}
			else if(TokenType.UINT64.equals(tokenType))	{type.setTypeClass(TypeClass.UINT64);  text.append(": "+TokenType.UINT64.getText());}
			else if(TokenType.INT.equals(tokenType))	{type.setTypeClass(TypeClass.INT);     text.append(": "+TokenType.INT.getText());}
			else if(TokenType.INT8.equals(tokenType))	{type.setTypeClass(TypeClass.INT8);    text.append(": "+TokenType.INT8.getText());}   
			else if(TokenType.INT16.equals(tokenType))	{type.setTypeClass(TypeClass.INT16);   text.append(": "+TokenType.INT16.getText());}   
			else if(TokenType.INT32.equals(tokenType))	{type.setTypeClass(TypeClass.INT32);   text.append(": "+TokenType.INT32.getText());}
			else if(TokenType.INT64.equals(tokenType))	{type.setTypeClass(TypeClass.INT64);   text.append(": "+TokenType.INT64.getText());}
			else if(TokenType.FLOAT32.equals(tokenType)){type.setTypeClass(TypeClass.FLOAT32); text.append(": "+TokenType.FLOAT32.getText());}
			else if(TokenType.FLOAT64.equals(tokenType)){type.setTypeClass(TypeClass.FLOAT64); text.append(": "+TokenType.FLOAT64.getText());}
			else if(TokenType.BYTE.equals(tokenType))	{type.setTypeClass(TypeClass.BYTE);    text.append(": "+TokenType.BYTE.getText());}
			else if(TokenType.UINTPTR.equals(tokenType)){type.setTypeClass(TypeClass.UINTPTR); text.append(": "+TokenType.UINTPTR.getText());}
			else if(TokenType.STRING.equals(tokenType)) {type.setTypeClass(TypeClass.STRING);  text.append(": "+TokenType.STRING.getText());}
			else if(TokenType.BOOL.equals(tokenType))	{type.setTypeClass(TypeClass.BOOL);    text.append(": "+TokenType.BOOL.getText());}
			else if(TokenType.CHAN.equals(tokenType))	{type.setTypeClass(TypeClass.CHAN);    text.append(": "+TokenType.CHAN.getText());}
			else if(TokenType.MAP.equals(tokenType))	{type.setTypeClass(TypeClass.MAP);     text.append(": "+TokenType.MAP.getText());}
			else {
				type.setTypeClass(TypeClass.UNKNOWN);
				type 		  = new Type();
				comment 	  = new StringBuffer();
				state 		  = State.START;
			}
			type.setName(text.toString());
			types.add(type);
			text          = new StringBuffer();
			type 		  = new Type();
			comment 	  = new StringBuffer();
			state 		  = State.START;
			break;
			
		case CONSUME_STRUCT:
			if(TokenType.RBRACE.equals(tokenType)){
				types.add(type);
				type 		  = new Type();
				comment 	  = new StringBuffer();
				state 		  = State.START;
			}
			else if(TokenType.LBRACE.equals(tokenType)){
				
			}
			else if(TokenType.IDENTIFIER.equals(tokenType)){
				var = new Var();
			}
			break;
			
		case CONSUME_INTERFACE:
			types.add(type);
			type 		  = new Type();
			comment 	  = new StringBuffer();
			state 		  = State.START;
			break;
		}

	}

	@Override
	public boolean isWhitespaceParser() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public static void main(String[] args) {

		Lexer      lexer     = new Lexer();
		Tokenizer  tokenizer = new Tokenizer(lexer);
		TypeParser fparser   = new TypeParser(false,tokenizer);

		try {
			lexer.scan(new File("test_go/import_test.go"));
			for(Type func:fparser.types){
				Activator.logInfo("=================================================");
				Activator.logInfo(func.getDocumentation());
				Activator.logInfo("-------------------------------------------------");
				Activator.logInfo(func.getName());
				Activator.logInfo(func.getInsertionText());
				Activator.logInfo("-------------------------------------------------");
			}
			
		} catch (IOException e) {
			Activator.logError(e);
		}
	}

}
