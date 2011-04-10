package com.googlecode.goclipse.go.lang.parser;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.go.lang.lexer.Lexer;
import com.googlecode.goclipse.go.lang.lexer.TokenUnit;
import com.googlecode.goclipse.go.lang.lexer.Tokenizer;
import com.googlecode.goclipse.model.Function;
import com.googlecode.goclipse.model.Import;
import com.googlecode.goclipse.model.Interface;
import com.googlecode.goclipse.model.Method;
import com.googlecode.goclipse.model.Node;
import com.googlecode.goclipse.model.Package;
import com.googlecode.goclipse.model.Type;
import com.googlecode.goclipse.preferences.PreferenceConstants;

/**
 * Used to provide code completions and navigation through the code base
 * 
 * @author steel
 */
public class CodeContext {
	
	static HashMap<String, CodeContext> externalContexts = new HashMap<String, CodeContext>();

	public String               name;
	public String               filetext;
	public Package              pkg;
	public TokenizedPage        page;
	public ArrayList<Import>    imports    = new ArrayList<Import>   ();
	public ArrayList<Method>    methods    = new ArrayList<Method>   ();
	public ArrayList<Function>  functions  = new ArrayList<Function> ();
	public ArrayList<Interface> interfaces = new ArrayList<Interface>();
	
	/**
	 * 
	 * @param filename
	 * @param fileText
	 * @throws IOException
	 */
	public CodeContext(String name) throws IOException{
		this.name = name;
	}
	
	/**
	 * 
	 * @param filename
	 * @param fileText
	 * @return
	 * @throws IOException
	 * @throws RecognitionException
	 */
	public static CodeContext getCodeContext(String filename, String fileText) throws IOException{
		
		CodeContext     codeContext     = new CodeContext(filename);
		Lexer     	    lexer  		    = new Lexer();
		Tokenizer 	    tokenizer 	    = new Tokenizer(lexer);
		PackageParser   packageParser   = new PackageParser(tokenizer);
		ImportParser    importParser    = new ImportParser(tokenizer);
		FunctionParser  functionParser  = new FunctionParser(false, tokenizer);
		TypeParser      typeParser      = new TypeParser(false, tokenizer);
//		InterfaceParser interfaceParser = new InterfaceParser(tokenizer);
		
		lexer.scan(fileText);
		
		codeContext.page 	   = new TokenizedPage(tokenizer.getTokenizedStream());		
		codeContext.pkg 	   = packageParser.getPckg();
		codeContext.imports    = importParser.getImports();
		codeContext.methods    = functionParser.getMethods();
		codeContext.functions  = functionParser.getFunctions();
//		codeContext.interfaces = interfaceParser.getFunctions();
		
		for(Import imp:codeContext.imports){
			CodeContext context = externalContexts.get(imp.path);
			if(context==null){
				context = getExternalCodeContext(imp.path);
				externalContexts.put(imp.path, context);
			}
			
			codeContext.methods.addAll(context.methods);
			codeContext.functions.addAll(context.functions);
		}
		
		for(Type type:typeParser.getTypes()){
			System.out.println(">> "+type.getName());
		}
		
		return codeContext;
	}
	
	/**
	 * 
	 * @param filename
	 * @param fileText
	 * @return
	 * @throws IOException
	 * @throws RecognitionException
	 */
	public static CodeContext getExternalCodeContext(String packagePath) throws IOException{
		
		CodeContext     codeContext     = new CodeContext(packagePath);
		Lexer     	    lexer  		    = new Lexer();
		Tokenizer 	    tokenizer 	    = new Tokenizer(lexer);
		PackageParser   packageParser   = new PackageParser(tokenizer);
		FunctionParser  functionParser  = new FunctionParser(true, tokenizer);
		TypeParser      typeParser      = new TypeParser(true, tokenizer);
//		InterfaceParser interfaceParser = new InterfaceParser(tokenizer);
		
		// find path
		String goarch = Activator.getDefault().getPreferenceStore().getString(
				PreferenceConstants.GOARCH);
		
		String goos = Activator.getDefault().getPreferenceStore().getString(
				PreferenceConstants.GOOS);
		
		String goroot = Activator.getDefault().getPreferenceStore().getString(
				PreferenceConstants.GOROOT);
		
		File pckdir = new File(goroot+"/src/pkg/"+packagePath);
		
		if(pckdir.exists()&&pckdir.isDirectory()){
			File[] files = pckdir.listFiles();
			for(File file:files){
				if(file.canRead() && file.getName().endsWith(".go") && !file.getName().contains("test")){
					System.out.println(">>> PARSING: "+file.getAbsolutePath());
					lexer.reset();
					lexer.scan(file);
				
					codeContext.pkg 	   = packageParser.getPckg();
					codeContext.methods    = functionParser.getMethods();
					codeContext.functions  = functionParser.getFunctions();
			//		codeContext.interfaces = interfaceParser.getFunctions();
				}
			}
		}
		
		return codeContext;
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
	public List<Node> getCompletionsForString(String line, String prefix, int linenumber){
		
		ArrayList<Node> arrayList = new ArrayList<Node>();
		
		// parse line into parts and determine context
		List<TokenUnit> lineTokens = page.getTokensForLine(linenumber);
		for(TokenUnit unit:lineTokens){
			System.out.println("-> "+unit.text+"     "+unit.tokenType);
		}
		
		arrayList.addAll(methods);
		
		arrayList.addAll(functions);
		
		return arrayList;
	}
	
	/**
	 * Declaration Only: 
	 * Outside of function
	 * 
	 * @param args
	 */
	public List<Node> getCompletionsForDeclarations(){
		return null;
	}
	
	/**
	 * Declaration Only: 
	 * Outside of function
	 * 
	 * @param args
	 */
	public List<Node> getCompletionsForFunction(){
		return null;
	}

	
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
			cc.getCompletionsForString("    var g", "g", 9);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

}
