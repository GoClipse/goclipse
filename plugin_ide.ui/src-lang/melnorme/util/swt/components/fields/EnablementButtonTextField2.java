/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.util.swt.components.fields;

import melnorme.utilbox.fields.Field;

/**
 * Second version of EnablementButtonTextField2, 
 * using AbstractCompositeWidget as base instead
 */
public abstract class EnablementButtonTextField2 extends EnablementCompositeWidget<String> {
	
	protected final ButtonTextField buttonTextField;
	
	public EnablementButtonTextField2(String label) {
		this(label, LABEL_UseDefault);
		this.layoutColumns = 2;
	}
	
	public EnablementButtonTextField2(String label, String useDefaultCheckboxLabel) {
		super(label, useDefaultCheckboxLabel);
		
		this.buttonTextField = init_createButtonTextField(getField());
		addChildWidget(buttonTextField);
	}
	
	protected abstract ButtonTextField init_createButtonTextField(Field<String> field);
	
	public ButtonTextField getButtonTextField() {
		return buttonTextField;
	}
	
}