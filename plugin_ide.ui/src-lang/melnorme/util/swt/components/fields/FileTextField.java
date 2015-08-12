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
package melnorme.util.swt.components.fields;

import melnorme.lang.ide.ui.utils.ControlUtils;
import melnorme.utilbox.concurrency.OperationCancellation;

public class FileTextField extends ButtonTextField {
	
	public static final String DEFAULT_BUTTON_LABEL = "B&rowse...";
	
	public FileTextField(String label) {
		super(label, FileTextField.DEFAULT_BUTTON_LABEL);
	}
	
	public FileTextField(String label, String buttonlabel) {
		super(label, buttonlabel);
	}
	
	@Override
	protected String getNewValueFromButtonSelection2() throws OperationCancellation {
		return ControlUtils.openFileDialog(getFieldValue(), button.getShell());
	}
	
}