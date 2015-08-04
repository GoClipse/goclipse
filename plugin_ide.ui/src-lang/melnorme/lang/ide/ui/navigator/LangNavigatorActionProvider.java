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


import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.actions.ActionContext;
import org.eclipse.ui.actions.ActionGroup;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonViewerWorkbenchSite;

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
	}
	
	protected abstract BuildTargetsActionGroup createBuildTargetsActionGroup(IViewPart viewPart);
	
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
	
	/* -----------------  ----------------- */
	
	public static class ViewPartActionGroup extends ActionGroup {
		
		protected final IViewPart viewPart;
		
		public ViewPartActionGroup(IViewPart viewPart) {
			this.viewPart = viewPart;
		}
		
		protected Shell getShell() {
			return viewPart.getSite().getShell();
		}
		
		protected Object getSelectionFirstElement() {
			ISelection selection = getContext().getSelection();
			if(selection instanceof IStructuredSelection) {
				IStructuredSelection structuredSelection = (IStructuredSelection) selection;
				return structuredSelection.getFirstElement();
			}
			return selection;
		}
		
	}
}