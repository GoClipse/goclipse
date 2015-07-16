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
package melnorme.util.swt.components;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;


public abstract class TextFieldComponent extends AbstractFieldComponent<String> {
	
	protected Text text;
	
	public TextFieldComponent() {
		super("");
	}
	
	@Override
	public String getDefaultFieldValue() {
		return ""; /* FIXME: duplicate default*/
	}
	
	@Override
	protected void doUpdateComponentFromValue() {
		text.setText(getFieldValue());
	}
	
	@Override
	protected Control getFieldControl() {
		return text;
	}
	
	@Override
	protected void createContents(Composite topControl) {
		text = doCreateContents(topControl);
		text.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent evt) {
				setFieldValueFromControl(text.getText());
				
				handleFieldValueAndControlChanged();
			}
		});
	}
	
	protected void handleFieldValueAndControlChanged() {
	}
	
	protected abstract Text doCreateContents(Composite topControl);
	
}