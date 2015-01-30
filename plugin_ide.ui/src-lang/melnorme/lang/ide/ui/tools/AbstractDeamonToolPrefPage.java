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
import melnorme.lang.ide.ui.preferences.FieldEditorPreferencePageExt;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;

public abstract class AbstractDeamonToolPrefPage extends FieldEditorPreferencePageExt {
	
	protected Group toolGroup;
	
	public AbstractDeamonToolPrefPage() {
		super(GRID);
		
		// Note: we must use the Core preference store, as that's the scope where the preferences are stored.
		setPreferenceStore(LangUIPlugin.getCorePrefStore());
	}
	
	@Override
	protected void createFieldEditors() {
		toolGroup = createPreferenceGroup(getDaemonToolName());
		
		addField(new BooleanFieldEditor(
			DaemonEnginePreferences.AUTO_START_SERVER.key,
			"Start " + getDaemonToolName() + " server automatically", toolGroup));
		
		addField(new BooleanFieldEditor(
			DaemonEnginePreferences.DAEMON_CONSOLE_ENABLE.key,
			"Enable " + getDaemonToolName() + " log console (requires restart)", toolGroup));
		
		createDaemonPathFieldEditor(toolGroup);
		
		GridLayoutFactory.fillDefaults().spacing(6, 2).margins(6, 4).applyTo(toolGroup);
	}
	
	protected void createDaemonPathFieldEditor(Group group) {
		addField(new FileFieldEditor(
			DaemonEnginePreferences.DAEMON_PATH.key, getDaemonToolName() + " path:", group));
	}
	
	@Override
	protected void adjustGridLayoutForFieldsNumberofColumns(int numColumns) {
		((GridLayout) toolGroup.getLayout()).numColumns = numColumns;
	}
	
	protected abstract String getDaemonToolName();
	
}