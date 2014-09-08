/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package MELNORME_ID.ide.ui.preferences;

import melnorme.lang.ide.ui.LangUIPlugin;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;


/**
 * The root preference page for DDT 
 */
public class LANGUAGE_RootPreferencePage extends PreferencePage implements IWorkbenchPreferencePage{
	
	public LANGUAGE_RootPreferencePage() {
		setPreferenceStore(LangUIPlugin.getPrefStore());
	}
	
	@Override
	public void init(IWorkbench workbench) {
		// Nothing to do
	}
	
	@Override
	protected Control createContents(Composite parent) {
		Composite content = new Composite(parent, SWT.NONE);
		return content;
	}
	
}