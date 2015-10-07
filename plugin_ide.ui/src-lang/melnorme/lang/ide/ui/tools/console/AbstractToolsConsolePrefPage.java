/*******************************************************************************
 * Copyright (c) 2014, 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.tools.console;

import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.util.swt.SWTFactoryUtil;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public abstract class AbstractToolsConsolePrefPage extends FieldEditorPreferencePage 
		implements ToolsConsolePrefs, IWorkbenchPreferencePage {
	
	public AbstractToolsConsolePrefPage() {
		super(GRID);
		setPreferenceStore(LangUIPlugin.getPrefStore());
	}
	
	@Override
	public void init(IWorkbench workbench) {
	}
	
	@Override
	protected void createFieldEditors() {
		Composite parent = getFieldEditorParent();
		
		addField(new BooleanFieldEditor(ACTIVATE_ON_ERROR_MESSAGES.key, 
			"Activate console on error messages", parent));
		
		SWTFactoryUtil.createLabel(parent, SWT.LEFT, "Console color settings:", 
			GridDataFactory.fillDefaults().span(2, 1).create());
		
		addField(new ColorFieldEditor(INFO_COLOR.getActiveKey(),
			"Information text color:", parent));
		addField(new ColorFieldEditor(STDOUT_COLOR.getActiveKey(),
			"Program standard output text color:", parent));
		addField(new ColorFieldEditor(STDERR_COLOR.getActiveKey(),
			"Program error output text color:", parent));
		addField(new ColorFieldEditor(BACKGROUND_COLOR.getActiveKey(),
			"Console background color:", parent));
	}
	
}