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

/**
 * Second version of EnablementButtonTextField2, 
 * using AbstractCompositeWidget as base instead
 */
public abstract class EnablementButtonTextField2 extends EnablementCompositeWidget<String> {
	
	protected final ButtonTextField buttonTextField;
	
	public EnablementButtonTextField2(String fieldLabel) {
		this(fieldLabel, LABEL_UseDefault);
	}
	
	public EnablementButtonTextField2(String fieldLabel, String useDefaultCheckboxLabel) {
		super(fieldLabel, useDefaultCheckboxLabel);
		this.createInlined = false;
		
		this.buttonTextField = init_createButtonTextField();
		addChildWidget(buttonTextField);
		
		field().addListener((newValue) -> buttonTextField.set(newValue));
		buttonTextField.addListener((newValue) -> field().set(newValue));
	}
	
	protected abstract ButtonTextField init_createButtonTextField();
	
	public ButtonTextField getButtonTextField() {
		return buttonTextField;
	}
	
}