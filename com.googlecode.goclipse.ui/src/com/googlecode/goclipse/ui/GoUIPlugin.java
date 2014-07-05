package com.googlecode.goclipse.ui;

import java.lang.reflect.InvocationTargetException;

import melnorme.lang.ide.ui.LangUIPlugin;

import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;
import org.osgi.framework.BundleContext;

import com.googlecode.goclipse.builder.GoToolManager;

public class GoUIPlugin extends LangUIPlugin {
	
	public static final String PLUGIN_ID = "com.googlecode.goclipse.ui";
	
	private GoBuilderConsoleListener toolchainProcessListener;
	
	@Override
	protected void doCustomStart_finalStage() {
		toolchainProcessListener = new GoBuilderConsoleListener();
		GoToolManager.getDefault().addBuildProcessListener(toolchainProcessListener);
		
		super.doCustomStart_finalStage();
	}
	
	@Override
	protected void doCustomStop(BundleContext context) {
		GoToolManager.getDefault().removeBuildProcessListener(toolchainProcessListener);
	}
	
	
	@SuppressWarnings("restriction")
	public static void fireProjectChange(final IProject project) {
		IRunnableWithProgress runnable = new IRunnableWithProgress(){

			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				fireProjectChange(project,monitor);
			}
			
		};
		
		IProgressService service = PlatformUI.getWorkbench().getProgressService();
		try {
			service.run(false, false, runnable);
		} catch (Exception e) {
		}
	}
	
	@SuppressWarnings("restriction")
	public static void fireProjectChange(IProject project,IProgressMonitor monitor) {
		if(project.getWorkspace() instanceof Workspace){
			Workspace workspace = (Workspace) project.getWorkspace();
			try {
				workspace.getRefreshManager().refresh(project);
				workspace.broadcastBuildEvent(project, IResourceChangeEvent.POST_CHANGE, IncrementalProjectBuilder.FULL_BUILD);
			} catch (Exception e) {
				getInstance().log(e);
			}
		}
	}

	public static void fireProjectChange(final IProject project, final IRunnableWithProgress runnable) {
		IRunnableWithProgress runner = new IRunnableWithProgress(){

			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				runnable.run(monitor);
				fireProjectChange(project, monitor);
			}
			
		};
		IProgressService service = PlatformUI.getWorkbench().getProgressService();
		try {
			service.run(false, false, runner);
		} catch (Exception e) {
		}
	}
	

	
	
	
}