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


import melnorme.lang.ide.ui.preferences.common.AbstractPreferencesBlock;
import melnorme.lang.ide.ui.preferences.common.PreferencesPageContext;
import melnorme.lang.ide.ui.preferences.common.IPreferencesWidget;
import melnorme.util.swt.components.IDisableableComponent;

public abstract class AbstractPreferencesBlockExt extends AbstractPreferencesBlock 
	implements IPreferencesWidget, IDisableableComponent {
	
	public AbstractPreferencesBlockExt(PreferencesPageContext prefContext) {
		super(prefContext);
	}
	
	@Override
	public void _verifyContract() {
		// Verify the contract of IDisableableComponent
		IDisableableComponent.super._verifyContract();
	}
	
}