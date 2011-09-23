package com.googlecode.goclipse.go.lang.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.goclipse.go.lang.lexer.Lexer;
import com.googlecode.goclipse.go.lang.lexer.TokenListener;
import com.googlecode.goclipse.go.lang.lexer.TokenType;
import com.googlecode.goclipse.go.lang.lexer.Tokenizer;
import com.googlecode.goclipse.go.lang.model.Import;
import com.googlecode.goclipse.go.lang.model.Import.PrefixType;

/**
 * Receives a stream of tokens to determine the imports of a given source file
 * 
 * @author stanleysteel
 */
public class ImportParser implements TokenListener {

	private enum State {
		MULTIPLE, SINGLE, DETERMINING, IGNORING, ERROR
	}

	private State 		 state 		   = State.IGNORING;
	private List<Import> imports       = new ArrayList<Import>();
	private Import 		 currentImport = null;
	private boolean 	 reading 	   = false;

	public ImportParser(Tokenizer tokenizer) {
		tokenizer.addTokenListener(this);
	}

	@Override
	public void tokenFound(TokenType type, String value, boolean inComment,	int linenumber, int start, int end) {
		// IGNORING --> DETERMINING --> SINGLE --> IGNORING
		// IGNORING --> DETERMINING --> MULTIPLE --> IGNORING
		
		try {
			// do not consider whitespace
			if (type.isWhiteSpace() || inComment) {
				return;
			}

			switch (state) {
			case IGNORING:
				if (TokenType.IMPORT.equals(type)) {
					state = State.DETERMINING;
					currentImport = new Import();
				}
				break;
			case DETERMINING:
				if (TokenType.LPAREN.equals(type)) {
					state = State.MULTIPLE;
				} 
				else if (TokenType.QUOTE.equals(type)) {
					state = State.SINGLE;
					currentImport.prefixType = Import.PrefixType.NONE;
				} 
				else if (TokenType.PERIOD.equals(type)) {
					if (currentImport != null) {
						currentImport.prefixType = Import.PrefixType.INCLUDED;
					}
				} 
				else if (TokenType.IDENTIFIER.equals(type)) {
					if (currentImport != null) {
						currentImport.prefixType = Import.PrefixType.ALIAS;
						currentImport.prefix = value;
					}
				}
				break;
			case SINGLE:
				if (TokenType.QUOTE.equals(type)) {
					imports.add(currentImport);
					currentImport = null;
					
					state = State.IGNORING;
				} else {
					currentImport.path += value;
					currentImport.setLine(linenumber);
				}
				break;
			case MULTIPLE:
				if (currentImport == null) {
					currentImport = new Import();
				}

				if (TokenType.QUOTE.equals(type)) {
					reading = !reading;

					if (currentImport.prefixType == Import.PrefixType.UNKNOWN) {
						currentImport.prefixType = Import.PrefixType.NONE;
					}

					if (!reading) {
						imports.add(currentImport);
						currentImport = null;
					}
				} else if (reading) {
					if (currentImport.prefixType == Import.PrefixType.UNKNOWN) {
						currentImport.prefixType = Import.PrefixType.ALIAS;
						currentImport.prefix = value;
					} else {
						currentImport.path += value;
						currentImport.setLine(linenumber);
					}
				} else if (TokenType.PERIOD.equals(type)) {
					if (currentImport.prefixType == Import.PrefixType.UNKNOWN) {
						currentImport.prefixType = Import.PrefixType.INCLUDED;
					}
				} else if (TokenType.DIVIDE.equals(type)) {
					currentImport.path += value;
				} else if (TokenType.SEMICOLON.equals(type)) {
					currentImport = null;
				} else if (TokenType.RPAREN.equals(type)) {
					state = State.IGNORING;
					currentImport = null;
				}
				break;
			}
			
			// post process
			for(Import import1:imports){
				if(import1.prefixType==PrefixType.NONE){
					String[] pathParts = import1.path.split("/");
					if (pathParts.length > 0) {
						import1.prefix = pathParts[pathParts.length-1];
					}
				}
			}
		} 
		catch (RuntimeException e) {
			//Activator.logError(e);
		}
	}

	/**
	 * @return the imports
	 */
	public ArrayList<Import> getImports() {
		return new ArrayList<Import>(imports);
	}

	@Override
	public boolean isWhitespaceParser() {
		return false;
	}
	
	public static void main(String[] args) {

		Lexer lexer = new Lexer();
		Tokenizer tokenizer = new Tokenizer(lexer);
		ImportParser importParser = new ImportParser(tokenizer);

		try {
			lexer.scan(new File("test/test_go/import_test.go"));

			for (Import imp : importParser.imports) {
				System.out.println(imp.prefix + " :: " + imp.path + " :: "
						+ imp.getLine());
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}

}
