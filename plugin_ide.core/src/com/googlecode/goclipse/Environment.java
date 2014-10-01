package com.googlecode.goclipse;

import java.util.ArrayList;
import java.util.List;
import melnorme.utilbox.misc.MiscUtil;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import com.googlecode.goclipse.tooling.GoFileNaming;

/**
 * Provides environmental utility methods for acquiring and storing a user
 * session's contextual data.
 * 
 */
@Deprecated
public class Environment {
	
	private Environment() {
	}
	
	public static String[] getSourceFoldersAsStringArray(IProject project) {
		return new String[] {"src"};
	}
	
	/**
	 * Return a list of source folders for the currently active project.
	 * 
	 * @return
	 */
	public static List<IFolder> getSourceFolders(IProject project) {
		List<IFolder> result = new ArrayList<IFolder>();
		
		for (String path : getSourceFoldersAsStringArray(project)) {
			IResource resource = project.findMember(path);
			
			if (resource instanceof IFolder) {
				result.add((IFolder)resource);
			}
			
		}
		
		return result;
	}
	
	public static IPath getDefaultCmdSourceFolder() {
		return Path.fromOSString("src");
	}
	
	public static IPath getDefaultPkgSourceFolder() {
		return Path.fromOSString("src");
	}
	
	/**
	 * @param path
	 * @return
	 */
	public static boolean isCmdFile(IPath path) {
		return getDefaultCmdSourceFolder().isPrefixOf(path);
	}
	
	/**
	 * @param path
	 * @return
	 */
	public static boolean isPkgFile(IPath path) {
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
	public static boolean isCmdSrcFolder(IProject project, IFolder folder){
		
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
	public static boolean isSourceFile(IProject project, IFile file) {
		IPath p = file.getProjectRelativePath();
		IResource res = project.findMember(p);
		if ( res==null ) {
			return false;
		}
		
		if ( !file.getName().endsWith(GoFileNaming.GO_SOURCE_FILE_EXTENSION) ) {
			return false;
		}
		
		for (IFolder folder : getSourceFolders(project)) {
			if(file.getLocation().toOSString().startsWith(folder.getLocation().toOSString())) {
				return true;
			}
		}
		
		return false;
	}
	
}
