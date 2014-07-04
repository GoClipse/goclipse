/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.ui.properties;

import melnorme.lang.ide.ui.dialogs.AbstractProjectPropertyPage;
import melnorme.util.swt.SWTFactoryUtil;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class GoProjectBuildOptionsPage extends AbstractProjectPropertyPage {
	
	public GoProjectBuildOptionsPage() {
	}
	
	@Override
	protected Control createContents(Composite parent, IProject project) {
		noDefaultAndApplyButton();
		
		return SWTFactoryUtil.createLabel(parent, SWT.LEFT, "No options at the moment", null);
	}
	
	@Override
	protected void performDefaults() {
	}
	
	@Override
	public boolean performOk() {
		return true;
	}
	
}