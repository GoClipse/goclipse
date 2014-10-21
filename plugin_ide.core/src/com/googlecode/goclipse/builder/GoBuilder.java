package com.googlecode.goclipse.builder;

import java.io.File;
import java.util.Collection;
import java.util.Map;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.LangProjectBuilder;
import melnorme.lang.tooling.ops.ToolSourceError;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.MiscUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.core.GoCoreMessages;
import com.googlecode.goclipse.core.GoEnvironmentPrefs;
import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.core.GoProjectPrefConstants;
import com.googlecode.goclipse.core.operations.GoToolManager;
import com.googlecode.goclipse.tooling.GoBuildOutputProcessor;
import com.googlecode.goclipse.tooling.GoPackageName;
import com.googlecode.goclipse.tooling.env.GoEnvironment;

public class GoBuilder extends LangProjectBuilder {
	
	public  static final String  BUILDER_ID = "com.googlecode.goclipse.goBuilder";
	
	public GoBuilder() {
	}
	
	@Override
	protected String getBuildProblemId() {
		return MarkerUtilities.MARKER_ID;
	}
	
	@Override
	protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor) throws CoreException {
		return super.build(kind, args, monitor);
	}
	
	private boolean checkBuild(IProject project) throws CoreException {
		
		if (!GoProjectEnvironment.isValid(project)){
			MarkerUtilities.addMarker(getProject(), GoCoreMessages.INVALID_PREFERENCES_MESSAGE);
			return false;
			
		} else {
			return true;
		}
	}
	
	@Override
	protected IProject[] doBuild(final IProject project, int kind, Map<String, String> args, IProgressMonitor monitor)
			throws CoreException {
		
 		if (!checkBuild(project)) {
			return null;
		}
 		
		GoToolManager.getDefault().notifyBuildStarting(project);
		
		try {
			doBuildAll(project, monitor);
		} 
		catch (CoreException ce) {
			if(!monitor.isCanceled()) {
				LangCore.logStatus(ce.getStatus());
			}
			
			forgetLastBuiltState();
			throw ce; // Note: if monitor is cancelled, exception will be ignored.
		} 
		finally {
			getProject().refreshLocal(IResource.DEPTH_INFINITE, monitor);
			
			GoToolManager.getDefault().notifyBuildTerminated(project);
		}
		
		// no project dependencies (yet)
		return null;
	}
	
	protected void doBuildAll(final IProject project, IProgressMonitor monitor) throws CoreException {
		
		GoEnvironment goEnvironment = GoProjectEnvironment.getGoEnvironment(project);
		String compilerPath = GoEnvironmentPrefs.COMPILER_PATH.get();
		
		if(compilerPath.isEmpty()) {
			throw LangCore.createCoreException("Compiler Path not defined.", null);
		}
		ArrayList2<String> goBuildCmdLine = new ArrayList2<>(compilerPath, "install", "-v");
		
		goBuildCmdLine.addElements(GoProjectPrefConstants.GO_BUILD_EXTRA_OPTIONS.getParsedArguments(project));
		
		Collection<GoPackageName> sourcePackages = GoProjectEnvironment.getSourcePackages(project, goEnvironment);
		for (GoPackageName goPackageName : sourcePackages) {
			goBuildCmdLine.add(goPackageName.getFullNameAsString());
		}
		
		ExternalProcessResult buildAllResult = GoToolManager.getDefault().runBuildTool(goEnvironment, project, monitor,
			project.getLocation().toFile(), goBuildCmdLine);
		
		GoBuildOutputProcessor buildOutput = new GoBuildOutputProcessor(MiscUtil.createValidPath("")) {
			@Override
			protected void handleParseError(CommonException ce) {
				LangCore.logError(ce.getMessage(), ce.getCause());
			}
		};
		buildOutput.parseOutput(buildAllResult);
		
		ArrayList2<ToolSourceError> buildErrors = buildOutput.getBuildErrors();
		addErrorMarkers(buildErrors);
	}
	
	@Override
	protected void clean(IProgressMonitor monitor) throws CoreException {
		IProject project = getProject();
		
		MarkerUtilities.deleteAllMarkers(project);
		
		File binFolder = GoProjectEnvironment.getBinFolder(project).toFile();
		if (binFolder.exists()) {
			deleteFolder(binFolder, true);
		}
		
		File pkgFolder = GoProjectEnvironment.getPkgFolder(project).toFile();
		if (pkgFolder.exists()) {
			deleteFolder(pkgFolder, true);
		}
		
		project.accept(new IResourceVisitor() {
			@Override
			public boolean visit(IResource resource) throws CoreException {
				IPath relativePath = resource.getProjectRelativePath();
				String lastSegment = relativePath.lastSegment();
				IPath rawLocation = resource.getRawLocation();
				if (rawLocation != null) {
					File file = rawLocation.toFile();
					if (file.exists() && file.isDirectory() 
							&& (Environment.isPkgFile(relativePath)) 
							&& (lastSegment.equals(GoConstants.OBJ_FILE_DIRECTORY) || lastSegment.equals(GoConstants.TEST_FILE_DIRECTORY)))
					{
						deleteFolder(file, true);
					}
				}
				return resource instanceof IContainer;
			}
		}, IResource.DEPTH_INFINITE, false);
		
		project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
	}

	private boolean deleteFolder(File f, boolean justContents) {
		if (!f.exists()) {
			return false;
		}
		if (f.isDirectory()) {
			String[] children = f.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteFolder(new File(f, children[i]), false);
				if (!success) {
					return false;
				}
			}
			if (!justContents) {
				f.delete();
			}
		} else {
			f.delete();
		}
		return true;
	}

}