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
package melnorme.util.swt.components.fields;

import org.eclipse.swt.widgets.DirectoryDialog;

public class DirectoryTextField extends ButtonTextField {
	
	public DirectoryTextField(String label, String buttonlabel) {
		super(label, buttonlabel);
	}
	
	@Override
	protected String getNewValueFromButtonSelection() {
		DirectoryDialog dialog = new DirectoryDialog(button.getShell());
		if(!getFieldValue().isEmpty()) {
			dialog.setFilterPath(getFieldValue());
		}
		return dialog.open();
	}
	
}