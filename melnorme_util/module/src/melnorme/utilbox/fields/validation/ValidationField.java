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
package melnorme.utilbox.fields.validation;

import static melnorme.utilbox.core.CoreUtil.list;

import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.fields.Field;
import melnorme.utilbox.fields.IFieldView;
import melnorme.utilbox.status.IStatusMessage;

public class ValidationField extends Field<IStatusMessage> implements ValidationSource {
	
	protected final ArrayList2<ValidationSource> validators = new ArrayList2<>();
	
	protected IStatusMessage explicitStatus;
	
	public ValidationField() {
		super(null);
		
		validators.add(() -> explicitStatus);
	}
	
	@Override
	public IStatusMessage getValidationStatus() {
		return getFieldValue();
	}
	
	public void updateValidation() {
		setFieldValue(ValidationSource.getHighestStatus(validators));
	}
	
	public void setExplicitStatus(IStatusMessage explicitStatus) {
		this.explicitStatus = explicitStatus;
		updateValidation();
	}
	
	/* -----------------  ----------------- */
	
	/**
	 * {@link #addFieldValidation2(boolean, IFieldView, ValidationSource)}
	 */
	public void addFieldValidation2(IFieldView<?> field, ValidationSource validationSource) {
		addFieldValidation2(true, field, validationSource); 
	}
	
	/**
	 * {@link #addFieldValidation2(boolean, Indexable, ValidationSource)}
	 */
	protected void addFieldValidation2(boolean init, IFieldView<?> field, ValidationSource validationSource) {
		addFieldValidation2(init, list(field), validationSource);
	}
	
	/**
	 * Add a validation source derived from given field.
	 * 
	 * Note: it is highly recommended that given validationSource calculation only depends from given fields,
	 * otherwise a manual call to {@link #updateValidation()} will be required to update this validation field.
	 * 
	 * @param init
	 * @param field
	 * @param validationSource
	 */
	public void addFieldValidation2(boolean init, Indexable<IFieldView<?>> fields, ValidationSource validationSource) {
		validators.add(validationSource);
		for (IFieldView<?> sourceField : fields) {
			sourceField.registerListener(init, (__) -> updateValidation());
		}
	}
	
	public void addStatusField(boolean init, IFieldView<IStatusMessage> statusField) {
		addFieldValidation2(init, statusField, () -> statusField.getFieldValue());
	}
	
}