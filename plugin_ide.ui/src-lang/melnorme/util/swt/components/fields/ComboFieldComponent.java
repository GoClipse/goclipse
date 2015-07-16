/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.util.swt.components.fields;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.SWTUtil;
import melnorme.util.swt.components.AbstractFieldComponent;
import melnorme.utilbox.misc.StringUtil;

/* FIXME: write test*/
public class ComboFieldComponent extends AbstractFieldComponent<String> {
	
	protected int comboStyle = SWT.DROP_DOWN | SWT.READ_ONLY;
	
	protected Combo combo;
	
	public ComboFieldComponent() {
		super();
	}
	
	@Override
	public int getPreferredLayoutColumns() {
		return 1;
	}
	
	@Override
	protected void createContents(Composite topControl) {
		createCombo(topControl);
	}
	
	protected void createCombo(Composite topControl) {
		combo = SWTFactoryUtil.createCombo(topControl, comboStyle, 
			gdFillDefaults().grab(false, false).create());
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setFieldValueFromControl(combo.getSelectionIndex() == -1 ? null : combo.getText());
			}
		});
	}
	
	public void setComboItems(String... items) {
		if(SWTUtil.isOkToUse(combo) && items.length > 0) {
			combo.setItems(items);
			combo.select(0);
		}
	}
	
	@Override
	protected Control getFieldControl() {
		return combo;
	}
	
	@Override
	protected void doUpdateComponentFromValue() {
		String stringValue = StringUtil.asString(getFieldValue());
		if(stringValue != null) {
			combo.setText(stringValue);
		}
	}
	
}