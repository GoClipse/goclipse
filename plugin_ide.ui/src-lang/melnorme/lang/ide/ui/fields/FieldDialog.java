/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.fields;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import melnorme.util.swt.components.AbstractWidget;
import melnorme.util.swt.components.fields.FieldCompositeWidget;
import melnorme.utilbox.concurrency.OperationCancellation;

public class FieldDialog<VALUE> extends Dialog {
	
	protected final FieldCompositeWidget<VALUE> fieldEditor;
	
	public FieldDialog(Shell parentShell, FieldCompositeWidget<VALUE> fieldEditor) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE);
		this.fieldEditor = assertNotNull(fieldEditor);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite topControl = (Composite) super.createDialogArea(parent);
		return fieldEditor.createComponent(topControl, AbstractWidget.gdGrabAll(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	public VALUE openDialog(VALUE initialValue) throws OperationCancellation {
		fieldEditor.field().set(initialValue);
		
		int result = open();
		if(result == Dialog.OK) {
			return fieldEditor.field().get();
		}
		throw new OperationCancellation();
	}
	
}