package com.googlecode.goclipse.go.antlr;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;

import com.googlecode.goclipse.go.antlr.GoSourceParser.program_return;
import com.googlecode.goclipse.go.antlr.Scope.Type;

/**
 * For each file, a code context should be built quickly and dynamically
 * 
 * @author steel
 */
public class CodeContext {
	
	
	public String filename;
	public ArrayList<Import> imports   = new ArrayList<Import>();
	public ArrayList<Scope>  scopes    = new ArrayList<Scope>();

	
	/**
	 * 
	 * @param filename
	 * @param fileText
	 * @throws IOException
	 */
	public CodeContext(String filename, String fileText) throws IOException{
		this.filename = filename;
		rebuildLocalOnly(fileText);
	}
	
	/**
	 * 
	 * @param filename
	 * @param fileText
	 * @return
	 * @throws IOException
	 * @throws RecognitionException
	 */
	public static CodeContext getCodeContext(String filename, String fileText) throws IOException, RecognitionException{
		CharStream     stream 		= new ANTLRStringStream(fileText);
		GoSourceLexer  lexer 		= new GoSourceLexer(stream);
		TokenStream    tokenStream  = new CommonTokenStream(lexer);
		GoSourceParser parser 		= new GoSourceParser(tokenStream);
		CodeContext    ctx          = new CodeContext(filename, "");
		parser.ctx       			= ctx;
		program_return program 		= parser.program();
		return ctx;
	}
	
	/**
	 * @return
	 */
	public boolean isInImportStatement(){
		return false;
	}
	
	/**
	 * @param line 
	 * @return
	 */
	public List<CodeUnit> getCompletionsForString(String str, int line){
		List<CodeUnit> codeUnits = new ArrayList<CodeUnit>();
		
		Scope scope = null;
		// find the correct scope
		boolean found = false;
		for(Scope s:scopes){
			if(line >= s.startLine && line <= s.stopLine){
				scope = s;
				found = true;
			}
			else if(found){
				break;
			}
		}
		
		// TODO determine prefic type here
		
		if(scope!=null){
			
			// add variables in alphabetical order
			Scope temp = scope;
			while(temp.parent!=null){
				if(temp.type == Type.FUNCTION){
					for(Var var:temp.variables){
						if(var.line<line){
							codeUnits.add(var);
						}
					}
				}
				else{
					codeUnits.addAll(temp.variables);
				}
				temp = temp.parent;
			}
			
			// add methods in alphabetical order
			temp = scope;
			while(temp.parent!=null){
				codeUnits.addAll(temp.methods);
				temp = temp.parent;
			}
			
			// add functions in alphabetical order
			temp = scope;
			while(temp.parent!=null){
				codeUnits.addAll(temp.functions);
				temp = temp.parent;
			}
		}
		
		return codeUnits;
	}
	
	/**
	 * @param fileText
	 * @throws IOException
	 */
	public void rebuildLocalOnly(String fileText) throws IOException{
		
	}
	
	/**
	 * 
	 */
	public void rebuildExternalOnly(){
	}
	
	/**
	 * 
	 */
	public void rebuildAll(){
		
	}
	
//	public List<ReferencialEntity> getTypeCompletions(String partial){
//		
//	}
//	
//	public List<ReferencialEntity> getPackageCompletions(String partial){
//		
//	}	
	
	public static void main(String[] args) {
		
		final String filename = "test_go/import_test.go";
		String str = "";
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}		
		
		try {
			while((str += reader.readLine())!=null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			CodeContext cc = getCodeContext(filename, str);
			cc.getCompletionsForString("f", 8);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RecognitionException e) {
			e.printStackTrace();
		}
	}
}
