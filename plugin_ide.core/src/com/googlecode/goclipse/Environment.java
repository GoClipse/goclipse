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
import org.osgi.service.prefs.BackingStoreException;

import com.googlecode.goclipse.builder.GoConstants;
import com.googlecode.goclipse.core.GoProjectPrefConstants;
import com.googlecode.goclipse.preferences.PreferenceConstants;

/**
 * Provides environmental utility methods for acquiring and storing a user
 * session's contextual data.
 * 
 */
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
			goPath = PreferenceConstants.GO_PATH.get(project);
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
		
		if (MiscUtil.OS_IS_WINDOWS) {
		  if (goPath.contains(";")) {
		    path = goPath.split(";");
		  } else {
		    path[0] = goPath;
		  }
		} else {
		  if (goPath.contains(":")) {
		    path =  goPath.split(":");
		  } else {
        path[0] = goPath;
		  }
		}

		return path;
	}

	public String getGoRoot(IProject project) {
		String goroot = null;
		
		// Plug-in property comes next
		if (goroot == null || "".equals(goroot)) {
			goroot = PreferenceConstants.GO_ROOT.get(project);
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

	/**
	 * Turns on auto unit testing for the project
	 * @param project
	 * @param string
	 * @param selection
	 */
	public void setAutoUnitTest(IProject project, boolean selection) throws BackingStoreException {
		GoProjectPrefConstants.PROJECT_ENABLE_AUTO_UNIT_TEST.set(project, selection);
    }
	
	/**
	 * Informs if unit testing is enabled.
	 * @param project
	 * @return
	 */
	public boolean getAutoUnitTest(IProject project) {
		return GoProjectPrefConstants.PROJECT_ENABLE_AUTO_UNIT_TEST.get(project);
    }
	
	/**
	 * Defines the regex to use for the project properties.
	 * @param project
	 * @param string
	 * @param selection
	 */
	public void setAutoUnitTestRegex(IProject project, String regex) throws BackingStoreException {
		GoProjectPrefConstants.PROJECT_AUTO_UNIT_TEST_REGEX.set(project, regex);
    }
	
	/**
	 * Returns the auto test regex from the project properties.
	 * @param project
	 * @return
	 */
	public String getAutoUnitTestRegex(IProject project) {
		return GoProjectPrefConstants.PROJECT_AUTO_UNIT_TEST_REGEX.get(project);
    }
	
	public static String getAutoUnitTestRegexDefault() {
		return "TestAuto[A-Za-z0-9_]*";
	}
	
	/**
	 * Set the auto test max time from the project properties.
	 * @param project
	 * @param time
	 */
	public void setAutoUnitTestMaxTime(IProject project, int time) throws BackingStoreException {
		GoProjectPrefConstants.PROJECT_AUTO_UNIT_TEST_MAX_TIME.set(project, time);
    }
	
	/**
	 * Returns the auto test max time from the project properties.
	 * @param project
	 * @return
	 */
	public int getAutoUnitTestMaxTime(IProject project) {
		return GoProjectPrefConstants.PROJECT_AUTO_UNIT_TEST_MAX_TIME.get(project);
    }
	
	public static int getAutoUnitTestMaxTimeDefault() {
		return 5000;
	}
	
}
