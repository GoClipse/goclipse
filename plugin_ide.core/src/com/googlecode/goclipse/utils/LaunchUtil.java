package com.googlecode.goclipse.utils;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;

import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.core.GoWorkspace;

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
		if (Environment.getDefaultCmdSourceFolder().equals(srcFolderPath)) {
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
		IPath binOutputFolder = new GoWorkspace(project).getBinFolderRelativePath();
		return binOutputFolder.append(cmdName + Environment.getExecutableExtension());
	}
}
