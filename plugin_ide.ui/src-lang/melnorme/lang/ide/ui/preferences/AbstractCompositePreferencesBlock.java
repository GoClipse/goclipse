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


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import melnorme.lang.ide.core.utils.prefs.DerivedValuePreference;
import melnorme.lang.ide.ui.preferences.common.PreferencesPageContext;
import melnorme.util.swt.components.CompositeWidget;
import melnorme.util.swt.components.FieldWidget;

public abstract class AbstractCompositePreferencesBlock extends CompositeWidget {
	
	protected final PreferencesPageContext prefContext;
	
	public AbstractCompositePreferencesBlock(PreferencesPageContext prefContext) {
		super(false);
		this.prefContext = assertNotNull(prefContext);
	}
	
	protected void bindFieldToDerivedPreference(FieldWidget<String> field, DerivedValuePreference<?> pref) {
		prefContext.bindToValidatedPreference(field, pref);
	}
	
}