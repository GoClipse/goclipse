package com.googlecode.goclipse.core.tools;

import java.io.File;

import melnorme.utilbox.misc.MiscUtil;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;

import com.googlecode.goclipse.builder.MarkerUtilities;
import com.googlecode.goclipse.builder.StreamAsLines;
import com.googlecode.goclipse.tooling.GoFileNaming;

/**
 * @author ??? - Original author?
 * @author Bruno - cleanup refactoring of external tool calling
 */
public class GoBuildOuputProcessor {

	public static int processCompileOutput(IProject project, final StreamAsLines output,
			                          final String        relativeTargetDir,
			                          final IFile         file) {
		int errorCount = 0;
		boolean iswindows = MiscUtil.OS_IS_WINDOWS;
		
		for (String line : output.getLines()) {
			
			if (line.startsWith("#")) {
				continue;
				
			} else if(line.startsWith("can't load package:")) {
				/*
				 * when building a main package mixed with a
				 * lib package this error occurs and is not
				 * specific to any one file.  It is related
				 * to the organization of the project.
				 */
				IContainer container = file.getParent();
				if(container instanceof IFolder){
					IFolder folder = (IFolder)container;
					MarkerUtilities.addMarker(folder, 0, line, IMarker.SEVERITY_ERROR);
					errorCount++;
				}
				continue;
			}
			
			int goPos = line.indexOf(GoFileNaming.GO_SOURCE_FILE_EXTENSION);
	
			if (goPos > 0) {
				
				int fileNameLength = goPos + GoFileNaming.GO_SOURCE_FILE_EXTENSION.length();
	
				// Strip the prefix off the error message
				String fileName = line.substring(0, fileNameLength);
				fileName = fileName.replace(project.getLocation().toOSString(), "");
				fileName = iswindows?fileName:fileName.substring(fileName.indexOf(":") + 1).trim();
	
				// Determine the type of error message
				if (fileName.startsWith(File.separator)) {
					fileName = fileName.substring(1);
					
				} else if (fileName.startsWith("." + File.separator)) {
					fileName = relativeTargetDir.substring(1) + File.separator + fileName.substring(2);
					
				} else if (line.startsWith("can't")) {
					fileName = relativeTargetDir.substring(1);
					
				} else {
					fileName = relativeTargetDir.substring(1) + File.separator + fileName;
				}
				
				// find the resource if possible
				IResource resource = project.findMember(fileName);
				if (resource == null && file != null) {
					resource = file;
					
				} else if (resource == null) {
					resource = project;
				}
	
				// Create the error message
				String msg = line.substring(fileNameLength + 1);
				String[] str = msg.split(":", 3);
				int location = -1; // marker for trouble
				int messageStart = msg.indexOf(": ");
	
				try {
					location = Integer.parseInt(str[0]);
				} catch (NumberFormatException nfe) {}
				
				// Determine how to mark the message
				if (location != -1 && messageStart != -1) {
					String message = msg.substring(messageStart + 2);
					MarkerUtilities.addMarker(resource, location, message, IMarker.SEVERITY_ERROR);
					errorCount++;
					
				} else {
					// play safe. to show something in UI
					MarkerUtilities.addMarker(resource, 1, line, IMarker.SEVERITY_ERROR);
					errorCount++;
				}
				
			} else {
				// runtime.main: undefined: main.main
				MarkerUtilities.addMarker(file, 1, line, IMarker.SEVERITY_ERROR);
				errorCount++;
			}
		}
		
		return errorCount;
	}
	
}