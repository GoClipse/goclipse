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
public class CodeContext {

	static HashMap<String, CodeContext>	externalContexts	= new HashMap<String, CodeContext>();

	public String	                    name;
	public String	                    filetext;
	public Package	                    pkg;
	public TokenizedPage	            page;
	public ArrayList<Import>	        imports	         = new ArrayList<Import>();
	public ArrayList<Method>	        methods	         = new ArrayList<Method>();
	public ArrayList<Function>	        functions	     = new ArrayList<Function>();
	public ArrayList<Type>	            types	         = new ArrayList<Type>();
	public ArrayList<Var>	            vars	         = new ArrayList<Var>();
	public ArrayList<Scope>	            moduleScope	     = new ArrayList<Scope>();

	/**
	 * @param filename
	 * @param fileText
	 * @throws IOException
	 */
	public CodeContext(String name) throws IOException {
		this.name = name;
	}

	/**
	 * @param filename
	 * @param fileText
	 * @return
	 * @throws IOException
	 * @throws RecognitionException
	 */
	public static CodeContext getCodeContext(String filename, String fileText) throws IOException {
		return getCodeContext(null, filename, fileText, true);
	}

	/**
	 * @param filename
	 * @param fileText
	 * @param useExternalContext
	 * @return
	 * @throws IOException
	 * @throws RecognitionException
	 */
	public static CodeContext getCodeContext(String filename, String fileText, boolean useExternalContext)
	        throws IOException {
		return getCodeContext(null, filename, fileText, useExternalContext);
	}

	/**
	 * @param project
	 * @param filename
	 * @param fileText
	 * @return
	 * @throws IOException
	 * @throws RecognitionException
	 */
	public static CodeContext getCodeContext(IProject project, String filename, String fileText) throws IOException {
		return getCodeContext(project, filename, fileText, true);
	}

	/**
	 * Static factory method that builds the code context. This is for non-test
	 * source files;
	 * 
	 * @param project
	 * @param filename
	 * @param fileText
	 * @param useExternalContext
	 * @return
	 * @throws IOException
	 */
	public static CodeContext getCodeContext(IProject project, String filename, String fileText,
	        boolean useExternalContext) throws IOException {

		boolean isCmdSrcFolder = false;

		if (project != null) {
			IResource res = project.findMember(filename);

			if (res != null && res instanceof IFile
			        && Environment.INSTANCE.isCmdSrcFolder(project, (IFolder) res.getParent())) {
				isCmdSrcFolder = true;

			}
		}

		CodeContext codeContext = new CodeContext(filename);

		File targetContext = new File(filename);
		if (!targetContext.exists()) {
			return codeContext;
		}

		File packageFolder = targetContext.getParentFile();

		parseText(project, targetContext, fileText, false, useExternalContext, codeContext);

		//
		// Only look at the other files in the directory if the file
		// itself is not a singular main function source file. Otherwise,
		// we could have the case where one main function source file could
		// pollute the code completion options of another the other main
		// source file.
		//
		if (!isCmdSrcFolder && useExternalContext) {
			for (File file : packageFolder.listFiles()) {
				if (file.isFile() && file.canRead() && file.getName().endsWith(GoConstants.GO_SOURCE_FILE_EXTENSION)
				        && !file.getName().endsWith(GoConstants.GO_TEST_FILE_EXTENSION)) {

					String text = readFile(file);
					parseText(project, file, text, true, false, codeContext);
				}
			}
		}

		return codeContext;
	}
	
	/**
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static CodeContext getTestCodeContext(IProject project, File pkg) throws IOException {
		CodeContext context = new CodeContext(pkg.getName());
		for(File child:pkg.listFiles()) {
			if (child.getName().endsWith("_test.go")) {
				parseText(project, child, readFile(child), false, false, context);
			}
		}
		
		return context;
	}
	
	/**
	 * Performing this merge operation only makes sense
	 * within the context of a single package.
	 * 
	 * @param b
	 */
	private void mergeTestContexts(CodeContext b) {
		this.imports.addAll(b.imports);
		this.methods.addAll(b.methods);
		this.functions.addAll(b.functions);
		this.types.addAll(b.types);
		this.vars.addAll(b.vars);
		this.moduleScope.addAll(b.moduleScope);
	}

