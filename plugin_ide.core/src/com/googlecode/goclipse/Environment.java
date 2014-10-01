package com.googlecode.goclipse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import melnorme.utilbox.misc.MiscUtil;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import com.googlecode.goclipse.builder.GoConstants;
import com.googlecode.goclipse.core.GoEnvironmentPrefConstants;

/**
 * Provides environmental utility methods for acquiring and storing a user
 * session's contextual data.
 * 
 */
@Deprecated
public class Environment {
	
	public static final boolean DEBUG = Boolean.getBoolean("goclipse.debug");
	
	public static final Environment INSTANCE = new Environment();
	
	private Environment() {
	}
	
	public String[] getSourceFoldersAsStringArray(IProject project) {
		return new String[] {"src"};
	}
	
	/**
	 * Return a list of source folders for the currently active project.
	 * 
	 * @return
	 */
	public List<IFolder> getSourceFolders(IProject project) {
		List<IFolder> result = new ArrayList<IFolder>();
		
		for (String path : getSourceFoldersAsStringArray(project)) {
			IResource resource = project.findMember(path);
			
			if (resource instanceof IFolder) {
				result.add((IFolder)resource);
			}
			
		}
		
		return result;
	}
	
	public IPath getDefaultCmdSourceFolder() {
		return Path.fromOSString("src");
	}
	
	public IPath getDefaultPkgSourceFolder() {
		return Path.fromOSString("src");
	}
	
	/**
	 * @param path
	 * @return
	 */
	public boolean isCmdFile(IPath path) {
		return getDefaultCmdSourceFolder().isPrefixOf(path);
	}
	
	/**
	 * @param path
	 * @return
	 */
	public boolean isPkgFile(IPath path) {
		return getDefaultPkgSourceFolder().isPrefixOf(path);
	}
	
	public static String getExecutableExtension() {
		return MiscUtil.OS_IS_WINDOWS ? ".exe" : "";
	}
	
	/**
	 * @param project
	 * @param folder
	 * @return
	 */
	public boolean isCmdSrcFolder(IProject project, IFolder folder){
		
		if (project == null || folder == null) {
			return false;
		}
		
		boolean isSrcFolder = false;
		String path = folder.getLocation().toOSString();
		
		for (IFolder src:getSourceFolders(project)){
			if (src.getLocation().toOSString().equals(path)) {
				isSrcFolder = true;
				break;
			}
		}
		
		if (!isSrcFolder) {
			return false;
		}
		
		if (folder.getName().equals("cmd") || folder.getName().equals("src")) {
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * Determines via the file suffix if the file is a test file or not.
	 * @param file
	 * @return
	 */
	public boolean isNonTestSourceFile(IFile file){
		
		if (file==null) {
			return false;
		}
		
		if (file.getName().endsWith("_test.go")) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * 
	 * @param project
	 * @param file
	 * @return
	 */
	public boolean isSourceFile(IProject project, IFile file) {
		IPath p = file.getProjectRelativePath();
		IResource res = project.findMember(p);
		if ( res==null ) {
			return false;
		}
		
		if ( !file.getName().endsWith(GoConstants.GO_SOURCE_FILE_EXTENSION) ) {
			return false;
		}
		
		for (IFolder folder : getSourceFolders(project)) {
			if(file.getLocation().toOSString().startsWith(folder.getLocation().toOSString())) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * @param project
	 * @return
	 */
	public String[] getGoPath(IProject project) {
		String[] path   = { "" };
		String   goPath = null;
		
		// Plug-in property comes next
		if (goPath == null || "".equals(goPath)) {
			goPath = GoEnvironmentPrefConstants.GO_PATH.get(project);
		}
		
		// last ditch effort via a system environment variable
		if (goPath == null || "".equals(goPath)) {
			goPath = System.getenv(GoConstants.GOPATH);
		}
		
		// If null, we give up and just return the default...
		// which is essentially an empty string
		if (goPath == null) {
			return path;
		}
		
		return goPath.split(Pattern.quote(File.pathSeparator));
	}
	
	public String getGoRoot(IProject project) {
		String goroot = null;
		
		// Plug-in property comes next
		if (goroot == null || "".equals(goroot)) {
			goroot = GoEnvironmentPrefConstants.GO_ROOT.get(project);
		}
		
		// last ditch effort via a system environment variable
		if (goroot == null || "".equals(goroot)) {
			goroot = System.getenv(GoConstants.GOROOT);
		}
		
		// If null, we give up and just return the default...
		// which is essentially an empty string
		if (goroot == null) {
			return "";
		}
		
		return goroot;
	}
	
}
