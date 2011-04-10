package com.googlecode.goclipse.go.lang.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.googlecode.goclipse.go.Func;
import com.googlecode.goclipse.go.Package;
import com.googlecode.goclipse.go.Type;
import com.googlecode.goclipse.go.Type.TypeClass;
import com.googlecode.goclipse.go.UnresolvedFunc;
import com.googlecode.goclipse.go.UnresolvedType;
import com.googlecode.goclipse.go.Var;
import com.googlecode.goclipse.go.lang.lexer.Lexer;
import com.googlecode.goclipse.go.lang.lexer.TokenType;
import com.googlecode.goclipse.go.lang.lexer.TokenUnit;


public class LibraryParser {

	public LibraryParser(){
		
	}
	
	public void parse(Package pkg, String filename){
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
			String line = null;
			boolean start = false;
			
			while((line = bufferedReader.readLine())!=null){
			
				line = line.trim();
				
				// Determine if we are in the package def
				if(line.contains("exports automatically generated from")){
					start = true;
					continue;
				}
				else if(line.contains("// local types")){
					start = false;
					continue;
				}
				
				if(start){
					if(line.startsWith("func")){
						line = line.replaceAll("func", "");
						if(Character.isUpperCase(line.trim().charAt(0))){
							System.out.println("        func: "+line);
						}
						else {
							System.out.println("private func: "+line);
						}
					}
					else if(line.startsWith("const")){
						line = line.replaceAll("const", "");
						if(Character.isUpperCase(line.trim().charAt(0))){
							System.out.println("        const: "+line);
						}
						else {
							System.out.println("private const: "+line);
						}
					}
					else if(line.startsWith("type")){
						
						line = line.replaceAll("\"\".", "").replaceAll("type", "");
						
						if(Character.isUpperCase(line.trim().charAt(0))){
							System.out.println("        type: " + line);
							
							final Type type = new Type(pkg, "");
							
							if(line.contains("struct")){
								parseStruct(line, type);
							}
							else if(line.contains("interface")){
								parseInterface(line, type);
							}
							else{ // ALIAS TYPE
								parseAlias(line, type);
							}
						}
						else {
							System.out.println("private type: "+line);
						}
					}
					else if(line.startsWith("var")){
						line = line.replaceAll("\"\".", "").replaceAll("var", "");
						if(Character.isUpperCase(line.trim().charAt(0))){
							System.out.println("        var:  "+line);
						}
						else {
							System.out.println("private var:  "+line);
						}
					}
				}
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * 
	 * @param line
	 * @param type
	 * @throws IOException
	 */
	private void parseStruct(String line, final Type type) throws IOException {
		String[] p1 = line.split("struct");
		String   p2 = p1[1].replace("}", "");
		String   p3 = p2.replace("{", "");
		String[] p4 = p3.split(";");
		
		type.setDisplayName(p1[0]);
		type.setTypeClass(TypeClass.STRUCT);
		
		for(String str:p4){
			String[] varParts = str.trim().split(" ");
			if(varParts[0].equals("?")){
				type.addEmbeddedTypes(new UnresolvedType(varParts[1]));
			}
			else{
				Var var = new Var();
				var.setName(varParts[0]);
				if(varParts[1].startsWith("*")){
					var.setPointer(true);
					var.setType(new UnresolvedType(varParts[1].replace("*", "")));
				}
				else{
					var.setType(new UnresolvedType(varParts[1]));
				}
				type.addVar(var);
			}
		}
	}
	
	/**
	 * 
	 * @param line
	 * @param type
	 * @throws IOException
	 */
	private void parseInterface(String line, final Type type) throws IOException {
		String[] p1 = line.split("interface");
		String   p2 = p1[1].replace("}", "");
		String   p3 = p2.replace("{", "");
		String[] p4 = p3.split(";");
		
		type.setDisplayName(p1[0]);
		type.setTypeClass(TypeClass.INTERFACE);
		
		for(String str:p4){
			UnresolvedFunc func = new UnresolvedFunc();
			parseFunc(str, func);
		}
	}
	
	/**
	 * 
	 * @param line
	 * @param type
	 * @throws IOException
	 */
	private void parseAlias(String line, final Type type) throws IOException {
		String trimmedLine = line.trim();
		String[] p1 = trimmedLine.split(" ");
		type.setDisplayName(trimmedLine);
		type.setTypeClass(TypeClass.ALIAS);
		type.setName(p1[0]);
		type.setAlias(true);
	}
	
	/**
	 * 
	 * @param line
	 * @param func
	 * @throws IOException
	 */
	private void parseFunc(String line, final Func func) throws IOException {
		line = line.trim();
		func.setDisplayName(line);
		
		// more complex parsing is required
		Lexer lexer = new Lexer();
		lexer.scan(line);
		List<TokenUnit> tokens = lexer.getTokenStream();
				
		func.setInsertionName(tokens.get(0).text);
		System.out.println(tokens.get(0).text);
		int i = 1;
		// parse parameters
		for(TokenUnit unit = tokens.get(i); !unit.text.equals(")"); unit = tokens.get(i), i++){
			if(unit.tokenType==TokenType.IDENTIFIER){
				System.out.println("parm >> "+unit.text);
				i+=2;
			}
		}
		
		// parse return
		for(TokenUnit unit = tokens.get(i); !unit.text.equals(")"); unit = tokens.get(i), i++){
			if(unit.tokenType==TokenType.IDENTIFIER){
//				func.setReturnType(returnType)
				System.out.println("ret >> "+unit.text);
				i+=2;
			}
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LibraryParser libraryParser = new LibraryParser();
		libraryParser.parse(new Package("mytest"), "test_go/tester.a");
	}
}
