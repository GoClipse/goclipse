/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Anton Leherbauer (Wind River) -  [296800] UI build actions should not lock the workspace
 *     Broadcom Corporation - [335960]  Update BuildAction to use new Workspace Build Configurations API
 *     Andrey Loskutov <loskutov@gmx.de> - generified interface, bug 462760
 *******************************************************************************/
package melnorme.lang.ide.ui.utils;

import java.util.Collection;
import java.util.HashSet;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.BuildAction;
import org.eclipse.ui.ide.ResourceUtil;

import melnorme.utilbox.collections.Indexable;

// Copied from org.eclipse.ui.internal.ide.actions
/**
 * This class contains convenience methods used by the various build commands
 * to determine enablement.  These utilities cannot be factored into a common
 * class because some build actions are API and some are not.
 *
 * @since 3.1
 */
@SuppressWarnings("unused")
public class BuildUtilities {
	
//	/**
//	 * Extracts the selected projects from a selection.
//	 *
//	 * @param selection The selection to analyze
//	 * @return The selected projects
//	 */
//	public static IProject[] extractProjects(Object[] selection) {
//		HashSet projects = new HashSet();
//		for (int i = 0; i < selection.length; i++) {
//			Object element = selection[i];
//			IResource resource = ResourceUtil.getResource(element);
//			if (resource != null) {
//				projects.add(resource.getProject());
//			} else {
//				ResourceMapping mapping = ResourceUtil.getResourceMapping(element);
//				if (mapping != null) {
//					IProject[] theProjects = mapping.getProjects();
//					for (int j = 0; j < theProjects.length; j++) {
//						projects.add(theProjects[j]);
//					}
//				} else {
//					Object marker = ResourceUtil.getAdapter(element, IMarker.class, false);
//					if (marker instanceof IMarker) {
//						IProject project = ((IMarker) marker).getResource().getProject();
//						if (project != null) {
//							projects.add(project);
//						}
//					}
//				}
//			}
//		}
//		return (IProject[]) projects.toArray(new IProject[projects.size()]);
//	}
//
//	/**
//	 * Finds and returns the selected projects in the given window
//	 *
//	 * @param window The window to find the selection in
//	 * @return The selected projects, or an empty array if no selection could be found.
//	 */
//	public static IProject[] findSelectedProjects(IWorkbenchWindow window) {
//		if (window == null) {
//			return new IProject[0];
//		}
//
//		IWorkbenchPage activePage= window.getActivePage();
//		if (activePage == null) {
//			return new IProject[0];
//		}
//
//		IWorkbenchPart activePart= activePage.getActivePart();
//		if (activePart == null) {
//			return new IProject[0];
//		}
//
//		ISelection selection= window.getSelectionService().getSelection(activePart.getSite().getId());
//		IProject[] selected = null;
//		if (selection != null && !selection.isEmpty() && selection instanceof IStructuredSelection) {
//			selected = extractProjects(((IStructuredSelection) selection).toArray());
//		} else {
//			//see if we can extract a selected project from the active editor
//			if (activePart instanceof IEditorPart) {
//				IEditorPart editor= (IEditorPart)activePart;
//				IFile file = ResourceUtil.getFile(editor.getEditorInput());
//				if (file != null) {
//					selected = new IProject[] {file.getProject()};
//				}
//			}
//		}
//		if (selected == null) {
//			selected = new IProject[0];
//		}
//		return selected;
//	}
//
//	/**
//	 * Returns whether a build command with the given trigger should
//	 * be enabled for the given selection.
//	 * @param projects The projects to use to determine enablement
//	 * @param trigger The build trigger (<code>IncrementalProjectBuilder.*_BUILD</code> constants).
//	 * @return <code>true</code> if the action should be enabled, and
//	 * <code>false</code> otherwise.
//	 */
//	public static boolean isEnabled(IProject[] projects, int trigger) {
//		//incremental build is only enabled if all projects are not autobuilding
//		if (trigger == IncrementalProjectBuilder.INCREMENTAL_BUILD && ResourcesPlugin.getWorkspace().isAutoBuilding()) {
//			if (!matchingTrigger(projects, IncrementalProjectBuilder.AUTO_BUILD, false)) {
//				return false;
//			}
//		}
//		//finally we are building only if there is a builder that accepts the trigger
//		return matchingTrigger(projects, trigger, true);
//	}
//
//	/**
//	 * Returns whether one of the projects has a builder whose trigger setting
//	 * for the given trigger matches the given value.
//	 *
//	 * @param projects The projects to check
//	 * @param trigger The trigger to look for
//	 * @param value The trigger value to look for
//	 * @return <code>true</code> if one of the projects has a builder whose
//	 * trigger activation matches the provided value, and <code>false</code> otherwise.
//	 */
//	private static boolean matchingTrigger(IProject[] projects, int trigger, boolean value) {
//		for (int i = 0; i < projects.length; i++) {
//			if (!projects[i].isAccessible()) {
//				continue;
//			}
//			try {
//				IProjectDescription description = projects[i].getDescription();
//				ICommand[] buildSpec = description.getBuildSpec();
//				for (int j = 0; j < buildSpec.length; j++) {
//					if (buildSpec[j].isBuilding(trigger) == value) {
//						return true;
//					}
//				}
//			} catch (CoreException e) {
//				//ignore projects that are not available
//			}
//		}
//		return false;
//	}

	/**
	 * Causes all editors to save any modified resources in the provided collection
	 * of projects depending on the user's preference.
	 * @param projects The projects in which to save editors, or <code>null</code>
	 * to save editors in all projects.
	 */
	public static void saveEditors(Indexable<IProject> projects) {
		if (!BuildAction.isSaveAllSet()) {
			return;
		}
		IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
		for (int i = 0; i < windows.length; i++) {
			IWorkbenchPage[] pages = windows[i].getPages();
			for (int j = 0; j < pages.length; j++) {
				IWorkbenchPage page = pages[j];
				if (projects == null) {
					page.saveAllEditors(false);
				} else {
					IEditorPart[] editors = page.getDirtyEditors();
					for (int k = 0; k < editors.length; k++) {
						IEditorPart editor = editors[k];
						IFile inputFile = ResourceUtil.getFile(editor.getEditorInput());
						if (inputFile != null) {
							if (projects.contains(inputFile.getProject())) {
								page.saveEditor(editor, false);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Doesn't need to be instantiated
	 */
	private BuildUtilities() {
	}
}