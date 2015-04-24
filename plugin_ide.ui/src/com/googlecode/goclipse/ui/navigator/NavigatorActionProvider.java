/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.ui.navigator;

import melnorme.lang.ide.ui.utils.UIOperationExceptionHandler;

import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionConstants;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonMenuConstants;
import org.eclipse.ui.navigator.ICommonViewerSite;
import org.eclipse.ui.navigator.ICommonViewerWorkbenchSite;

public class NavigatorActionProvider extends CommonActionProvider {
	
	protected NavigatorOpenAction navigatorOpenAction;
	
	public NavigatorActionProvider() {
	}
	
	@Override
	public void init(ICommonActionExtensionSite aSite) {
		super.init(aSite);
		
		ICommonViewerSite viewSite = aSite.getViewSite();
		if(viewSite instanceof ICommonViewerWorkbenchSite) {
			ICommonViewerWorkbenchSite cvws = (ICommonViewerWorkbenchSite) viewSite;
			navigatorOpenAction = new NavigatorOpenAction(cvws.getPage(), cvws.getSelectionProvider());
		}
	}
	
	@Override
	public void fillActionBars(IActionBars actionBars) {
		super.fillActionBars(actionBars);
		
		if(navigatorOpenAction.isEnabled()) {
			actionBars.setGlobalActionHandler(ICommonActionConstants.OPEN, navigatorOpenAction);
		}
	}
	
	@Override
	public void fillContextMenu(IMenuManager menu) {
		if(navigatorOpenAction.isEnabled()) {
			menu.appendToGroup(ICommonMenuConstants.GROUP_OPEN, navigatorOpenAction);
		}
	}
	
	public static class NavigatorOpenAction extends Action {
		
		protected final IWorkbenchPage page;
		protected final ISelectionProvider selectionProvider;
		protected IFileStore fileStore;
		
		public NavigatorOpenAction(IWorkbenchPage page, ISelectionProvider selectionProvider) {
			this.page = page;
			this.selectionProvider = selectionProvider;
			setText("Open");
		}
		
		@Override
		public boolean isEnabled() {
			ISelection selection = selectionProvider.getSelection();
			if(selection.isEmpty() || !(selection instanceof StructuredSelection)) {
				return false;
			}
			StructuredSelection ss = (StructuredSelection) selection;
			if(ss.size() == 1 && ss.getFirstElement() instanceof IFileStore) {
				IFileStore fileStore = (IFileStore) ss.getFirstElement();
				if(!fileStore.fetchInfo().isDirectory()) {
					this.fileStore = fileStore;
					return true;
				}
				return false;
			}
			return false;
		}
		
		@Override
		public void run() {
			if(isEnabled()) {
				try {
					IDE.openEditorOnFileStore(page, fileStore);
				} catch (PartInitException exception) {
					UIOperationExceptionHandler.handleError("Error Opening File", exception);
				}
			}
		}
		
	}
	
}