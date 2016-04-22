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
package melnorme.lang.tooling.data.validation;

import melnorme.lang.tooling.data.IStatusMessage;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.fields.Field;
import melnorme.utilbox.fields.IFieldView;

public class ValidationField extends Field<IStatusMessage> implements ValidationSource {
	
	protected final ArrayList2<ValidationSource> validators = new ArrayList2<>();
	
	protected IStatusMessage explicitStatus;
	
	public ValidationField() {
		super(null);
		
		validators.add(() -> explicitStatus);
	}
	
	public <SOURCE> void addFieldValidator(boolean init, IFieldView<SOURCE> field, Validator<SOURCE, ?> validator) {
		addFieldValidation(init, field, new ValidatableField<>(field, validator));
	}
	
	public final void addFieldValidationX(boolean init, IFieldView<?> field, ValidationSourceX validationSource) {
		addFieldValidation(init, field, validationSource);
	}
	
	public void addFieldValidation(boolean init, IFieldView<?> field, ValidationSource validationSource) {
		validators.add(validationSource);
		field.registerListener(init, (__) -> updateFieldValue());
	}
	
	public void addValidatableField(boolean init, IValidatableField<?> statusField) {
		addFieldValidation(init, statusField, statusField);
	}
	
	public void addStatusField(boolean init, IFieldView<IStatusMessage> statusField) {
		addFieldValidation(init, statusField, () -> statusField.getFieldValue());
	}
	
	public void updateFieldValue() {
		setFieldValue(ValidationSource.getHighestStatus(validators));
	}
	
	@Override
	public IStatusMessage getValidationStatus() {
		return getFieldValue();
	}
	
	public void setExplicitStatus(IStatusMessage explicitStatus) {
		doSetExplicitStatus(explicitStatus);
		updateFieldValue();
	}
	
	protected void doSetExplicitStatus(IStatusMessage explicitStatus) {
		this.explicitStatus = explicitStatus;
	}
	
}