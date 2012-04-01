package com.googlecode.goclipse.dependency;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
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
	private static Map<String, DependencyGraph> graphs = new HashMap<String, DependencyGraph>();
	
	
	private IProject project;
		
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
			graphs.put(project.getName(), graph);
			
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
	
	/**
	 * @param folder
	 * @throws CoreException
	 * @throws IOException
	 */
	private void build(IFolder folder) throws CoreException, IOException{
		
		for (IResource res:folder.members()) {
			
			if (res instanceof IFolder) {
				build((IFolder)res);
				
			} else if (res instanceof IFile && res.getName().endsWith(".go")) {
				
				File file = res.getLocation().toFile();
				
				Lexer     	    lexer  		    = new Lexer();
				Tokenizer 	    tokenizer 	    = new Tokenizer(lexer);
				PackageParser   packageParser   = new PackageParser(tokenizer);
				ImportParser    importParser    = new ImportParser(tokenizer);
				lexer.scan(file);
				
				String dir = file.getParent().toString();
				String local = dir.replaceFirst(project.getLocation().toOSString(), "");
				
				if ("main".equals(packageParser.getPckg().getName())){
					local = file.getName();
				}
				
				PackageVertex localpkg = PackageVertex.getPackageVertex(project, local);
				
				for (Import i:importParser.getImports()) {
					System.out.println(local+" :: "+i.path);
					localpkg.addDependency(i.path);
					PackageVertex.getPackageVertex(project, i.path).addReverseDependency(local);
				}
			}
		}
	}
	
	/**
	 * @param pkgname
	 */
	public Set<String> getReverseDependencies(String pkgname) {
		return PackageVertex.getPackageVertex(project, pkgname).getReverseDependencies();
    }
}
