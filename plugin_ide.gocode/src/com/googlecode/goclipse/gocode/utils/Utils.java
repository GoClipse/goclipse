package com.googlecode.goclipse.gocode.utils;

import java.io.File;

import org.eclipse.core.runtime.IPath;

public class Utils {
	
	private Utils() {
		
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