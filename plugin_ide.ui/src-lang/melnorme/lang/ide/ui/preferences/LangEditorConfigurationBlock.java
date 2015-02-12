/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation (JDT)
 *     DLTK team ? - DLTK modifications 
 *     Bruno Medeiros - Lang modifications
 *******************************************************************************/
package melnorme.lang.ide.ui.preferences;

import melnorme.lang.ide.ui.preferences.EditorAppearanceColorsComponent.EditorColorItem;
import melnorme.lang.ide.ui.preferences.common.AbstractComponentsPrefPage;
import melnorme.lang.ide.ui.preferences.common.AbstractPreferencesBlock;

import org.eclipse.swt.widgets.Composite;

//originally from DLTK version 5.0.0
public class LangEditorConfigurationBlock extends AbstractPreferencesBlock {
	
	public LangEditorConfigurationBlock(AbstractComponentsPrefPage mainPreferencePage) {
		super(mainPreferencePage);
	}
	
	@Override
	public int getPreferredLayoutColumns() {
		return 1;
	}
	
	@Override
	protected void createContents(Composite topControl) {
		createAppearanceGroup(topControl);
	}
	
	protected void createAppearanceGroup(Composite parent) {
		createEditorAppearanceColorsComponent(parent);
	}
	
	protected void createEditorAppearanceColorsComponent(Composite parent) {
		EditorColorItem[] editorColorItems = createEditorAppearanceColorEntries();
		if(editorColorItems.length == 0) {
			return;
		}
		EditorAppearanceColorsComponent appearanceItemsCp = new EditorAppearanceColorsComponent(editorColorItems);
		appearanceItemsCp.createComponentInlined(parent);
		addPrefComponent(appearanceItemsCp);
	}
	
	protected EditorColorItem[] createEditorAppearanceColorEntries() {
		return new EditorColorItem[] { };
	}
	
}