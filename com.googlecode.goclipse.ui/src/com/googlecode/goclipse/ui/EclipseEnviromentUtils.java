package com.googlecode.goclipse.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

// BM: The way this code is used is horrible, it should be removed
public class EclipseEnviromentUtils {
	/**
	 * Determines the current project of the active context; if possible. else
	 * return null.
	 * 
	 * @return IProject | null
	 */
	@Deprecated
	public static IProject getCurrentProject() {
		IProject project = null;

		try {
			IWorkbench iWorkbench 			  = PlatformUI.getWorkbench();
			IWorkbenchWindow iWorkbenchWindow = iWorkbench.getActiveWorkbenchWindow();
			if (iWorkbenchWindow != null) {
				IWorkbenchPage iWorkbenchPage 	  = iWorkbenchWindow.getActivePage();
				ISelection iSelection 			  = iWorkbenchPage.getSelection();
				iSelection 						  = iWorkbenchPage.getSelection();
				
				if(iSelection instanceof TextSelection){
					IEditorInput editorInput = iWorkbenchWindow.getActivePage().getActiveEditor().getEditorInput();
					if(editorInput instanceof IFileEditorInput){
						return ((IFileEditorInput)editorInput).getFile().getProject();
					}
				}
				
				project = extractSelection(iSelection).getProject();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (project == null) {
			IWorkspace root = ResourcesPlugin.getWorkspace();
			IProject[] projects = root.getRoot().getProjects();

			for (int i = 0; i < projects.length - 1; i++) {
				for (int j = i + 1; j < projects.length; j++) {
					if (projects[i].getName().compareToIgnoreCase(
							projects[j].getName()) > 0) { // ascending
						// sort
						IProject temp = projects[i];
						projects[i] = projects[j]; // swapping
						projects[j] = temp;

					}
				}
			}

			if (projects.length > 0) {
				return projects[0];
			}
		}
		
		return project;
	}
	
	/**
	 * Returns the selected resource
	 * 
	 * @param sel
	 * @return
	 */
	public static IResource extractSelection(ISelection sel) {
		if (!(sel instanceof IStructuredSelection))
			return null;
		IStructuredSelection ss = (IStructuredSelection) sel;
		Object element = ss.getFirstElement();
		if (element instanceof IResource)
			return (IResource) element;
		if (!(element instanceof IAdaptable))
			return null;
		IAdaptable adaptable = (IAdaptable) element;
		Object adapter = adaptable.getAdapter(IResource.class);
		return (IResource) adapter;
	}

	/**
	 * Get the shell of the Active Workbench Window
	 * 
	 * @return
	 */
	public static Shell getActiveShell() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow wWindow = workbench.getActiveWorkbenchWindow();
		return wWindow.getShell();
	}

}