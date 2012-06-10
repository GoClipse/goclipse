package com.googlecode.goclipse.dependency;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.Environment;
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
	private static Map<String, DependencyGraph> graphs       = new HashMap<String, DependencyGraph>();
	private static Map<String, File>            commandFiles = new HashMap<String, File>();
	private static Map<String, IFolder>         pkgFolders   = new HashMap<String, IFolder>();
	
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
			
			// get the src directories
			List<IFolder> folders = Environment.INSTANCE.getSourceFolders(project);
			
			for (IFolder srcfolder:folders) {
				try {
		            graph.build(srcfolder, srcfolder);
		        } catch (Exception e) {
		            Activator.logError(e);
	            }
			}
		} else {
			// rebuild modified portions of the graph
			
		}
		
		return graph;
	}
	
	/**
	 * @param folder
	 * @throws CoreException
	 * @throws IOException
	 */
	private void build(IFolder srcfolder, IFolder folder) throws CoreException, IOException{
		
		for (IResource res:folder.members()) {
			
			if (res instanceof IFolder) {
				String local = res.getParent().toString().replace(srcfolder.toString()+File.separator, "");
				pkgFolders.put(local, (IFolder)res);
				build(srcfolder, (IFolder)res);
				
			} else if (res instanceof IFile && res.getName().endsWith(".go")) {
				processDependencies(srcfolder, res);
			}
		}
	}

	/**
     * @param srcfolder
     * @param res
     * @throws IOException
     */
    private void processDependencies(IFolder srcfolder, IResource res) throws IOException {
	    File file = res.getLocation().toFile();
	    
	    if (file.isDirectory()){
	    	return;
	    }
	    
	    Lexer     	    lexer  		    = new Lexer();
	    Tokenizer 	    tokenizer 	    = new Tokenizer(lexer);
	    PackageParser   packageParser   = new PackageParser(tokenizer);
	    ImportParser    importParser    = new ImportParser(tokenizer);
	    lexer.scan(file);
	    
	    String local = res.getParent().toString().replace(srcfolder.toString()+File.separator, "");
	    
	    // command files have dependencies
	    if ("main".equals(packageParser.getPckg().getName())){
	    	local = file.getName();
	    	commandFiles.put(local, file);
	    }
	    
	    PackageVertex localpkg = PackageVertex.getPackageVertex(project, local);
	    
	    for (Import i:importParser.getImports()) {
	    	localpkg.addDependency(i.path);
	    	PackageVertex.getPackageVertex(project, i.path).addReverseDependency(local);
	    }
    }
	
	/**
	 * @param pkgname
	 */
	public Set<String> getReverseDependencies(String pkgname) {
		return PackageVertex.getPackageVertex(project, pkgname).getReverseDependencies();
    }
	
	/**
	 * @param pkgname
	 */
	public Set<String> getDependencies(String pkgname) {
		PackageVertex v = PackageVertex.getPackageVertex(project, pkgname);
		if (v != null) {
			return v.getDependencies();
		} else {
			return new HashSet<String>();
		}
    }
	
	/**
	 * @param pkgname
	 */
	public void clearDependencies(String pkgname) {
		PackageVertex v = PackageVertex.getPackageVertex(project, pkgname);
		if(v!=null){
			v.clearDependencies();
		}
		
		return;
    }
	
	/**
	 * @param name
	 * @return
	 */
	public File getCommandFileForName(String name) {
		return commandFiles.get(name);
	}

	/**
	 * @param modified
	 */
	public void reprocessResources(List<IResource> modified) {
		
		List<IFolder> srcFolders = Environment.INSTANCE.getSourceFolders(project);
		
		for (IResource resource: modified) {
			for (IFolder srcfolder : srcFolders) {
				String pkgname = "";
				IFolder folder = null;
						
				if (resource instanceof IFolder) {
					pkgname = resource.toString().replaceFirst(srcfolder.toString(), "");
					folder = project.getFolder(resource.getLocation());
					Set<String> dep = this.getDependencies(pkgname);
					dep.clear();
					
				} else if (resource instanceof IFile) {
					folder = (IFolder)resource.getParent();
					pkgname = resource.getParent().toString().replaceFirst(srcfolder.toString()+File.separator, "");
					Set<String> dep = this.getDependencies(pkgname);
					dep.clear();
				}

				if (folder != null && folder.exists()) {
					
					try {
						for (IResource file: folder.members()) {
							processDependencies(srcfolder, file);
						}
						
						Set<String> dep = this.getDependencies(pkgname);
                    } catch (IOException e) {
                    	Activator.logError(e);
                    } catch (CoreException e) {
                    	Activator.logError(e);
                    }
				}
				
			}
		}
    }
}
