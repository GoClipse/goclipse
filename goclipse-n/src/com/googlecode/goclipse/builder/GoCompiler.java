package com.googlecode.goclipse.builder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubMonitor;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.SysUtils;
import com.googlecode.goclipse.preferences.PreferenceConstants;

public class GoCompiler {
	private GoDependencyManager dependencyManager;
	private Map<String, String> env; //environment for build

	public GoCompiler(GoDependencyManager dependencyManager) {
		this.dependencyManager = dependencyManager;
	}

	public void remove(List<IResource> toRemove, IProject project, IProgressMonitor pmonitor) {
		if(toRemove == null || toRemove.isEmpty()) {
			return;
		}
		SubMonitor monitor = SubMonitor.convert(pmonitor, toRemove.size());
		SysUtils.debug("remove():" + toRemove);
		String binRel = Environment.INSTANCE.getOutputFolder(project);
		IPath exeBase = project.getLocation().append(binRel).append(
				GoConstants.EXE_FILE_DIRECTORY);
		IPath objBase = project.getLocation().append(binRel).append(
				GoConstants.OBJ_FILE_DIRECTORY);
		File objFolder = new File(objBase.toOSString());
		File exeFolder = new File(exeBase.toOSString());
		String goarch = Activator.getDefault().getPreferenceStore().getString(
				PreferenceConstants.GOARCH);
		Arch arch = Arch.getArch(goarch);
		try {
			for (IResource res : toRemove) {
				if (res.exists()) {
					//does not exist in remove incremental
					res.deleteMarkers(IMarker.PROBLEM, false,IResource.DEPTH_INFINITE);
				}
				IPath rpath = res.getProjectRelativePath().removeFirstSegments(1);
				if (objFolder.exists()) {
					String oPath =objBase.append(rpath).removeFileExtension().toOSString() + arch.getExtension();
					File oFile = new File(oPath);
					if (oFile.exists()) {
						SysUtils.debug("deleting" + oFile);
						oFile.delete();
					}
					
				}
				if (exeFolder.exists()) {
					String ePath =exeBase.append(rpath).removeFileExtension().toOSString() + arch.getExecutableExt();
					File eFile = new File(ePath);
					if (eFile.exists()) {
						SysUtils.debug("deleting" + eFile);
						eFile.delete();
					}
					
				}
			}
			project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
			
		}catch(CoreException ce) {
			SysUtils.severe(ce);
		}
		SysUtils.debug("remove() - done");
	}

