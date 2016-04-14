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
package melnorme.lang.tooling.data;

import melnorme.lang.tooling.data.IValidatableValue.ValidatableField;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.fields.Field;
import melnorme.utilbox.fields.IFieldView;

public class CompositeValidatableField extends Field<IStatusMessage> implements IValidationSource {
	
	protected final ArrayList2<IValidationSource> validators = new ArrayList2<>();
	
	public <SOURCE> void addFieldValidation(boolean init, IFieldView<SOURCE> field, IValidator<SOURCE, ?> validator) {
		addFieldValidation(init, field, new ValidatableField<>(field, validator));
	}
	
	public void addFieldValidation(boolean init, IFieldView<?> field, IValidationSource validationSource) {
		validators.add(validationSource);
		field.registerListener(init, () -> updateFieldValue());
	}
	
	public void addValidatableField(boolean init, IValidatableField<?> statusField) {
		addFieldValidation(init, statusField, statusField);
	}
	
	public void addStatusField(boolean init, IFieldView<IStatusMessage> statusField) {
		addFieldValidation(init, statusField, () -> statusField.getFieldValue());
	}
	
	public void updateFieldValue() {
		setFieldValue(IValidationSource.getHighestStatus(validators));
	}
	
	@Override
	public IStatusMessage getValidationStatus() {
		return getFieldValue();
	}
	
}