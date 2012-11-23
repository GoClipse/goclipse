package com.googlecode.goclipse.debug.utils;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.preferences.PreferenceConstants;

import java.io.IOException;

//gdb --version prints the intro text + version and quits
//GNU gdb 6.3.50-20050815 (Apple version gdb-1822) (Sun Aug 5 03:00:42 UTC 2012)
//GNU gdb (GDB) 7.5

public class GDBUtils {
  public static final double MIN_VERSION = 7.1;
  
  private GDBUtils() {
    
  }
  
  public static Double getGDBVersion() {
    return getGDBVersion(getGDBPath());
  }
  
  public static Double getGDBVersion(String gdbPath) {
    if (gdbPath == null) {
      return null;
    }
    
    ProcessRunner runner = new ProcessRunner(gdbPath, "--version");
    
    try {
      int exitCode = runner.execute();
      
      if (exitCode == 0) {
        String[] stdout = runner.getStdout().split("\n");
        
        if (stdout.length > 0) {
          return parseGDBVersion(stdout[0]);
        }
      }
    } catch (IOException e) {
      
    }
    
    return null;
  }

  public static Double parseGDBVersion(String verString) {
    //GNU gdb 6.3.50-20050815 (Apple version gdb-1822) (Sun Aug 5 03:00:42 UTC 2012)
    //GNU gdb (GDB) 7.5
    
    verString = removeParens(verString.trim());
    
    String[] words = verString.split("\\s");
    
    for (int i = 0; i < words.length; i++) {
      if (words[i].equals("gdb")) {
        if (i + 1 < words.length) {
          String val = words[i + 1];
          
          val = cleanUp(val);
          
          try {
            return Double.parseDouble(val);
          } catch (NumberFormatException e) {
            return null;
          }
        }
      }
    }
    
    return null;
  }

  // 6.3.50-20050815 ==> 6.3
  private static String cleanUp(String val) {
    int index = val.indexOf('-');
    
    if (index != -1) {
      val = val.substring(0, index);
    }
    
    index = val.indexOf('.');
    
    if (index != -1) {
      // check for a second '.'
      index = val.indexOf('.', index + 1);
      
      if (index != -1) {
        val = val.substring(0, index);
      }
    }
    
    return val;
  }

  private static String removeParens(String str) {
    int index = str.indexOf('(');
    
    while (index != -1) {
      int match = str.indexOf(')', index);
      
      if (match == -1) {
        return str;
      }
      
      // remove everything between '(' and ')'
      str = str.substring(0, index) + str.substring(match + 1);
      
      index = str.indexOf('(');
    }
    
    return str.replaceAll("  ", " ");
  }

  public static String getGDBPath() {
    String path = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.GDB_PATH);
    
    if (path != null && path.length() == 0) {
      return null;
    }
    
    return path;
  }
  
}
