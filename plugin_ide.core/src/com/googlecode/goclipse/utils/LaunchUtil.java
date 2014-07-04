package com.googlecode.goclipse.utils;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;

import com.googlecode.goclipse.Environment;

public class LaunchUtil {
	
	/**
	 * @param resourceRelativePath
	 * @return
	 */
	public static String getCmdName(IPath resourceRelativePath) {
		IPath srcFolderPath = resourceRelativePath.removeLastSegments(1);
		String cmdName;
		// if it's directly in the cmd folder, name it by filename, else name it
		// by folder name
		if (Environment.INSTANCE.getDefaultCmdSourceFolder().equals(srcFolderPath)) {
			cmdName = resourceRelativePath.removeFileExtension().lastSegment();
		} else {
			cmdName = srcFolderPath.lastSegment();
		}
		return cmdName;
	}
	
	/**
	 * @param cmdName
	 * @param project
	 * @return
	 */
	public static IPath getExecutablePath(String cmdName, IProject project) {
		Environment instance = Environment.INSTANCE;
		IPath binOutputFolder = instance.getBinOutputFolder(project);
		IPath executablePath = binOutputFolder.append(cmdName + instance.getExecutableExtension());
		return executablePath;
	}
}
