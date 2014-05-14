package com.googlecode.goclipse.preferences;

import java.util.ArrayList;
import java.util.List;

import melnorme.utilbox.misc.MiscUtil;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import com.googlecode.goclipse.core.GoCore;


/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	private static final String OS_ARCH = "os.arch";

	@Override
  public void initializeDefaultPreferences() {
		
		IEclipsePreferences coreDefaults = DefaultScope.INSTANCE.getNode(GoCore.PLUGIN_ID);
		
		if (MiscUtil.OS_IS_WINDOWS){
			coreDefaults.put(PreferenceConstants.GOOS, PreferenceConstants.OS_WINDOWS);
		} else if (MiscUtil.OS_IS_LINUX) {
			coreDefaults.put(PreferenceConstants.GOOS, PreferenceConstants.OS_LINUX);
		} else if (MiscUtil.OS_IS_MAC) {
			coreDefaults.put(PreferenceConstants.GOOS, PreferenceConstants.OS_DARWIN);
		}

		if (isAMD64()){
			coreDefaults.put(PreferenceConstants.GOARCH, PreferenceConstants.ARCH_AMD64);
		} else if (is386()){
			coreDefaults.put(PreferenceConstants.GOARCH, PreferenceConstants.ARCH_386);
		}
		
	}
	
  private static boolean isAMD64() {
		String osProp = System.getProperty(OS_ARCH);
		return "x86_64".equals(osProp) || "amd64".equals(osProp);
	}

	private static boolean is386() {
		String osProp = System.getProperty(OS_ARCH);
		return "i386".equals(osProp) || "x86".equals(osProp) || "i686".equals(osProp);
	}
	
	public static String getDefaultPlatformChar() {
		if (isAMD64()){
			return "6";
		} else if (is386()) {
			return "8";
		}
		return null;
	}
	
	public static String getDefaultCompilerName() {
		String platformChar = getDefaultPlatformChar();
		if (platformChar == null){
			return null;
		}
		return "go"+(MiscUtil.OS_IS_WINDOWS?".exe":"");
	}
	
	// TODO refactor this out
	protected static String get32bitCompilerName() {
		return "go"+(MiscUtil.OS_IS_WINDOWS?".exe":"");
	}
	
	public static List<String> getSupportedCompilerNames() {
	  List<String> names = new ArrayList<String>();
	  
	  String defaultCompiler = getDefaultCompilerName();
	  
	  if (defaultCompiler != null) {
	    names.add(defaultCompiler);
	  }
	  
	  if (isAMD64()) {
	    String altCompiler = get32bitCompilerName();
	    
	    if (altCompiler != null) {
	      names.add(altCompiler);
	    }
	  }
	  
	  return names;
	}
	
	public static String getDefaultGodocName() {
		return "godoc" +(MiscUtil.OS_IS_WINDOWS?".exe":"");
	}

	public static String getDefaultGofmtName() {
		return "gofmt" +(MiscUtil.OS_IS_WINDOWS?".exe":"");
	}

}
