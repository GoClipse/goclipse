/*******************************************************************************
 * Copyright (c) 2000, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Bruno Medeiros - LangIDE adaptation.
 *******************************************************************************/
package melnorme.lang.ide.ui.tools.console;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.TextConsole;
import org.eclipse.ui.console.TextConsolePage;
import org.eclipse.ui.console.TextConsoleViewer;

/**
 * A page for an OperationsConsolePage. Originally based on {@link IOConsole}
 * Console is read only.
 */
public class ToolsConsolePage extends TextConsolePage {
	
	protected ScrollLockAction fScrollLockAction;
	
	public ToolsConsolePage(TextConsole console, IConsoleView view) {
		super(console, view);
		
		setReadOnly();
	}
	
	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		getViewer().setReadOnly();
	}
	
	@Override
	protected TextConsoleViewer createViewer(Composite parent) {
		return new IOConsoleViewer(parent, (TextConsole)getConsole());
	}
	
	@Override
	public IOConsoleViewer getViewer() {
		return (IOConsoleViewer) super.getViewer();
	}
	
	public void setAutoScroll(boolean scroll) {
		IOConsoleViewer viewer = getViewer();
		if (viewer != null) {
			viewer.setAutoScroll(scroll);
			fScrollLockAction.setChecked(!scroll);
		}
	}
	
	/**
	 * Informs the viewer that it's text widget should not be editable.
	 */
	 public void setReadOnly() {
		IOConsoleViewer viewer = getViewer();
		if (viewer != null) {
			viewer.setReadOnly();
		}
	 }
	 
	 @Override
	 protected void createActions() {
		 super.createActions();
		 fScrollLockAction = new ScrollLockAction(getConsoleView());
		 setAutoScroll(!fScrollLockAction.isChecked());
	 }
	 
	 @Override
	 protected void contextMenuAboutToShow(IMenuManager menuManager) {
		 super.contextMenuAboutToShow(menuManager);
		 menuManager.add(fScrollLockAction);
	 }
	 
	 @Override
	 protected void configureToolBar(IToolBarManager mgr) {
		 super.configureToolBar(mgr);
		 mgr.appendToGroup(IConsoleConstants.OUTPUT_GROUP, fScrollLockAction);
	 }
	 
	 @Override
	 public void dispose() {
		 if (fScrollLockAction != null) {
			 fScrollLockAction.dispose();
			 fScrollLockAction = null;
		 }
		 super.dispose();
	 }
	 
}