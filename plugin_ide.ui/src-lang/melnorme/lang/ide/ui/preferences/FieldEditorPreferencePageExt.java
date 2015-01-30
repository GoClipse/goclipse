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
package melnorme.lang.ide.ui.preferences;


import melnorme.util.swt.SWTFactoryUtil;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

public abstract class FieldEditorPreferencePageExt extends FieldEditorPreferencePage {
	
	public FieldEditorPreferencePageExt(int style) {
		super(style);
	}
	
	public FieldEditorPreferencePageExt(String title, int style) {
		super(title, style);
	}
	
	public FieldEditorPreferencePageExt(String title, ImageDescriptor image, int style) {
		super(title, image, style);
	}
	
	protected Group createPreferenceGroup(String groupName) {
		return SWTFactoryUtil.createGroup(getFieldEditorParent(), groupName,
			GridDataFactory.fillDefaults().grab(true, false).minSize(300, SWT.DEFAULT).create());
	}
	
	protected GridDataFactory fillGridDataFactory(int numColumns) {
		return GridDataFactory.fillDefaults().grab(true, false).span(numColumns, 1);
	}
	
	@Override
	protected void adjustGridLayout() {
		super.adjustGridLayout();
		
		int numColumns = ((GridLayout) getFieldEditorParent().getLayout()).numColumns;
		// Fix parent layout, the number of columns there is irrelevant, because we are using groups.
		((GridLayout) getFieldEditorParent().getLayout()).numColumns = 1;
		// Instead, adjudt the numColumns of the group layout:
		adjustGridLayoutForFieldsNumberofColumns(numColumns);
	}
	
	protected abstract void adjustGridLayoutForFieldsNumberofColumns(int numColumns);
	
	protected void applyDefaultGridLayout(Composite composite, int numColumns) {
		GridLayoutFactory.fillDefaults().spacing(6, 4).margins(6, 3).numColumns(numColumns).applyTo(composite);
	}
	
}