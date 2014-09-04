package com.googlecode.goclipse;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import melnorme.utilbox.misc.MiscUtil;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IPreferencesService;

import com.googlecode.goclipse.builder.GoConstants;
import com.googlecode.goclipse.preferences.PreferenceConstants;

/**
 * Provides environmental utility methods for acquiring and storing a user
 * session's contextual data.
 * 
 */
public class Environment {

	private static final String PROJECT_ENABLE_AUTO_UNIT_TEST   = "com.googlecode.goclipse.environment.auto.unit.test";
	private static final String PROJECT_AUTO_UNIT_TEST_REGEX    = "com.googlecode.goclipse.environment.auto.unit.test.regex";
	private static final String PROJECT_AUTO_UNIT_TEST_MAX_TIME = "com.googlecode.goclipse.environment.auto.unit.test.max.time";

	public static final boolean DEBUG = Boolean.getBoolean("goclipse.debug");
	
	public static final Environment INSTANCE = new Environment();
	
	private final IPreferencesService preferences;

	private Map<String, Properties> propertiesMap = new HashMap<String, Properties>();
	
	/**
	 * 
	 */
	private Environment() {
		preferences = Platform.getPreferencesService();
	}

	/**
	 * @param project
	 * @return
	 */
	private Properties getProperties(IProject project) {
		
		Properties properties = propertiesMap.get(project.getName());
		
		if (properties == null) {
			properties = loadProperties(project);
			propertiesMap.put(project.getName(), properties);
		}
		
		return properties;
	}

	/**
	 * @param project
	 * @return
	 */
	private Properties loadProperties(IProject project) {
		
		Properties properties = new Properties();
		
		try {
			properties.loadFromXML(
				new FileInputStream(
					project.getWorkingLocation(Activator.PLUGIN_ID) + "/properties.xml"));
		} catch (InvalidPropertiesFormatException e) {
			Activator.logError(e);
		} catch (FileNotFoundException e) {
			IPath path = project.getWorkingLocation(Activator.PLUGIN_ID);
			try(FileOutputStream fos = new FileOutputStream(path.toOSString() + "/properties.xml", false)) {
				properties.storeToXML(fos, " this is a comment");
            } catch (FileNotFoundException e1) {
            	Activator.logError(e);
            } catch (IOException e1) {
            	Activator.logError(e);
            }
			saveProperties(project);
		} catch (IOException e) {
			Activator.logError(e);
		}
		
		return properties;
	}

	/**
	 * @param project
	 * @return
	 */
	private void saveProperties(IProject project) {
		if(project==null){
			Activator.logError("Null project given while atempting to save properties.");
			return;
		}
		
		Properties properties = getProperties(project);
		IPath path = project.getWorkingLocation(Activator.PLUGIN_ID);
		
		try {
			Activator.logInfo("writing to "
					+ path.toOSString()
					+ "/properties.xml");
			
			
			try(FileOutputStream fos = new FileOutputStream(path.toOSString() + "/properties.xml", false)) {
				properties.storeToXML(fos, project.getName()+" properties");
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the preferences
	 */
	public IPreferencesService getPreferences() {
		return preferences;
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
		
		// Project property takes precedence
		if (project != null) {
			goPath = getProperties(project).getProperty(GoConstants.GOPATH);
		}
		
		// Plug-in property comes next
		if (goPath == null || "".equals(goPath)) {
			goPath = PreferenceConstants.GO_PATH.get();
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
		
		// Project property takes precedence
		if (project != null) {
			goroot = getProperties(project).getProperty(GoConstants.GOROOT);
		}
		
		// Plug-in property comes next
		if (goroot == null || "".equals(goroot)) {
			goroot = PreferenceConstants.GO_ROOT.get();
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
	public void setAutoUnitTest(IProject project, boolean selection) {
		Properties properties = getProperties(project);
		
		// The boolean has to be converted to a string here, otherwise the
		// serialization to XML in the properties.storeToXML method
		// will not store properly.
		properties.put( PROJECT_ENABLE_AUTO_UNIT_TEST, "" + selection );
		saveProperties( project );
    }
	
	/**
	 * Informs if unit testing is enabled.
	 * @param project
	 * @return
	 */
	public boolean getAutoUnitTest(IProject project) {
		Properties properties = getProperties(project);
		Object b = properties.get(PROJECT_ENABLE_AUTO_UNIT_TEST);
		if(b instanceof String){
			return Boolean.parseBoolean(b.toString());
		}
		return false;
    }
	
	/**
	 * Defines the regex to use for the project properties.
	 * @param project
	 * @param string
	 * @param selection
	 */
	public void setAutoUnitTestRegex(IProject project, String regex) {
		Properties properties = getProperties(project);
		properties.put( PROJECT_AUTO_UNIT_TEST_REGEX, regex );
		saveProperties( project );
    }
	
	/**
	 * Returns the auto test regex from the project properties.
	 * @param project
	 * @return
	 */
	public String getAutoUnitTestRegex(IProject project) {
		Properties properties = getProperties(project);
		Object b = properties.get(PROJECT_AUTO_UNIT_TEST_REGEX);
		if(b==null || b.equals("")){
			return getAutoUnitTestRegexDefault();
		}
		return b.toString();
    }
	
	public String getAutoUnitTestRegexDefault() {
		return "TestAuto[A-Za-z0-9_]*";
	}
	
	/**
	 * Set the auto test max time from the project properties.
	 * @param project
	 * @param time
	 */
	public void setAutoUnitTestMaxTime(IProject project, int time) {
		Properties properties = getProperties(project);
		properties.put( PROJECT_AUTO_UNIT_TEST_MAX_TIME, "" + time );
		saveProperties( project );
    }
	
	/**
	 * Returns the auto test max time from the project properties.
	 * @param project
	 * @return
	 */
	public int getAutoUnitTestMaxTime(IProject project) {
		Properties properties = getProperties(project);
		Object b = properties.get(PROJECT_AUTO_UNIT_TEST_MAX_TIME);
		if(b==null){
			return getAutoUnitTestMaxTimeDefault();
		}
		return Integer.parseInt(b.toString());
    }
	
	public int getAutoUnitTestMaxTimeDefault() {
		return 5000;
	}
	
}
