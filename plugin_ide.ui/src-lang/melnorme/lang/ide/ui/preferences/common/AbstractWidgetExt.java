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
package melnorme.lang.ide.ui.preferences.common;


import melnorme.lang.tooling.data.IStatusMessage;
import melnorme.lang.tooling.data.IValidatableField;
import melnorme.lang.tooling.data.IValidationSource;
import melnorme.lang.tooling.data.MultipleFieldValidation;
import melnorme.util.swt.components.AbstractWidget;

public abstract class AbstractWidgetExt extends AbstractWidget implements IValidationSource {
	
	protected final MultipleFieldValidation validation = new MultipleFieldValidation();
	
	public AbstractWidgetExt() {
		super();
	}
	
	@Override
	public IStatusMessage getValidationStatus() {
		return validation.getValidationStatus();
	}
	
	public IValidatableField<IStatusMessage> getStatusField() {
		return validation;
	}
	
}