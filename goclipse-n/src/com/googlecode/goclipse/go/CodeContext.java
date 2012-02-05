package com.googlecode.goclipse.go;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.go.lang.lexer.Lexer;
import com.googlecode.goclipse.go.lang.lexer.TokenUnit;
import com.googlecode.goclipse.go.lang.lexer.Tokenizer;
import com.googlecode.goclipse.go.lang.model.Function;
import com.googlecode.goclipse.go.lang.model.Import;
import com.googlecode.goclipse.go.lang.model.Method;
import com.googlecode.goclipse.go.lang.model.Node;
import com.googlecode.goclipse.go.lang.model.Package;
import com.googlecode.goclipse.go.lang.model.Scope;
import com.googlecode.goclipse.go.lang.model.Type;
import com.googlecode.goclipse.go.lang.model.Var;
import com.googlecode.goclipse.go.lang.parser.FunctionParser;
import com.googlecode.goclipse.go.lang.parser.ImportParser;
import com.googlecode.goclipse.go.lang.parser.PackageParser;
import com.googlecode.goclipse.go.lang.parser.ScopeParser;
import com.googlecode.goclipse.go.lang.parser.TokenizedPage;
import com.googlecode.goclipse.go.lang.parser.TypeParser;
import com.googlecode.goclipse.go.lang.parser.VariableParser;
import com.googlecode.goclipse.preferences.PreferenceConstants;

/**
 * Used to provide code completions and navigation through the code base
 * 
 * @author steel
 */
@SuppressWarnings("unused")
public class CodeContext {
	
	static HashMap<String, CodeContext> externalContexts = new HashMap<String, CodeContext>();

	public String              name;
	public String              filetext;
	public Package             pkg;
	public TokenizedPage       page;
	public ArrayList<Import>   imports     = new ArrayList<Import>   ();
	public ArrayList<Method>   methods     = new ArrayList<Method>   ();
	public ArrayList<Function> functions   = new ArrayList<Function> ();
	public ArrayList<Type>     types       = new ArrayList<Type>     ();
	public ArrayList<Var>      vars        = new ArrayList<Var>      ();
	public ArrayList<Scope>    moduleScope = new ArrayList<Scope>    ();
	
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
		return getCodeContext(filename, fileText, true);
	}
	
	/**
	 * Static factory method that builds the code context.  Should be renamed.
	 * 
	 * @param filename
	 * @param fileText
	 * @param useExternalContext
	 * @return
	 * @throws IOException
	 */
	public static CodeContext getCodeContext(String filename, String fileText, boolean useExternalContext) throws IOException{
		CodeContext     codeContext     = new CodeContext(filename);
		Lexer     	    lexer  		    = new Lexer();
		Tokenizer 	    tokenizer 	    = new Tokenizer(lexer);
		PackageParser   packageParser   = new PackageParser(tokenizer);
		ImportParser    importParser    = new ImportParser(tokenizer);
		ScopeParser     scopeParser     = new ScopeParser(tokenizer);
		FunctionParser  functionParser  = new FunctionParser(false, tokenizer);
		functionParser.setScopeParser(scopeParser);
		
		TypeParser      typeParser      = new TypeParser(false, tokenizer);
		typeParser.setScopeParser(scopeParser);
		
		VariableParser  variableParser  = new VariableParser(tokenizer, functionParser);
		variableParser.setScopeParser(scopeParser);
		
//		InterfaceParser interfaceParser = new InterfaceParser(tokenizer);
		
		lexer.scan(fileText);
		
		codeContext.page 	   = new TokenizedPage(tokenizer.getTokenizedStream());
		codeContext.pkg 	   = packageParser.getPckg();
		codeContext.imports    = importParser.getImports();
		codeContext.methods    = functionParser.getMethods();
		codeContext.functions  = functionParser.getFunctions();
		codeContext.types	   = typeParser.getTypes();
		codeContext.vars       = variableParser.getVars();
		//scopeParser.print();
//		codeContext.interfaces = interfaceParser.getFunctions();
		
		// INFO: not for production
		//scopeParser.getRootScope().print("");
		
		if (useExternalContext) {
			for(Import imp:codeContext.imports){
				CodeContext context = externalContexts.get(imp.path);
				if(context==null){
					context = getExternalCodeContext(imp.path);
					externalContexts.put(imp.path, context);
				}
				
				codeContext.methods.addAll(context.methods);
				codeContext.functions.addAll(context.functions);
			}
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
		
		File pkgdir = new File(goroot+"/src/pkg/"+packagePath);
		
		if(pkgdir.exists() && pkgdir.isDirectory()){
			File[] files = pkgdir.listFiles();
			for(File file:files){
				if(file.canRead() && file.getName().endsWith(".go") && !file.getName().contains("test")){
					
					lexer.reset();
					lexer.scan(file);
				
					codeContext.pkg 	   = packageParser.getPckg();
					codeContext.methods    = functionParser.getMethods();
					codeContext.functions  = functionParser.getFunctions();
			//		codeContext.interfaces = interfaceParser.getFunctions();
				}
			}
		}
		else{
			String path = Environment.INSTANCE.getAbsoluteProjectPath();
			pkgdir = new File(path+"/src/pkg/"+packagePath);
			if(pkgdir.exists() && pkgdir.isDirectory()){
				File[] files = pkgdir.listFiles();
				for(File file:files){
					if(file.canRead() && file.getName().endsWith(".go") && !file.getName().contains("test")){
						lexer.reset();
						lexer.scan(file);
					
						codeContext.pkg 	   = packageParser.getPckg();
						codeContext.methods    = functionParser.getMethods();
						codeContext.functions  = functionParser.getFunctions();
				//		codeContext.interfaces = interfaceParser.getFunctions();
					}
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
		}
		
		arrayList.addAll(methods);
		
		arrayList.addAll(functions);
		
		return arrayList;
	}
	
	/**
	 * 
	 * @param names
	 * @return
	 */
	public String getDescriptionForName(final String name){
		
		String n = name+"()";
		
		// slowest possible searches...
		for(Method method: methods){
			if(n.equals(method.getInsertionText())){
				return method.getDocumentation();
			}
		}
		
		for(Function function: functions){
			if(n.equals(function.getInsertionText())){
				return function.getDocumentation();
			}
		}
				
		return "";
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

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof CodeContext)) {
			return false;
		}
		
		CodeContext other = (CodeContext)obj;
		
		return name.equals(other.name);
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

	public boolean isMethodName(String identifier) {
		for(Method method: methods){
			if(method.getInsertionText().startsWith(identifier+"(")){
				return true;
			}
		}
		return false;
	}
	
	// need to use scope to determine this
//	public boolean isPackageVariableName(String identifier){
//		for(Var var: vars){
//			if(var.getInsertionText().startsWith(identifier)){
//				return true;
//			}
//		}
//		return false;
//	}
	

}
