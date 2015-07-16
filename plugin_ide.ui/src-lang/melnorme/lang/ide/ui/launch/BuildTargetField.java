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
package melnorme.lang.ide.ui.launch;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.widgets.Composite;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.lang.ide.core.project_model.ProjectBuildInfo;
import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.components.fields.ComboFieldComponent;
import melnorme.utilbox.misc.ArrayUtil;

/**
 * Field for a program path relative to Eclipse project.
 */
public class BuildTargetField extends ComboFieldComponent {
	
	public BuildTargetField() {
		super();
	}
	
	@Override
	protected Composite doCreateTopLevelControl(Composite parent) {
		return SWTFactoryUtil.createGroup(parent, LangUIMessages.BuildTargetField_title);
	}
	
	@Override
	protected GridLayoutFactory createTopLevelLayout() {
		return GridLayoutFactory.swtDefaults().numColumns(getPreferredLayoutColumns());
	}
	
	@Override
	protected void createContents(Composite topControl) {
		super.createContents(topControl);
		
		combo.setLayoutData(gdFillDefaults().grab(false, false).hint(200, 1).create());
	}
	
	/* ----------------- binding ----------------- */
	
	public void handleProjectChanged(IProject project) {
		if(project == null) {
			return;
		}
		ProjectBuildInfo buildInfo = LangCore.getBuildManager().getBuildInfo(project);
		if(buildInfo == null) {
			return;
		}
		
		String[] comboItems = ArrayUtil.map(buildInfo.getBuildTargets(), 
			(BuildTarget buildTarget) -> buildTarget.getTargetName(), String.class);
		
		setComboItems(comboItems);
	}
	
}