package com.googlecode.goclipse.go.lang.parser;

import java.io.File;
import java.io.IOException;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.go.lang.lexer.Lexer;
import com.googlecode.goclipse.go.lang.lexer.TokenListener;
import com.googlecode.goclipse.go.lang.lexer.TokenType;
import com.googlecode.goclipse.go.lang.lexer.Tokenizer;
import com.googlecode.goclipse.go.lang.model.Import;
import com.googlecode.goclipse.go.lang.model.Package;

public class PackageParser implements TokenListener{

	private enum State {
       START, CONSUME, FINISHED, ERROR
    }
	
	private State state = State.START;
	private Package pckg = new Package();

	public PackageParser(Tokenizer tokenizer) {
		tokenizer.addTokenListener(this);
	}

	@Override
	public void tokenFound(TokenType type, String value, boolean inComment, int linenumber, int start, int end) {
		
		if (type.isWhiteSpace() || inComment) {
           return;
        }
		
		switch(state){
		
		case START:
			if(TokenType.PACKAGE.equals(type)){
				state = State.CONSUME;
			}
			break;
		case CONSUME:
			if(TokenType.IDENTIFIER.equals(type)){
				pckg.setName(value);
				pckg.setLine(linenumber);
			}
			state = State.FINISHED;
			
		case FINISHED:
			break;
		}
	}

	/**
	 * 
	 * @return
	 */
	public Package getPckg() {
		return pckg;
	}

	/**
	 * 
	 * @param pckg
	 */
	public void setPckg(Package pckg) {
		this.pckg = pckg;
	}
	
	public static void main(String[] args) {

		Lexer lexer = new Lexer();
		Tokenizer tokenizer = new Tokenizer(lexer);
		PackageParser packageParser = new PackageParser(tokenizer);
		ImportParser importParser = new ImportParser(tokenizer);

		try {
			lexer.scan(new File("test_go/import_test.go"));
			Activator.logInfo(packageParser.pckg.getName()+" line="+packageParser.pckg.getLine());	
			for (Import imp : importParser.getImports()) {
				Activator.logInfo(	imp.prefix + " :: " + imp.path+ " :: " + imp.getLine());
			}
		} catch (IOException e) {
			Activator.logError(e);
		}
	}

	@Override
	public boolean isWhitespaceParser() {
		// TODO Auto-generated method stub
		return false;
	}
}
