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
import org.eclipse.swt.widgets.Shell;

import melnorme.utilbox.concurrency.OperationCancellation;

public class DirectoryTextField extends ButtonTextField {
	
	public DirectoryTextField(String label) {
		super(label, FileTextField.DEFAULT_BUTTON_LABEL);
	}
	
	public DirectoryTextField(String label, String buttonlabel) {
		super(label, buttonlabel);
	}
	
	@Override
	protected String getNewValueFromButtonSelection2() throws OperationCancellation {
		return openDirectoryDialog(getFieldValue(), button.getShell());
	}
	
	public static String openDirectoryDialog(String initialValue, Shell shell) throws OperationCancellation {
		DirectoryDialog dialog = new DirectoryDialog(shell);
		if(!initialValue.isEmpty()) {
			dialog.setFilterPath(initialValue);
		}
		String result = dialog.open();
		if(result == null) {
			throw new OperationCancellation();
		}
		return result;
	}
	
}