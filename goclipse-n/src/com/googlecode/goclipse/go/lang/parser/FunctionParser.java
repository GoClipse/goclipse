package com.googlecode.goclipse.go.lang.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.googlecode.goclipse.SysUtils;
import com.googlecode.goclipse.go.lang.lexer.Lexer;
import com.googlecode.goclipse.go.lang.lexer.TokenListener;
import com.googlecode.goclipse.go.lang.lexer.TokenType;
import com.googlecode.goclipse.go.lang.lexer.Tokenizer;
import com.googlecode.goclipse.model.Function;

public class FunctionParser implements TokenListener{

	private enum State {
       START, CONSUME, FINISHED, ERROR
    }
	
	private State state = State.START;
	
	private ArrayList<Function> funcs = new ArrayList<Function>();
	private Function func = new Function();
	private StringBuffer comment = new StringBuffer();
	private StringBuffer functext = new StringBuffer();
	private int lastCommentLine = 0;

	public FunctionParser(Tokenizer tokenizer) {
		tokenizer.addTokenListener(this);
	}

	@Override
	public void tokenFound(TokenType type, String value, boolean inComment, int linenumber, int start, int end) {
		
		if (inComment) {
		   if(!TokenType.COMMENT.equals(type) && 
			  !TokenType.BLOCK_COMMENT_START.equals(type) && 
			  !TokenType.BLOCK_COMMENT_END.equals(type)){
			   
			   if(linenumber-lastCommentLine>1){
				   comment = new StringBuffer();
			   }
			   
			   comment.append(value);
			   lastCommentLine = linenumber;
		   }
           return;
        }
		
		switch(state){
		
		case START:
			if(TokenType.FUNC.equals(type)){
				state = State.CONSUME;
			}
			break;
		case CONSUME:
			if(TokenType.IDENTIFIER.equals(type)){
				functext.append(value);
				func.setLine(linenumber);
			}
			else if(TokenType.LBRACE.equals(type)){
				func.setName(functext.toString());
				func.setDocumentation(comment.toString());
				functext = new StringBuffer();
				funcs.add(func);
				func = new Function();
				comment = new StringBuffer();
				state = State.START;
			}
			else {
				functext.append(value);
			}
			
		case FINISHED:
			break;
		}
	}
	
	public static void main(String[] args) {

		Lexer lexer = new Lexer();
		Tokenizer tokenizer = new Tokenizer(lexer);
		FunctionParser fparser = new FunctionParser(tokenizer);

		try {
			lexer.scan(new File("test_go/import_test.go"));
			for(Function func:fparser.funcs){
				SysUtils.debug("-------------------------------------------------");
				SysUtils.debug(func.getDocumentation());
				SysUtils.debug("-------------------------------------------------");
				SysUtils.debug(func.getName());
				SysUtils.debug("-------------------------------------------------");
			}
			
		} catch (IOException e) {
			SysUtils.severe(e);
		}
	}
}
