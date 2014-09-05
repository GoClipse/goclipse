package com.googlecode.goclipse.builder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.dependency.DependencyGraph;
import com.googlecode.goclipse.go.lang.lexer.Lexer;
import com.googlecode.goclipse.go.lang.lexer.Tokenizer;
import com.googlecode.goclipse.go.lang.model.Package;
import com.googlecode.goclipse.go.lang.parser.PackageParser;

/**
 * This class is the target called by the Eclipse environment to
 * build the active Go projects in the workspace.
 */
public class GoBuilder extends IncrementalProjectBuilder {
	
	private GoCompiler compiler;
	private GoGet goGet;
	
	/**
	 * Entry point of this builder, everything start from this method.
	 */
	@Override
	@SuppressWarnings("rawtypes")
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor) throws CoreException {
		if (!Environment.INSTANCE.isValid()){
			MarkerUtilities.addMarker(getProject(), GoConstants.INVALID_PREFERENCES_MESSAGE);
			return null;
		}
		
		if (compiler == null){
			compiler = new GoCompiler();
		}
		
		if (goGet == null) {
			goGet = new GoGet();
		}
		
		if (compiler.requiresRebuild(getProject())) {
			kind = FULL_BUILD;
		}
		
		GoToolManager.getDefault().notifyBuildStarting(getProject());
		
		try {
			
			if (kind == FULL_BUILD) {
				fullBuild(monitor);
			} else {
				IResourceDelta delta = getDelta(getProject());
				if (delta == null) {
					fullBuild(monitor);
				} else {
					incrementalBuild(getProject(), delta, monitor);
				}
			}
			
			compiler.updateVersion(getProject());
			
		} catch(Exception e) {
			Activator.logError(e);
		}
		
		GoToolManager.getDefault().notifyBuildTerminated(getProject());
		// no project dependencies (yet)
		return null;
	}

	/**
	 * @param pmonitor
	 * @throws CoreException
	 */
	protected void fullBuild(final IProgressMonitor pmonitor) throws CoreException {
		Activator.logInfo("fullBuild");
		final IProject project = getProject();
		final SubMonitor monitor = SubMonitor.convert(pmonitor, 2000);
		final List<IFile> toCompileList = new ArrayList<IFile>();

		project.accept(new IResourceVisitor() {
					@Override
					public boolean visit(IResource resource) {
						if (resource.getParent().equals(project) && resource instanceof IFolder) {
							return resource.getName().equals("src");
						}

						if (resource instanceof IFile &&
								resource.getName().endsWith(".go")) {
							toCompileList.add((IFile)resource);
						}
						// return true to continue visiting children.
						return true;
					}
				}
		);
		monitor.worked(20);

		clean(monitor.newChild(10));
		
		Set<String> packages = new HashSet<String>();
		int cost = 2000 / (toCompileList.size() + 1);
		for(IFile toCompile: toCompileList) {
			File file = toCompile.getLocation().toFile();
			try {
				if ( isCommandFile(file) ){
					// if it is a command file, compile as such
					monitor.beginTask("Compiling command file "+file.getName(), cost);
					compiler.compileCmd(project, monitor.newChild(100), file);
				} else {
					// else if not a command file, schedule to build the package
					String pkgpath = computePackagePath(file);
					if ( !packages.contains(pkgpath) ) {
						monitor.beginTask("Compiling package "+file.getName().replace(".go", ""), cost);
						compiler.compilePkg(project, monitor.newChild(100), pkgpath, file);
						packages.add(pkgpath);
					}
				}

			} catch (IOException e) {
				Activator.logError(e);
			}
		}
		Activator.logInfo("fullBuild - done");
		getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	private final String computePackagePath(File file) {
		IProject project = getProject();
		final IPath projectLocation = project.getLocation();
		final IFile ifile = project.getFile(file.getAbsolutePath().replace(project.getLocation().toOSString(), ""));
		IPath pkgFolder = Environment.INSTANCE.getPkgOutputFolder();
		
		String pkgname = ifile.getParent().getLocation().toOSString().replace(projectLocation.toOSString(), "");
		if(file.isDirectory()){
			pkgname = ifile.getLocation().toOSString().replace(projectLocation.toOSString(), "");
		}
		
		String[] split = pkgname.split(File.separatorChar == '\\' ? "\\\\" : File.separator);
		String path = projectLocation.toOSString() + File.separator + pkgFolder;

		for (int i = 2; i < split.length; i++) {
			path += File.separator + split[i];
		}

		return path + GoConstants.GO_LIBRARY_FILE_EXTENSION;
	}


	/**
	 * incrementalBuild builds the most recently edited package or command file.
	 * Then, it walks the reverse dependencies backwards building packages and
	 * command files until everything that depended on the original pkg or
	 * command file was built.
	 * 
	 * @param project
	 * @param delta
	 * @param pmonitor
	 * @throws CoreException
	 */
	protected void incrementalBuild(final IProject project, IResourceDelta delta,
			IProgressMonitor pmonitor) throws CoreException {		
		SubMonitor monitor = SubMonitor.convert(pmonitor, 170);
		
		// collect resources
		final List<IFile> toCompileList = new ArrayList<IFile>();
		final List<IFile> removed = new ArrayList<IFile>();
		delta.accept(new IResourceDeltaVisitor() {
			@Override
			public boolean visit(IResourceDelta delta) throws CoreException {
				IResource resource = delta.getResource();
				if (resource.getParent().equals(project) && resource instanceof IFolder) {
					return resource.getName().equals("src");
				}

				if (resource instanceof IFile && resource.getName().endsWith(".go")) {
					switch (delta.getKind()) {
						case IResourceDelta.ADDED:
							// handle added resource
							toCompileList.add((IFile)resource);
							break;
							
						case IResourceDelta.CHANGED:
							// handle changed resource
							toCompileList.add((IFile)resource);
							break;
							
						case IResourceDelta.REMOVED:
							// handle removed resource
							removed.add((IFile)resource);
							break;
					}
				}
				// return true to continue visiting children.
				return true;
			}
		});

		monitor.worked(20);

		Activator.logInfo("incrementalBuild");
		
		DependencyGraph graph = DependencyGraph.getForProject(project);
		
		// compile
		graph.reprocessResources(toCompileList);
		graph.reprocessResources(removed);
		
		for (IFile toCompile : toCompileList) {
			File file = toCompile.getLocation().toFile();
			try {
				if (isCommandFile(file)){
					// if it is a command file, compile as such
					compiler.compileCmd(project, monitor.newChild(100), file);
					
				} else {
					// else if not a command file, schedule to build the package
					String pkgpath = computePackagePath(file);

					// clean the old package
					File rm = new File(pkgpath);
					try {
                        rm.delete();
                    } catch (Exception e) {
                        Activator.logError(e);
                    }
					
					/**
					 * Not only do we have to compile this package, but we have
					 * to (afterwards) compile every package that depends on this
					 * one.
					 */
					String pkg = toCompile.toString();
					String parent = toCompile.getParent().toString();
					for(IFolder folder : Environment.INSTANCE.getSourceFolders(project)) {
						if (parent.startsWith(folder.toString())){
							pkg = parent.replace(folder.toString()+"/", "");
						}
					}
					
					monitor.beginTask("Compiling package "+file.getName().replace(".go", ""), 1);
					compiler.compilePkg(project, monitor.newChild(100), pkgpath, file);

					List<IFolder> srcfolders = Environment.INSTANCE.getSourceFolders(project);
					buildDependencies(project, srcfolders, monitor, graph, file, pkg);
				}
				
			} catch (IOException e) {
				Activator.logError(e);
			}
		}
		Activator.logInfo("incrementalBuild - done");
	}
	
	
	/**
     * @param project
     * @param srcfolders
     * @param monitor
     * @param graph
     * @param packages
     * @param file
     * @param pkg
     */
    private void buildDependencies(IProject project, List<IFolder> srcfolders, SubMonitor monitor,
            DependencyGraph graph, File file, String pkg) {

		Set<String> built   = new HashSet<String>();
		Set<String> depends = graph.getReverseDependencies(pkg);
	    int max_depth = 32;
	    int depth = 0;
	    
	    Comparator<String> comparator = new Comparator<String>(){

			@Override
            public int compare(String arg0, String arg1) {
				boolean a = arg0.endsWith(".go");
				boolean b = arg1.endsWith(".go");
                
				if(a && !b){
                	return -1;
                	
                } else if(!a && b){
                	return 1;
                
                }
                
                return 0;
            }
    	};
    	
	    while ( depends.size() > 0 && depth < max_depth ) {
	    	
	    	depth++;
	    	List<String> sortedDependencies = new ArrayList<String>(depends);
	    	Collections.sort(sortedDependencies, comparator);
	    	depends = new HashSet<String>();
	    	
	    	for (String name:sortedDependencies) {
	    		depends.addAll(graph.getReverseDependencies(name));
	    		
	    		if(built.contains(name)){
	    			continue;
	    		}
	    		
	    		if ( name.endsWith(".go")) {
	    			File cmdfile = graph.getCommandFileForName(name);
	    			compiler.compileCmd(project, monitor.newChild(100), cmdfile);
	    			
	    		} else {
	    			
	    			for (IFolder srcfolder:srcfolders) {
	    			
	    				String 	  dependentPkgName  = srcfolder.getProjectRelativePath().toString()+File.separatorChar+name;
	    				IResource res2 				= project.findMember(dependentPkgName);
	    				File 	  file2 			= res2.getLocation().toFile();
	    				String 	  target 			= computePackagePath(file2);
	    				
	    				if (!built.contains(target) ) {
	    					monitor.beginTask("Compiling package  "+file.getName().replace(".go", ""), 1);
	    					compiler.compilePkg(project, monitor.newChild(100), target, file2);
	    				}
	    			}
	    		}
	    		
	    		built.add(name);
	    	}
	    }
    }

	/**
	 * TODO this needs to be centralized into a common index...
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private boolean isCommandFile(File file) throws IOException {
		Lexer 		  lexer         = new Lexer();
		Tokenizer 	  tokenizer 	= new Tokenizer(lexer);
		PackageParser packageParser = new PackageParser(tokenizer, file);
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String temp = "";
		StringBuilder builder = new StringBuilder();
		while( (temp = reader.readLine()) != null ) {
			builder.append(temp);
			builder.append("\n");
		}
		reader.close();
		lexer.scan(builder.toString());
		Package pkg = packageParser.getPckg();
		
		if (pkg != null && "main".equals(pkg.getName())) {
			return true;
		}
		
		return false;
	}

	@Override
	protected void clean(IProgressMonitor monitor) throws CoreException {
		IProject project = getProject();
		
		MarkerUtilities.deleteAllMarkers(project);
		
		IPath binPath = Environment.INSTANCE.getBinOutputFolder();
		File binFolder = new File(project.getLocation().append(binPath).toOSString());
		
		if (binFolder.exists()) {
			deleteFolder(binFolder, true);
		}
		
		IPath pkgPath = Environment.INSTANCE.getPkgOutputFolder();
		
		File pkgFolder = new File(project.getLocation().append(pkgPath).toOSString());
		
		if (pkgFolder.exists()) {
			deleteFolder(pkgFolder, true);
		}
		
		project.accept(new IResourceVisitor() {
			@Override
			public boolean visit(IResource resource) throws CoreException {
				IPath relativePath = resource.getProjectRelativePath();
				Environment instance = Environment.INSTANCE;
				String lastSegment = relativePath.lastSegment();
				IPath rawLocation = resource.getRawLocation();
				if (rawLocation != null) {
					File file = rawLocation.toFile();
					if (file.exists() && file.isDirectory() &&
						(instance.isCmdFile(relativePath) || instance.isPkgFile(relativePath))
						&& (lastSegment.equals(GoConstants.OBJ_FILE_DIRECTORY) || lastSegment.equals(GoConstants.TEST_FILE_DIRECTORY)))
					{
						deleteFolder(file, true);
					}
				}
				return resource instanceof IContainer;
			}
		}, IResource.DEPTH_INFINITE, false);
		
		project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
	}

	private boolean deleteFolder(File f, boolean justContents) {
		if (!f.exists()) {
			return false;
		}
		if (f.isDirectory()) {
			String[] children = f.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteFolder(new File(f, children[i]), false);
				if (!success) {
					return false;
				}
			}
			if (!justContents) {
				f.delete();
			}
		} else {
			f.delete();
		}
		return true;
	}

}
