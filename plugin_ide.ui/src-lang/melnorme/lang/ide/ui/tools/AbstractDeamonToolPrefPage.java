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

import melnorme.lang.ide.core.operations.ToolchainPreferences;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.LangUIPlugin_Actual;
import melnorme.lang.ide.ui.preferences.common.AbstractComponentsPrefPage;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.components.fields.ButtonTextField;
import melnorme.util.swt.components.fields.CheckBoxField;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;

public abstract class AbstractDeamonToolPrefPage extends AbstractComponentsPrefPage {
	
	public AbstractDeamonToolPrefPage() {
		// Note: we must use the Core preference store, as that's the scope where the preferences are stored.
		super(LangUIPlugin.getCorePrefStore());
	}
	
	@Override
	protected String getHelpId() {
		return null;
	}
	
	/* -----------------  ----------------- */
	
	protected Group toolGroup;
	protected ButtonTextField daemonPathEditor;
	
	@Override
	protected Control createContents(Composite parent) {
		Composite block = SWTFactoryUtil.createComposite(parent);
		doCreateContents(block);
		return block;
	}
	
	protected void doCreateContents(Composite block) {
		block.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).create());
		
		toolGroup = createOptionsSection(block, 
			getDaemonToolName(), 
			GridDataFactory.fillDefaults().grab(true, false).minSize(300, SWT.DEFAULT).create(), 
			3);
		
		addBooleanComponent(ToolchainPreferences.AUTO_START_DAEMON, toolGroup, new CheckBoxField(
			"Start " + getDaemonToolName() + " server automatically"));
		
		addBooleanComponent(ToolchainPreferences.DAEMON_CONSOLE_ENABLE, toolGroup, new CheckBoxField(
			"Enable " + getDaemonToolName() + " log console (requires restart)"));
		
		daemonPathEditor = createDaemonPathFieldEditor(toolGroup);
	}
	
	protected ButtonTextField createDaemonPathFieldEditor(Group group) {
		return createFileComponent(group, getDaemonToolName() + " path:", 
			ToolchainPreferences.DAEMON_PATH, true);
	}
	
	protected String getDaemonToolName() {
		return LangUIPlugin_Actual.DAEMON_TOOL_Name;
	}
	
}