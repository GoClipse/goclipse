/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.ui;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.osgi.framework.BundleContext;

import melnorme.lang.ide.core.LangCore_Actual;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.utils.WorkbenchUtils;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.utilbox.core.CommonException;

public class GoUIPlugin extends LangUIPlugin {
	
	@Override
	protected GoOperationsConsoleUIHandler createOperationsConsoleListener() {
		return new GoOperationsConsoleUIHandler();
	}
	
	@Override
	protected void doInitializeAfterLoad(IOperationMonitor om) throws CommonException {
		checkPrefPageIdIsValid(LangCore_Actual.TOOLS_PREF_PAGE_ID);
	}
	
	protected void checkPrefPageIdIsValid(String prefId) {
		Shell shell = WorkbenchUtils.getActiveWorkbenchShell();
		PreferenceDialog prefDialog = PreferencesUtil.createPreferenceDialogOn(shell, prefId, null, null);
		assertNotNull(prefDialog); // Don't create, just eagerly check that it exits, that the ID is correct
		ISelection selection = prefDialog.getTreeViewer().getSelection();
		if(selection instanceof IStructuredSelection) {
			IStructuredSelection ss = (IStructuredSelection) selection;
			if(ss.getFirstElement() instanceof IPreferenceNode) {
				IPreferenceNode prefNode = (IPreferenceNode) ss.getFirstElement();
				if(prefNode.getId().equals(prefId)) {
					return; // Id exists
				}
			}
		}
		assertFail();
	}
	
	@Override
	protected void doCustomStop(BundleContext context) {
	}
	
}