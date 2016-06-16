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
package com.googlecode.goclipse.ui.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.swt.widgets.Composite;

import melnorme.lang.ide.ui.tools.console.AbstractToolsConsolePrefPage;

public class GoOperationsConsolePrefPage extends AbstractToolsConsolePrefPage {
	
	@Override
	protected void create_ActivateOnErrorMessagesField(Composite parent) {
		addField(new BooleanFieldEditor(ACTIVATE_ON_ERROR_MESSAGES.key, 
		"Activate console on build failures.", parent));
	}
	
}