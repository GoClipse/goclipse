/*******************************************************************************
 * Copyright (c) 2010, 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.ui.properties;

import melnorme.lang.ide.ui.fields.ArgumentsGroupField;
import melnorme.lang.ide.ui.utils.UIOperationExceptionHandler;
import melnorme.util.swt.components.AbstractComponentExt;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.osgi.service.prefs.BackingStoreException;

import com.googlecode.goclipse.core.GoProjectPrefConstants;

public class GoProjectOptionsBlock extends AbstractComponentExt {
	
	protected IProject project;
	
	protected final ArgumentsGroupField buildExtraOptionsField = new ArgumentsGroupField(
		"Arguments for 'go build'");
	
	public GoProjectOptionsBlock() {
	}
	
	@Override
	public int getPreferredLayoutColumns() {
		return 1;
	}
	
	@Override
	protected void createContents(Composite topControl) {
		buildExtraOptionsField.createComponent(topControl,
			GridDataFactory.fillDefaults().grab(true, false).hint(200, SWT.DEFAULT).create());
	}
	
	@Override
	public void updateComponentFromInput() {
		if(project != null) {
			buildExtraOptionsField.setFieldValue(GoProjectPrefConstants.GO_BUILD_EXTRA_OPTIONS.get(project));
		}
	}
	
	// Note this can be called before the component is created
	public void initializeFrom(IProject project) {
		this.project = project;
		updateComponentFromInput();
	}
	
	public boolean performOk() {
		if(project == null) {
			return false;
		}
		try {
			GoProjectPrefConstants.GO_BUILD_EXTRA_OPTIONS.set(project, buildExtraOptionsField.getFieldValue());
		} catch (BackingStoreException e) {
			UIOperationExceptionHandler.handleError("Error saving preferences.", e);
		}
		return true;
	}
	
	public void restoreDefaults() {
		buildExtraOptionsField.setFieldValue(GoProjectPrefConstants.GO_BUILD_EXTRA_OPTIONS.getDefault());
	}
	
}