	/**
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String readFile(File file) throws FileNotFoundException, IOException {
		
		StringBuilder  sb = new StringBuilder();
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
	private static void parseText(IProject project, File file, String fileText, boolean packagePeer, boolean useExternalContext,
	        CodeContext codeContext) throws IOException {

		Lexer         lexer         = new Lexer();
		Tokenizer     tokenizer     = new Tokenizer(lexer);
		PackageParser packageParser = new PackageParser(tokenizer, file);
		ImportParser  importParser  = new ImportParser(tokenizer, file);
		ScopeParser   scopeParser   = new ScopeParser(tokenizer, file);

		FunctionParser functionParser = new FunctionParser(false, tokenizer, file);
		functionParser.setScopeParser(scopeParser);

		TypeParser typeParser = new TypeParser(false, tokenizer, file);
		typeParser.setScopeParser(scopeParser);

		VariableParser variableParser = new VariableParser(tokenizer, file, functionParser);
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

		if (useExternalContext) {
			
			for (Import imp : codeContext.imports) {
			
				CodeContext context = externalContexts.get(imp.path);
				
				if (context == null) {
					context = getExternalCodeContext(project, imp.path);
					externalContexts.put(imp.path, context);
				}

				for (Method method : context.methods) {
					method.setPackage(context.pkg);
				}
				
				codeContext.methods.addAll(context.methods);

				for (Function function : context.functions) {
					function.setPackage(context.pkg);
				}
				
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
	public static CodeContext getExternalCodeContext(IProject project, String packagePath) throws IOException {
		
		CodeContext codeContext = new CodeContext(packagePath);

		// InterfaceParser interfaceParser = new InterfaceParser(tokenizer);
		// find path
		String goarch = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.GOARCH);
		String goos = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.GOOS);
		String goroot = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.GOROOT);
		File pkgdir = new File(goroot + "/src/pkg/" + packagePath);

		// TODO Get rid of the following duplication

		if (pkgdir.exists() && pkgdir.isDirectory()) {
			processExternalPackage(codeContext, pkgdir);

		} else {

			for (IFolder folder : Environment.INSTANCE.getSourceFolders(project)) {

//				String path = Environment.INSTANCE.getAbsoluteProjectPath();

				pkgdir = new File(folder.getLocation().toOSString() + File.separator + packagePath);

				if (pkgdir.exists() && pkgdir.isDirectory()) {
					processExternalPackage(codeContext, pkgdir);

				} else {
					continue;
				}

				String goPath = Environment.INSTANCE.getGoPath(project)[0];

				pkgdir = new File(goPath + "/src/" + packagePath);
				if (pkgdir.exists() && pkgdir.isDirectory()) {
					processExternalPackage(codeContext, pkgdir);
				}
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
	private static void processExternalPackage(CodeContext codeContext, File pkgdir) throws IOException {

		File[] files = pkgdir.listFiles();

		for (File file : files) {
			Lexer          lexer          = new Lexer();
			Tokenizer      tokenizer      = new Tokenizer(lexer);
			PackageParser  packageParser  = new PackageParser(tokenizer, file);
			FunctionParser functionParser = new FunctionParser(true, tokenizer, file);
			TypeParser     typeParser     = new TypeParser(true, tokenizer, file);

			if (file.canRead() && file.getName().endsWith(".go") && !file.getName().endsWith("_test.go")) {

				lexer.reset();
				lexer.scan(file);

				codeContext.pkg = packageParser.getPckg();
				codeContext.methods.addAll(functionParser.getMethods());
				for (Method method : functionParser.getMethods()) {
					if (method.getFile() == null) {
						method.setFile(file);
					}
				}

				codeContext.functions.addAll(functionParser.getFunctions());
				for (Function function : functionParser.getFunctions()) {
					if (function.getFile() == null) {
						function.setFile(file);
					}
				}

				codeContext.types.addAll(typeParser.getTypes());
				for (Type type : typeParser.getTypes()) {
					if (type.getFile() == null) {
						type.setFile(file);
					}
				}

				// codeContext.vars = variableParser.getVars();
				// for(Var var:variableParser.getVars()){
				// if(var.getFile()==null){
				// var.setFile(file);
				// }
				// }

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
	 * @param name
	 * @return
	 */
	public Import getImportForName(String name) {
		
		if (name == null || name.length()==0){
			return null;
		}
		
		for (Import i : imports) {
			if (i.prefix != null && i.prefix.equals(name)) {
				return i;
			}
		}
		
		return null;
	}
	
	/**
	 * @param name
	 * @return
	 */
	public Function getFunctionForName(String name) {
		
		if (name == null || name.length()==0){
			return null;
		}
		
		for (Function f : functions) {
			if (f.getInsertionText().equals(name)) {
				return f;
			}
		}
		
		return null;
	}
	
	/**
	 * @param name
	 * @return
	 */
	public Method getMethodForName(String name, String type) {
		
		if (name == null || name.length()==0){
			return null;
		}
		
		for (Method m : methods) {
			if (m.getInsertionText().equals(name)) {
				System.out.println(m.getName());
				return m;
			}
		}
		
		return null;
	}

	/**
	 * @param name
	 * @return
	 */
	public Var getVarForName(String name, int line) {
		
		if (name == null || name.length()==0){
			return null;
		}
		
		for (Var v : vars) {
			if (v.getInsertionText().equals(name) &&
				v.getScope().getStart() <= line   &&
				v.getScope().getEnd() >= line  ) {
				return v;
			}
		}
		
		return null;
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

		for (Type type : types) {
			if (name.equals(type.getInsertionText())) {
				return type.getDocumentation();
			}
		}

		return "";
	}

	/**
	 * 
	 * @param names
	 * @return
	 */
	public Node getLocationForPkgAndName(final String pkg, final String name) {

		String n = name + "()";
		CodeContext cc = externalContexts.get(pkg);

		// slowest possible searches...
		for (Method method : cc.methods) {
			if (n.equals(method.getInsertionText())) {
				return method;
			}
		}

		for (Function function : cc.functions) {
			if (n.equals(function.getInsertionText())) {
				return function;
			}
		}

		for (Type type : cc.types) {
			if (name.equals(type.getName())) {
				// System.out.println(">>");
				return type;
			}
		}

		// for (Var var : vars) {
		// Package p = var.getPackage();
		// if (p != null && n.equals(var.getInsertionText()) && pkg!=null &&
		// pkg.endsWith(p.getName())) {
		// return var;
		// }
		// }

		return null;
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

	/**
	 * @param identifier
	 * @return
	 */
	public boolean isMethodName(String identifier) {
		for (Method method : methods) {
			if (method.getInsertionText().startsWith(identifier + "(")) {
				return true;
			}
		}
		return false;
	}

}
