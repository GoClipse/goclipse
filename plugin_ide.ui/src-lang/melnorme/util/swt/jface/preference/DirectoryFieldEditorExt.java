/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.util.swt.jface.preference;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.swt.widgets.Composite;

public class DirectoryFieldEditorExt extends DirectoryFieldEditor {
	
	public DirectoryFieldEditorExt(String name, String labelText, Composite parent) {
		super(name, labelText, parent);
	}
	
	@Override
	public void setValidateStrategy(int value) {
		super.setValidateStrategy(DirectoryFieldEditor.VALIDATE_ON_KEY_STROKE);
	}
	
}