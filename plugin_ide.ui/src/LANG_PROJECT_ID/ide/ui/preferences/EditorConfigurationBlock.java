/*******************************************************************************
 * Copyright (c) 2007, 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package LANG_PROJECT_ID.ide.ui.preferences;

import melnorme.lang.ide.ui.preferences.LangEditorConfigurationBlock;
import melnorme.lang.ide.ui.preferences.common.AbstractComponentsPrefPage;

import org.eclipse.swt.widgets.Composite;

public class EditorConfigurationBlock extends LangEditorConfigurationBlock {
	
	public EditorConfigurationBlock(AbstractComponentsPrefPage mainPreferencePage) {
		super(mainPreferencePage);
	}
	
	@Override
	protected void createAppearanceGroup(Composite parent) {
		//super.createAppearanceGroup(parent);
		// return, don't create appearance group
	}
	
}