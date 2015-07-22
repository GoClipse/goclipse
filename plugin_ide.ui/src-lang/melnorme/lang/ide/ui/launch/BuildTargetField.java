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

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.widgets.Composite;

import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.components.fields.ComboOptionsField;

/**
 * Field for a program path relative to Eclipse project.
 */
public class BuildTargetField extends ComboOptionsField {
	
	public BuildTargetField() {
		super(LangUIMessages.BuildTargetField_title);
	}
	
	@Override
	protected Composite doCreateTopLevelControl(Composite parent) {
		return SWTFactoryUtil.createGroup(parent, getLabelText());
	}
	
	@Override
	protected GridLayoutFactory createTopLevelLayout() {
		return GridLayoutFactory.swtDefaults().numColumns(getPreferredLayoutColumns());
	}
	
	@Override
	public int getPreferredLayoutColumns() {
		return 1;
	}
	
	@Override
	protected void createContents_layout() {
		combo.setLayoutData(gdFillDefaults().grab(false, false).hint(200, 1).create());
	}
	
	@Override
	protected void createContents_Label(Composite parent) {
		// Do not create
	}
	
}