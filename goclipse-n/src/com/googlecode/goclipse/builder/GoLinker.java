package com.googlecode.goclipse.builder;

import org.eclipse.core.resources.IProject;

public class GoLinker {

	void createExecutable(IProject prj, String target, String[] dependencies) {
//		if (!GoBuilder.dependenciesExist(prj, dependencies)){
//			Activator.logWarning("Missing dependency for '"+target+"' not compiling");
//			return;
//		}
//
//		IFile res;
//		if (dependencies.length > 0){
//			res = prj.getFile(dependencies[0]);
//		} else {
//			res = prj.getFile(target);
//		}
//		final IPath prjLoc = prj.getLocation();
//
//		// do linker
//		String linkerPath = Activator.getDefault().getPreferenceStore()
//				.getString(PreferenceConstants.LINKER_PATH);
//		ExternalCommand link = new ExternalCommand(linkerPath);
//		link.setEnvironment(GoConstants.environment());
//		link.setWorkingFolder(prjLoc.toOSString());
//		StreamAsLines output = new StreamAsLines();
//		link.setResultsFilter(output);
//		List<String> args = new ArrayList<String>();
//
//		link.setCommand(linkerPath);
//		args.clear();
//		args.add(GoConstants.COMPILER_OPTION_L);
//		args.add(Environment.INSTANCE.getPkgOutputFolder(prj).toOSString());
//		args.add(GoConstants.COMPILER_OPTION_O);
//		args.add(target);
//		for (String dependency : dependencies) {
//			args.add(dependency);
//		}
//		String rez = link.execute(args);
//		if (rez != null) {
//			MarkerUtilities.addMarker(res, rez);
//		}
//		for (String line : output.getLines()) {
//			MarkerUtilities.addMarker(res, line);
//		}
//		try {
//			prj.refreshLocal(IResource.DEPTH_INFINITE, null);
//		} catch (CoreException e) {
//			MarkerUtilities.addMarker(prj, e.getLocalizedMessage());
//		}
	}
	
}
