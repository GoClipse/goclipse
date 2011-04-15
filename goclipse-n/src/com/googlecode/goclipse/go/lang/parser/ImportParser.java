package com.googlecode.goclipse.go.lang.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.googlecode.goclipse.SysUtils;
import com.googlecode.goclipse.go.lang.lexer.Lexer;
import com.googlecode.goclipse.go.lang.lexer.LexerListener;
import com.googlecode.goclipse.go.lang.lexer.TokenListener;
import com.googlecode.goclipse.go.lang.lexer.TokenType;
import com.googlecode.goclipse.go.lang.lexer.Tokenizer;
import com.googlecode.goclipse.model.Import;
import com.googlecode.goclipse.model.Import.PrefixType;

/**
 * Receives a stream of tokens to determine the imports of a given source file
 * 
 * @author stanleysteel
 */
public class ImportParser implements TokenListener {

	private enum State {
		MULTIPLE, SINGLE, DETERMINING, IGNORING, ERROR
	}

	private State previousState 			= State.IGNORING;
	private State state 				    = State.IGNORING;
	private HashMap<Import, Import> imports = new HashMap<Import, Import>();
	private Import currentImport 			= null;
	private boolean reading 			    = false;

	public ImportParser(Tokenizer tokenizer) {
		tokenizer.addTokenListener(this);
	}

	@Override
	public void tokenFound(TokenType type, String value, boolean inComment,
			int linenumber, int start, int end) {

		try {
			/*
			 * do not consider whitespace
			 */
			if (type.isWhiteSpace() || inComment) {
				return;
			}

			switch (state) {
			case IGNORING:
				if (TokenType.IMPORT.equals(type)) {
					state = State.DETERMINING;
					currentImport = new Import();
					imports.put(currentImport, currentImport);
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
				if (TokenType.IDENTIFIER.equals(type)) {
					currentImport.path += value;
					currentImport.setLine(linenumber);
				} else if (TokenType.DIVIDE.equals(type)) {
					currentImport.path += value;
				} else if (TokenType.QUOTE.equals(type)) {
					currentImport = null;
					state = State.IGNORING;
				}
				break;
			case MULTIPLE:
				if (currentImport == null) {
					currentImport = new Import();
				}

				if (TokenType.IDENTIFIER.equals(type)) {

					if (currentImport.prefixType == Import.PrefixType.UNKNOWN) {
						currentImport.prefixType = Import.PrefixType.ALIAS;
						currentImport.prefix = value;
					} else {

						// a way to ensure it is only added once to import list
						if (currentImport.path.length() == 0) {
							imports.put(currentImport, currentImport);
						}
						
						currentImport.path += value;
						currentImport.setLine(linenumber);
					}
				} else if (TokenType.QUOTE.equals(type)) {
					reading = !reading;

					if (currentImport.prefixType == Import.PrefixType.UNKNOWN) {
						currentImport.prefixType = Import.PrefixType.NONE;
					}

					if (!reading) {
						currentImport = null;
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
			for(Import import1:imports.keySet()){
				if(import1.prefixType==PrefixType.NONE){
					String[] pathParts = import1.path.split("/");
					import1.prefix = pathParts[pathParts.length-1];
				}
			}
		} 
		catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the imports
	 */
	public ArrayList<Import> getImports() {
		return new ArrayList<Import>(imports.keySet());
	}

	/**
	 * @param imports
	 *            the imports to set
	 */
	public void setImports(ArrayList<Import> imports) {
		for(Import import1:imports){
			this.imports.put(import1, import1);
		}
	}

	public static void main(String[] args) {

		Lexer lexer = new Lexer();
		Tokenizer tokenizer = new Tokenizer(lexer);
		ImportParser importParser = new ImportParser(tokenizer);

		try {
			lexer.scan(new File("test_go/parser.go"));

			for (Import imp : importParser.imports.keySet()) {
				SysUtils.debug(imp.prefix + " :: " + imp.path + " :: "
						+ imp.getLine());
			}
		} catch (IOException e) {
			SysUtils.severe(e);
		}
	}

	@Override
	public boolean isWhitespaceParser() {
		return false;
	}
}