	public void compile(List<String> paths, IProject project,
			IProgressMonitor pmonitor) {
		SubMonitor monitor = SubMonitor.convert(pmonitor, 130);
		SysUtils.debug("compile():" + paths);
		List<Package> sorted = dependencyManager.sortForCompile(paths, monitor.newChild(30));
		IPath prjLoc = project.getLocation();
		String compilerPath = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.COMPILER_PATH);
	    String goarch = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.GOARCH);
		String outputFolder = Environment.INSTANCE.getOutputFolder(project);
		IPath outputPath = Path.fromOSString(outputFolder);
		IPath objPath = outputPath.append(GoConstants.OBJ_FILE_DIRECTORY);
		ExternalCommand compilePackageCmd = new ExternalCommand(compilerPath);
		compilePackageCmd.setEnvironment(env);
		List<String> args = new ArrayList<String>();
		// get the architecture
	    Arch arch = Arch.getArch(goarch);
	    StreamAsLines output = new StreamAsLines();
		compilePackageCmd.setResultsFilter(output);
		for(Package p: sorted) {
			SysUtils.debug("building package: " + p.name + " in " + p.relPath);
			List<String> param = new ArrayList<String>(p.files);
			//to compile a package, give the compiler all files in the package
			//also rotate all files to arrive in the first position (so that it will generate an obj for each)
			for (String file : p.files) {
				param.remove(file);
				param.add(0, file);
				args.clear();
				//show all errors option
				args.add(GoConstants.COMPILER_OPTION_E);
				//include folder
				args.add(GoConstants.COMPILER_OPTION_I);
				args.add(prjLoc.append(objPath).toOSString());
				
				//output file option
				args.add(GoConstants.COMPILER_OPTION_O);
				//output file
				IPath packageRelSrcPath = Path.fromOSString(p.relPath);
				IPath packageOutPath = prjLoc.append(objPath.append(packageRelSrcPath.removeFirstSegments(1)));
				String spOutPath = packageOutPath.toOSString();
				File opf = new File(spOutPath);
				if (!opf.exists()) {
					opf.mkdirs();
				}
				
				String outFile = packageOutPath.append(file).removeFileExtension().toOSString() + arch.getExtension(); //for compiler
				args.add(outFile);
				for (String fName: param) {
					args.add(prjLoc.append(packageRelSrcPath).append(fName).toOSString());
				}
				
				compilePackageCmd.setWorkingFolder(spOutPath);
				String srcFilePath = packageRelSrcPath.append(file).toOSString();
				IResource resource = project.findMember(srcFilePath);
				deleteMarkers(resource);
				String result = compilePackageCmd.execute(args);
				List<CompilerInfo> infos = new ArrayList<CompilerInfo>();
				if (result != null) {
					CompilerInfo ci = new CompilerInfo(resource, 0, result);
					infos.add(ci);
				}
				infos.addAll(processCompileOutput(resource , output));
				for (CompilerInfo info : infos) {
					// ErrorLocator.INSTANCE.locateError(lines, info);
					addMarker(resource, info, IMarker.SEVERITY_ERROR);
				}
				
			}
		}
		try {
			project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		private List<CompilerInfo> processCompileOutput(IResource resource, StreamAsLines output) {
		List<CompilerInfo> errors = new ArrayList<CompilerInfo>();
		for (String line: output.getLines()) {
	         SysUtils.debug(line);
	         int goPos = line.indexOf(GoConstants.GO_SOURCE_FILE_EXTENSION);
	         if (goPos == -1) {
	        	 errors.add(new CompilerInfo(resource, 0, "no go extension"));
	        	 errors.add(new CompilerInfo(resource, 0, line)); //show the line
	        	 SysUtils.debug("no .go extension for file? this should not happen");	 
	         } else {
	        	 line = line.substring(goPos + GoConstants.GO_SOURCE_FILE_EXTENSION.length() + 1);
	        	 String[] str = line.split(":", 2);
	        	 int location = -1; //marker for trouble
	        	 try {
	        		 location = Integer.parseInt(str[0]);
	        	 }catch(NumberFormatException nfe) {        		 
	        	 }
	             if (location != -1 && str.length > 1) {
	                errors.add(new CompilerInfo(resource, Integer.parseInt(str[0]), str[1].trim()));
	             } else {
	            	 //play safe. to show something in UI
	            	errors.add(new CompilerInfo(resource, 0, line)); 
	             }
	         }
	         
	      }
		return errors;
	}

	private void addMarker(IResource file, CompilerInfo info, int severity) {
		if (file == null || !file.exists()) {
			return;
		}
		try {
			IMarker marker = file.createMarker(IMarker.PROBLEM);
			marker.setAttribute(IMarker.MESSAGE, info.message);
			marker.setAttribute(IMarker.SEVERITY, severity);
			if (info.line == -1) {
				info.line = 1;
			}
			marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
			marker.setAttribute(IMarker.LINE_NUMBER, info.line);

			// find error type to mark location (experimental)
			if (info.isLocationFound) {
				System.out.println("> " + info.begin + ", " + info.end);
				marker.setAttribute(IMarker.CHAR_START, info.begin);
				marker.setAttribute(IMarker.CHAR_END, info.end);
			}

		} catch (CoreException e) {
			SysUtils.debug(e);
		}
	}

	private void deleteMarkers(IResource file) {
		try {
			if (file != null && file.exists()) {
				file.deleteMarkers(IMarker.PROBLEM, false, IResource.DEPTH_ZERO);
			}
		} catch (CoreException ce) {
			SysUtils.debug(ce);
		}
	}

	public void setEnvironment(Map<String, String> goEnv) {
		this.env = goEnv;
		
	}
}
