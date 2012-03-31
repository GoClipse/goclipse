package com.googlecode.goclipse.dependency;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.go.lang.lexer.Lexer;
import com.googlecode.goclipse.go.lang.lexer.Tokenizer;
import com.googlecode.goclipse.go.lang.model.Import;
import com.googlecode.goclipse.go.lang.parser.ImportParser;
import com.googlecode.goclipse.go.lang.parser.PackageParser;
/**
 * DependencyGraph maintains a directed graph of all the dependencies within the
 * project.  It will detect cycles and mark errors when they occur.
 */
public class DependencyGraph {
	
	/**
	 * 
	 */
	private static Map<IProject, DependencyGraph> graphs = new HashMap<IProject, DependencyGraph>();
	
	
	private IProject project;
	
	private Set<UndirectedEdge> edges = new HashSet<UndirectedEdge>();
	
	/**
	 * Hidden Constructor
	 */
	private DependencyGraph(IProject project) {
		this.project = project;
	}
	
	/**
	 * @param project
	 * @return
	 */
	public static DependencyGraph getForProject(IProject project) {
		
		DependencyGraph graph = graphs.get(project.getName());
		
		if (graph==null) {
			graph = new DependencyGraph(project);
			graphs.put(project, graph);
			
			// get the src directory
			IFolder folder = project.getFolder("src");
			
			// recurse
			try {
	            graph.build(folder);
            } catch (Exception e) {
	            Activator.logError(e);
            }
		}
		
		return graph;
	}
	
	
	private void build(IFolder folder) throws CoreException, IOException{
		
		for (IResource res:folder.members()) {
			if(res instanceof IFolder ){
				build((IFolder)res);
				
			} else if (res instanceof IFile && ".go".equals(res.getFileExtension())) {
				
				File file = res.getLocation().toFile();
				
				Lexer     	    lexer  		    = new Lexer();
				Tokenizer 	    tokenizer 	    = new Tokenizer(lexer);
				PackageParser   packageParser   = new PackageParser(tokenizer);
				ImportParser    importParser    = new ImportParser(tokenizer);
				lexer.scan(file);
				
				String dir = file.getParent().toString();
				String local = dir.replaceFirst(project.getLocation().toOSString(), "");
				
				for (Import i:importParser.getImports()){
					UndirectedEdge edge = UndirectedEdge.buildEdge(local, i.path);
					edges.add(edge);
				}
			}
		}
	}
	
	
	
}
