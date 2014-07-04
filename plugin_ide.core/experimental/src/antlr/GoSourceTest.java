package com.googlecode.goclipse.go.antlr;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTree;

import com.googlecode.goclipse.go.antlr.GoSourceParser.program_return;

public class GoSourceTest {
	private static boolean exit = true;
	/**
	 * @param args
	 * @throws RecognitionException 
	 * @throws IOException 
	 */
	public static void main(String[] args){
		long t1 = System.nanoTime();
		if(!testFile("test_go/strings.go")) {
			System.out.println("fail");
			//System.exit(1);
		} else {
			System.out.println(">ok");
		}
		String baseTests = "~/bin/go/src/pkg";
		//testFile(baseTests + File.separator + "235.go");
		processFolder(new File(baseTests));
		System.out.println("done.");
		long t2 = System.nanoTime();
		System.out.println("time: "+((t2-t1)/1000000000.0)+"sec");
	}
	
	public static void processFolder(File folder) {
		if (!folder.isDirectory()) {return;}
		System.out.println("processing " + folder.getName());
		String[] fileNames = folder.list();
		int len = fileNames.length;
		int failed = 0;
		List<File> subf = new ArrayList<File>();
		for (String fName: fileNames) {
			String f1 = folder + File.separator + fName;
			File f1f = new File(f1);
			if (f1f.isDirectory()) {
				len --;
				subf.add(f1f);
			} else {
				if (!f1.endsWith(".go")) {len--; continue;}
				System.out.print("testing " + fName + ":");
				boolean result = testFile(f1);
				if (result) {
					System.out.println("ok");
				} else {
					failed++;
					System.out.println("fail");
					if (exit) System.exit(1);
						
				}
			}
		}
		System.out.println("pass " + (len - failed) + "/" + len);
		for (File f: subf) {
			processFolder(f);
		}
		
	}
	
	
	public static boolean testFile(String filename) {
		long t1 = System.nanoTime();
		boolean result = false;
		try {
			
			CharStream stream 		= new ANTLRFileStream(filename, "UTF-8");
			GoSourceLexer lexer 	= new GoSourceLexer(stream);
			TokenStream tokenStream = new CommonTokenStream(lexer);
			GoSourceParser parser 	= new GoSourceParser(tokenStream);
			CodeContext ctx         = new CodeContext(filename, "");
			parser.ctx       		= ctx;
			program_return program 	= parser.program();
			
			//System.out.println(((CommonTree)program.getTree()).toStringTree());
			if (parser.hasErrors) {
				result = false;
			} else {
				CommonTree tree = (CommonTree)program.getTree();
				//print(tree, "");
				result = true;
			}
			
		}catch(Throwable t) {
			t.printStackTrace();
			result = false;
		}
		
		return result;
	}
	
	
	public static void print(CommonTree tree, String ident) {
		System.out.println(ident + tree.getText());
		for (int i = 0; i< tree.getChildCount(); i++) {
			print((CommonTree)tree.getChild(i), ident+ "  ");
		}
	}
}