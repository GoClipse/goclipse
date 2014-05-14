package com.googlecode.goclipse.preferences;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.jface.util.Util;
import org.eclipse.swt.SWT;

import com.googlecode.goclipse.core.GoCore;
import com.googlecode.goclipse.editors.IColorConstants;


/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	private static final String OS_ARCH = "os.arch";

	@Override
  public void initializeDefaultPreferences() {
		
		IEclipsePreferences coreDefaults = DefaultScope.INSTANCE.getNode(GoCore.PLUGIN_ID);
		
		coreDefaults.putBoolean(PreferenceConstants.FIELD_USE_HIGHLIGHTING, true);
		coreDefaults.put(PreferenceConstants.FIELD_SYNTAX_KEYWORD_COLOR, StringConverter.asString(IColorConstants.KEYWORD));
		coreDefaults.put(PreferenceConstants.FIELD_SYNTAX_VALUE_COLOR, StringConverter.asString(IColorConstants.VALUE));
		coreDefaults.put(PreferenceConstants.FIELD_SYNTAX_PRIMITIVE_COLOR, StringConverter.asString(IColorConstants.PRIMITIVE));
		coreDefaults.put(PreferenceConstants.FIELD_SYNTAX_COMMENT_COLOR, StringConverter.asString(IColorConstants.COMMENT));
		coreDefaults.put(PreferenceConstants.FIELD_SYNTAX_BUILTIN_FUNCTION_COLOR, StringConverter.asString(IColorConstants.BUILTIN_FUNCTION));
		coreDefaults.put(PreferenceConstants.FIELD_SYNTAX_STRING_COLOR, StringConverter.asString(IColorConstants.STRING));
		coreDefaults.put(PreferenceConstants.FIELD_SYNTAX_MULTILINE_STRING_COLOR, StringConverter.asString(IColorConstants.MULTILINE_STRING));
		coreDefaults.putInt(PreferenceConstants.FIELD_SYNTAX_KEYWORD_STYLE,          SWT.BOLD           );
		coreDefaults.putInt(PreferenceConstants.FIELD_SYNTAX_VALUE_STYLE,            SWT.BOLD|SWT.ITALIC);
		coreDefaults.putInt(PreferenceConstants.FIELD_SYNTAX_PRIMITIVE_STYLE,        SWT.ITALIC         );
		coreDefaults.putInt(PreferenceConstants.FIELD_SYNTAX_COMMENT_STYLE,          SWT.NORMAL         );
		coreDefaults.putInt(PreferenceConstants.FIELD_SYNTAX_BUILTIN_FUNCTION_STYLE, SWT.BOLD           );
		coreDefaults.putInt(PreferenceConstants.FIELD_SYNTAX_STRING_STYLE,           SWT.NORMAL         );
		coreDefaults.putInt(PreferenceConstants.FIELD_SYNTAX_MULTILINE_STRING_STYLE, SWT.NORMAL         );

		if (Util.isWindows()){
			coreDefaults.put(PreferenceConstants.GOOS, PreferenceConstants.OS_WINDOWS);
		} else if (Util.isLinux()) {
			coreDefaults.put(PreferenceConstants.GOOS, PreferenceConstants.OS_LINUX);
		} else if (Util.isMac()) {
			coreDefaults.put(PreferenceConstants.GOOS, PreferenceConstants.OS_DARWIN);
		}

		if (isAMD64()){
			coreDefaults.put(PreferenceConstants.GOARCH, PreferenceConstants.ARCH_AMD64);
		} else if (is386()){
			coreDefaults.put(PreferenceConstants.GOARCH, PreferenceConstants.ARCH_386);
		}
		
		tryAndDiscoverGDB(coreDefaults);
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
		return "go"+(Util.isWindows()?".exe":"");
	}
	
	// TODO refactor this out
	protected static String get32bitCompilerName() {
		return "go"+(Util.isWindows()?".exe":"");
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
		return "godoc" +(Util.isWindows()?".exe":"");
	}

	public static String getDefaultGofmtName() {
		return "gofmt" +(Util.isWindows()?".exe":"");
	}

  private void tryAndDiscoverGDB(IEclipsePreferences coreDefault) {
    final String[] possiblePaths = {
        "/opt/local/bin/fsf-gdb",
        "/usr/local/bin/gdb",
        "/Developer/usr/bin/gdb",
        "/usr/bin/gdb"
    };
    
    for (String path : possiblePaths) {
      File file = new File(path);
      
      if (file.exists() && file.canExecute()) {
        coreDefault.put(PreferenceConstants.GDB_PATH, path);
        
        return;
      }
    }
  }

}
