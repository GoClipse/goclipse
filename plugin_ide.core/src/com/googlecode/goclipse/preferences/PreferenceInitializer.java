package com.googlecode.goclipse.preferences;

import java.util.ArrayList;
import java.util.List;

import melnorme.utilbox.misc.MiscUtil;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;

import com.googlecode.goclipse.core.GoEnvironmentPrefUtils;


/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {
	
	@Override
	public void initializeDefaultPreferences() {
	}
	
	public static String getDefaultPlatformChar() {
		if (GoEnvironmentPrefUtils.isAMD64()){
			return "6";
		} else if (GoEnvironmentPrefUtils.is386()) {
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
	  
	  if (GoEnvironmentPrefUtils.isAMD64()) {
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
