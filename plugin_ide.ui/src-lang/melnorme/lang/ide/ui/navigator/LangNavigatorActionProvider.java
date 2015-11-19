/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.navigator;


import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.actions.ActionContext;
import org.eclipse.ui.actions.ActionGroup;
import org.eclipse.ui.actions.BuildAction;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonMenuConstants;
import org.eclipse.ui.navigator.ICommonViewerWorkbenchSite;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.project_model.view.DependenciesContainer;
import melnorme.lang.ide.ui.LangImages;
import melnorme.lang.ide.ui.actions.RunUIOperationAction;
import melnorme.lang.ide.ui.utils.operations.AbstractUIOperation;
import melnorme.utilbox.collections.ArrayList2;

public abstract class LangNavigatorActionProvider extends CommonActionProvider {
	
	protected final ArrayList2<ActionGroup> actionGroups = new ArrayList2<>();
	
	public LangNavigatorActionProvider() {
		super();
	}
	
	@Override
	public void init(ICommonActionExtensionSite site) {
		if (site.getViewSite() instanceof ICommonViewerWorkbenchSite) {
			ICommonViewerWorkbenchSite workbenchSite = (ICommonViewerWorkbenchSite) site.getViewSite();
			if (workbenchSite.getPart() instanceof IViewPart) {
				IViewPart viewPart= (IViewPart) workbenchSite.getPart();
				
				initActionGroups(viewPart);
			}
		}
	}
	
	protected void initActionGroups(IViewPart viewPart) {
		actionGroups.add(createBuildTargetsActionGroup(viewPart));
		actionGroups.add(createProjectBuildActionGroup(viewPart));
	}
	
	@Override
	public void setContext(ActionContext context) {
		for(ActionGroup actionGroup : actionGroups) {
			actionGroup.setContext(context);
		}
	}
	
	@Override
	public void fillActionBars(IActionBars actionBars) {
		for(ActionGroup actionGroup : actionGroups) {
			actionGroup.fillActionBars(actionBars);
		}
	}
	
	@Override
	public void fillContextMenu(IMenuManager menu) {
		for(ActionGroup actionGroup : actionGroups) {
			actionGroup.fillContextMenu(menu);
		}
	}
	
	protected abstract BuildTargetsActionGroup createBuildTargetsActionGroup(IViewPart viewPart);
	
	protected BuildOperationsActionGroup createProjectBuildActionGroup(IViewPart viewPart) {
		return new BuildOperationsActionGroup(viewPart);
	}
	
	/* -----------------  ----------------- */
	
	public static class ViewPartActionGroup extends ActionGroup {
		
		protected final IViewPart viewPart;
		
		public ViewPartActionGroup(IViewPart viewPart) {
			this.viewPart = viewPart;
		}
		
		protected Shell getShell() {
			return viewPart.getSite().getShell();
		}
		
		protected IStructuredSelection getStructuredSelection() {
			ISelection selection = getContext().getSelection();
			if(selection instanceof IStructuredSelection) {
				return (IStructuredSelection) selection;
			}
			return null;
		}
		
		protected Object getSelectionFirstElement() {
			IStructuredSelection ssel = getStructuredSelection();
			if(ssel != null) {
				return ssel.getFirstElement();
			}
			return getContext().getSelection();
		}
		
		public IProject getBundleProjectFromSelection() {
			Object selElement = getSelectionFirstElement();
			if(selElement instanceof IProject) {
				return (IProject) selElement;
			}
			if(selElement instanceof DependenciesContainer) {
				DependenciesContainer dependenciesContainer = (DependenciesContainer) selElement;
				return dependenciesContainer.getParent();
			}
			if(selElement instanceof IFile) {
				IFile file = (IFile) selElement;
				if(LangCore.getBundleModelManager().isBundleManifestFile(file)) {
					return file.getProject();
				}
			}
			return null;
		}
		
	}
	
	public static class BuildOperationsActionGroup extends ViewPartActionGroup {
		
		protected final BuildAction buildAction;
		
		public BuildOperationsActionGroup(IViewPart viewPart) {
			super(viewPart);
			
			buildAction = new BuildAction(() -> viewPart.getSite().getShell(), 
				IncrementalProjectBuilder.INCREMENTAL_BUILD);
			buildAction.setActionDefinitionId(IWorkbenchCommandConstants.PROJECT_BUILD_PROJECT);
		}
		
		@Override
		public void fillContextMenu(IMenuManager menu) {
			IProject project = getBundleProjectFromSelection();
			if(project == null)
				return;
			
			if(ResourcesPlugin.getWorkspace().isAutoBuilding()) {
				// This action is not otherwise shown if project is auto-building, so add it.
				
				IStructuredSelection structuredSel = getStructuredSelection();
				if(structuredSel != null) {
					buildAction.selectionChanged(structuredSel);
				}
				menu.appendToGroup(ICommonMenuConstants.GROUP_BUILD, buildAction);
			}
		}
		
	}
	
	public static abstract class BundleOperationsActionGroup extends ViewPartActionGroup {
		
		public BundleOperationsActionGroup(IViewPart viewPart) {
			super(viewPart);
		}
		
		@Override
		public void fillContextMenu(IMenuManager menu) {
			IProject project = getBundleProjectFromSelection();
			if(project == null)
				return;
			
			MenuManager bundleOpsMenu = new MenuManager(getMenuName(), LangImages.NAV_Library, "bundleMenu");
			
			initActions(bundleOpsMenu, project);
			
			menu.prependToGroup(ICommonMenuConstants.GROUP_BUILD, bundleOpsMenu);
			
		}
		
		protected abstract String getMenuName();
		
		protected abstract void initActions(MenuManager bundleOpsMenu, IProject project);
		
		protected void addRunOperationAction(MenuManager bundleOpsMenu, AbstractUIOperation operation) {
			bundleOpsMenu.add(new RunUIOperationAction(operation));
		}
		
	}
	
}