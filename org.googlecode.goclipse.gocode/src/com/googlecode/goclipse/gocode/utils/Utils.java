package com.googlecode.goclipse.gocode.utils;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;

public class Utils {

  private Utils() {
    
  }
  
  public static boolean isWindows() {
    return Platform.getOS().startsWith("win");
  }
  
  public static boolean isMacOS() {
    return Platform.getOS().equals(Platform.OS_MACOSX);
  }

  public static boolean isLinux() {
    return Platform.getOS().equals(Platform.OS_LINUX);
  }
  
  public static boolean pathExists(IPath path) {
    if (path == null || path.isEmpty()) {
      return false;
    }
    
    File file = path.toFile();
    
    return file.exists();
  }
  
  public static boolean is64Bit() {
    String arch = System.getProperty("os.arch");

    return "x86_64".equals(arch) || "amd64".equals(arch);
  }

  public static void ensureExecutable(IPath path) {
    if (pathExists(path)) {
      File file = path.toFile();
      
      if (!file.canExecute()) {
        if (!file.setExecutable(true)) {
          file.setExecutable(true, true);
        }
      }
    }
  }
  
}
