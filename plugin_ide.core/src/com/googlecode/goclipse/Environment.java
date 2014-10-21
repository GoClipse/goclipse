package com.googlecode.goclipse;

import static melnorme.utilbox.core.CoreUtil.array;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import com.googlecode.goclipse.tooling.env.GoEnvironment;

/** FIXME: Need to review an rewrite users of this class, since this likely breaks
 * with the neew {@link GoEnvironment} API that allows projects contained in the GOPATH. */
@Deprecated
public class Environment {
	
	private Environment() {
	}
	
	public static String[] getSourceFoldersAsStringArray(IProject project) {
		return array("src");
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
	
	public static IPath getDefaultPkgSourceFolder() {
		return Path.fromOSString("src");
	}
	
	public static boolean isPkgFile(IPath path) {
		return getDefaultPkgSourceFolder().isPrefixOf(path);
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
	
}
