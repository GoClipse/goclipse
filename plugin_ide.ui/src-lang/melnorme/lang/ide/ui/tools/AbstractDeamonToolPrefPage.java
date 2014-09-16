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
package melnorme.lang.ide.ui.tools;

import melnorme.lang.ide.core.operations.DaemonEnginePreferences;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.util.swt.SWTFactoryUtil;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Group;

public abstract class AbstractDeamonToolPrefPage extends FieldEditorPreferencePage {
	
	public AbstractDeamonToolPrefPage() {
		super(GRID);
		
		// Note: we must use the Core preference store, as that's the scope where the preferences are stored.
		setPreferenceStore(LangUIPlugin.getCorePrefStore());
	}
	
	@Override
	protected void createFieldEditors() {
		Group group = SWTFactoryUtil.createGroup(getFieldEditorParent(),
			getDaemonToolName(),
			GridDataFactory.fillDefaults().grab(true, false).minSize(300, SWT.DEFAULT).create());
		
		addField(new BooleanFieldEditor(
			DaemonEnginePreferences.AUTO_START_SERVER.key,
			"Start " + getDaemonToolName() + " server automatically", group));
		
		addField(new FileFieldEditor(
			DaemonEnginePreferences.DAEMON_PATH.key, getDaemonToolName() + " path:", group));
		
		addField(new BooleanFieldEditor(
			DaemonEnginePreferences.DAEMON_CONSOLE_ENABLE.key,
			"Enable " + getDaemonToolName() + " log console (requires restart)", group));
		
		GridLayoutFactory.fillDefaults().margins(6, 4).numColumns(3).applyTo(group);
	}
	
	protected abstract String getDaemonToolName();
	
}