/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.preferences;


import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import melnorme.lang.ide.ui.preferences.common.AbstractPreferencesEditorsPrefPage;

public abstract class AbstractPreferencesBlockPrefPage extends AbstractPreferencesEditorsPrefPage {
	
	protected final ValidatedConfigBlock preferencesBlock;
	
	public AbstractPreferencesBlockPrefPage() {
		super();
		
		this.preferencesBlock = init_createPreferencesBlock();
		addValidationStatusField(preferencesBlock.validation);
	}
	
	protected abstract ValidatedConfigBlock init_createPreferencesBlock();
	
	@Override
	protected Control createContents(Composite parent) {
		return preferencesBlock.createComponent(parent);
	}
	
}