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
import melnorme.lang.ide.ui.preferences.common.AbstractComponentsPrefPage;
import melnorme.util.swt.SWTFactoryUtil;
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
	
	@Override
	protected Control createContents(Composite parent) {
		Composite block = SWTFactoryUtil.createComposite(parent);
		doCreateContents(block);
		return block;
	}
	
	protected void doCreateContents(Composite block) {
		block.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).create());
		
		toolGroup = SWTFactoryUtil.createGroup(block,
			getDaemonToolName(),
			GridDataFactory.fillDefaults().grab(true, false).minSize(300, SWT.DEFAULT).create());
		
		toolGroup.setLayout(GridLayoutFactory.fillDefaults().numColumns(3).spacing(6, 2).margins(6, 4).create());
		
		addBooleanComponent(DaemonEnginePreferences.AUTO_START_SERVER.key, toolGroup, new CheckBoxField(
			"Start " + getDaemonToolName() + " server automatically"));
		
		addBooleanComponent(DaemonEnginePreferences.DAEMON_CONSOLE_ENABLE.key, toolGroup, new CheckBoxField(
			"Enable " + getDaemonToolName() + " log console (requires restart)"));
		
		createDaemonPathFieldEditor(toolGroup);
	}
	
	protected void createDaemonPathFieldEditor(Group group) {
		createFileComponent(group, getDaemonToolName() + " path:", 
			DaemonEnginePreferences.DAEMON_PATH.key, true);
	}
	
	protected abstract String getDaemonToolName();
	
}