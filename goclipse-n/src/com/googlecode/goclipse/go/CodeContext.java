package com.googlecode.goclipse.go;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.builder.GoConstants;
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

	static HashMap<String, CodeContext>	externalContexts = new HashMap<String, CodeContext>();

	public String	           name;
	public String	           filetext;
	public Package	           pkg;
	public TokenizedPage	   page;
	public ArrayList<Import>   imports	   = new ArrayList<Import>();
	public ArrayList<Method>   methods	   = new ArrayList<Method>();
	public ArrayList<Function> functions   = new ArrayList<Function>();
	public ArrayList<Type>	   types	   = new ArrayList<Type>();
	public ArrayList<Var>	   vars	       = new ArrayList<Var>();
	public ArrayList<Scope>	   moduleScope = new ArrayList<Scope>();

	/**
	 * @param  filename
	 * @param  fileText
	 * @throws IOException
	 */
	public CodeContext(String name) throws IOException {
		this.name = name;
	}

	/**
	 * @param  filename
	 * @param  fileText
	 * @return
	 * @throws IOException
	 * @throws RecognitionException
	 */
	public static CodeContext getCodeContext(String filename, String fileText) throws IOException {
		return getCodeContext(null, filename, fileText, true);
	}
	
	/**
	 * @param  filename
	 * @param  fileText
	 * @param useExternalContext
	 * @return
	 * @throws IOException
	 * @throws RecognitionException
	 */
	public static CodeContext getCodeContext(String filename, String fileText, boolean useExternalContext) throws IOException {
		return getCodeContext(null, filename, fileText, useExternalContext);
	}
	
	/**
	 * @param  project
	 * @param  filename
	 * @param  fileText
	 * @return
	 * @throws IOException
	 * @throws RecognitionException
	 */
	public static CodeContext getCodeContext(IProject project, String filename, String fileText) throws IOException {
		return getCodeContext(project, filename, fileText, true);
	}

	/**
	 * Static factory method that builds the code context. This is for
	 * non-test source files;
	 * 
	 * @param project
	 * @param filename
	 * @param fileText
	 * @param useExternalContext
	 * @return
	 * @throws IOException
	 */
	public static CodeContext getCodeContext(IProject project, String filename, String fileText, boolean useExternalContext)
	        throws IOException {
		
		boolean isCmdSrcFolder = false;
		
		if (project != null) {
			IResource res = project.findMember(filename);
			
			if(res!=null && res instanceof IFile &&
					Environment.INSTANCE.isCmdSrcFolder(project, (IFolder)res.getParent())){
				isCmdSrcFolder = true;
			}
		}
		
		CodeContext codeContext = new CodeContext(filename);

		File targetContext = new File(filename);
		if (!targetContext.exists()) {
			return codeContext;
		}

		File packageFolder = targetContext.getParentFile();
		
		parseText(fileText, false, useExternalContext, codeContext);

		//
		// Only look at the other files in the directory if the file
		// itself is not a singular main function source file.  Otherwise,
		// we could have the case where one main function source file could
		// pollute the code completion options of another the other main
		// source file.
		//
		if ( !isCmdSrcFolder ) {
			for (File file : packageFolder.listFiles()) {
				if (file.isFile() && file.canRead() && file.getName().endsWith(GoConstants.GO_SOURCE_FILE_EXTENSION)
				        && !file.getName().endsWith(GoConstants.GO_TEST_FILE_EXTENSION)) {
	
					String text = readFile(file);
					parseText(text, true, false, codeContext);
				}
			}
		}

		return codeContext;
	}

	/**
     * @param file
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static String readFile(File file) throws FileNotFoundException, IOException {
    	StringBuilder sb = new StringBuilder();
	    BufferedReader br = null;
	    try {
	    	br = new BufferedReader(new FileReader(file));
	    	
	    	int c = 0;
	    	while ((c = br.read()) != -1) {
	    		sb.append((char) c);
	    	}
	    	
	    } finally {
	    	if (br != null) {
	    		br.close();
	    	}
	    }
	    return sb.toString();
    }

	/**
	 * @param fileText
	 * @param useExternalContext
	 * @param codeContext
	 * @throws IOException
	 */
	private static void parseText(String fileText, boolean packagePeer, boolean useExternalContext, CodeContext codeContext)
	        throws IOException {
		
		Lexer          lexer          = new Lexer();
		Tokenizer      tokenizer      = new Tokenizer(lexer);
		PackageParser  packageParser  = new PackageParser(tokenizer);
		ImportParser   importParser   = new ImportParser(tokenizer);
		ScopeParser    scopeParser    = new ScopeParser(tokenizer);
		
		FunctionParser functionParser = new FunctionParser(false, tokenizer);
		functionParser.setScopeParser(scopeParser);

		TypeParser     typeParser     = new TypeParser(false, tokenizer);
		typeParser.setScopeParser(scopeParser);

		VariableParser variableParser = new VariableParser(tokenizer, functionParser);
		variableParser.setScopeParser(scopeParser);

		// InterfaceParser interfaceParser = new InterfaceParser(tokenizer);

		lexer.scan(fileText);

		if (!packagePeer) {
			codeContext.page = new TokenizedPage(tokenizer.getTokenizedStream());
			codeContext.pkg = packageParser.getPckg();
			codeContext.imports.addAll(importParser.getImports());
		}
		
		codeContext.methods.addAll(functionParser.getMethods());
		codeContext.functions.addAll(functionParser.getFunctions());
		codeContext.types.addAll(typeParser.getTypes());
		codeContext.vars.addAll(variableParser.getVars());
		// scopeParser.print();
		// codeContext.interfaces = interfaceParser.getFunctions();

		// INFO: not for production
		// scopeParser.getRootScope().print("");

		if (useExternalContext) {
			for (Import imp : codeContext.imports) {
				CodeContext context = externalContexts.get(imp.path);
				if (context == null) {
					context = getExternalCodeContext(imp.path);
					externalContexts.put(imp.path, context);
				}

				codeContext.methods.addAll(context.methods);
				codeContext.functions.addAll(context.functions);
			}
		}
	}

	/**
	 * 
	 * @param filename
	 * @param fileText
	 * @return
	 * @throws IOException
	 * @throws RecognitionException
	 */
	public static CodeContext getExternalCodeContext(String packagePath) throws IOException {

		CodeContext    codeContext    = new CodeContext(packagePath);
		Lexer          lexer          = new Lexer();
		Tokenizer      tokenizer      = new Tokenizer(lexer);
		PackageParser  packageParser  = new PackageParser(tokenizer);
		FunctionParser functionParser = new FunctionParser(true, tokenizer);
		TypeParser     typeParser     = new TypeParser(true, tokenizer);
		// InterfaceParser interfaceParser = new InterfaceParser(tokenizer);

		// find path
		String goarch = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.GOARCH);

		String goos = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.GOOS);

		String goroot = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.GOROOT);

		File pkgdir = new File(goroot + "/src/pkg/" + packagePath);

		// TODO Get rid of the following duplication

		if (pkgdir.exists() && pkgdir.isDirectory()) {
			processExternalPackage(codeContext, lexer, packageParser, functionParser, pkgdir);
		} else {
			String path = Environment.INSTANCE.getAbsoluteProjectPath();
			pkgdir = new File(path + "/src/pkg/" + packagePath);
			if (pkgdir.exists() && pkgdir.isDirectory()) {
				processExternalPackage(codeContext, lexer, packageParser, functionParser, pkgdir);
			}

			String GOPATH = System.getenv("GOPATH").split(":")[0];
			
			pkgdir = new File(GOPATH + "/src/" + packagePath);
			if (pkgdir.exists() && pkgdir.isDirectory()) {
				processExternalPackage(codeContext, lexer, packageParser, functionParser, pkgdir);
			}
		}

		return codeContext;
	}

	/**
	 * @param codeContext
	 * @param lexer
	 * @param packageParser
	 * @param functionParser
	 * @param pkgdir
	 * @throws IOException
	 */
	private static void processExternalPackage(CodeContext codeContext, Lexer lexer, PackageParser packageParser,
	        FunctionParser functionParser, File pkgdir) throws IOException {
		File[] files = pkgdir.listFiles();
		for (File file : files) {
			if (file.canRead() && file.getName().endsWith(".go") && !file.getName().endsWith("_test.go")) {

				lexer.reset();
				lexer.scan(file);

				codeContext.pkg = packageParser.getPckg();
				codeContext.methods = functionParser.getMethods();
				codeContext.functions = functionParser.getFunctions();
				// codeContext.interfaces = interfaceParser.getFunctions();
			}
		}
	}

	/**
	 * @return
	 */
	public boolean isInImportStatement() {
		return false;
	}

	/**
	 * @param line
	 * @return
	 */
	public List<Node> getCompletionsForString(String line, String prefix, int linenumber) {

		ArrayList<Node> arrayList = new ArrayList<Node>();

		// parse line into parts and determine context
		List<TokenUnit> lineTokens = page.getTokensForLine(linenumber);
		for (TokenUnit unit : lineTokens) {
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
	public String getDescriptionForName(final String name) {

		String n = name + "()";

		// slowest possible searches...
		for (Method method : methods) {
			if (n.equals(method.getInsertionText())) {
				return method.getDocumentation();
			}
		}

		for (Function function : functions) {
			if (n.equals(function.getInsertionText())) {
				return function.getDocumentation();
			}
		}

		return "";
	}

	/**
	 * Declaration Only: Outside of function
	 * 
	 * @param args
	 */
	public List<Node> getCompletionsForDeclarations() {
		return null;
	}

	/**
	 * Declaration Only: Outside of function
	 * 
	 * @param args
	 */
	public List<Node> getCompletionsForFunction() {
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

		CodeContext other = (CodeContext) obj;

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
			while ((str += reader.readLine()) != null)
				;
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
		for (Method method : methods) {
			if (method.getInsertionText().startsWith(identifier + "(")) {
				return true;
			}
		}
		return false;
	}

	// need to use scope to determine this
	// public boolean isPackageVariableName(String identifier){
	// for(Var var: vars){
	// if(var.getInsertionText().startsWith(identifier)){
	// return true;
	// }
	// }
	// return false;
	// }

}
