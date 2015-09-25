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

import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.swt.widgets.Composite;

public class FileFieldEditorExt extends FileFieldEditor {
	
	public FileFieldEditorExt(String name, String labelText, Composite parent) {
		super(name, labelText, false, VALIDATE_ON_KEY_STROKE, parent);
	}
	
	@Override
	protected boolean checkState() {
		return true;
	}
	
}