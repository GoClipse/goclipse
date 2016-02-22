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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import melnorme.utilbox.fields.IFieldView;

public interface IValidatableValue<TYPE> extends IValidationSource {
	
	public TYPE getValidatedValue() throws StatusException;
	
	@Override
	default StatusException getValidationStatus() {
		try {
			getValidatedValue();
			return null;
		} catch (StatusException se) {
			return se;
		}
	}
	
	/* -----------------  ----------------- */
	
	public static class NullValidatableValue<T> implements IValidatableValue<T> {
		
		public static <T> NullValidatableValue<T> create() {
			return new NullValidatableValue<>();
		}
		
		@Override
		public T getValidatedValue() throws StatusException {
			return null;
		}
	}
	
	public static class ValidatableField<SOURCE> implements IValidatableValue<Object> {
		
		public final IFieldView<SOURCE> property;
		public final IValidator<SOURCE, ?> validator;
		
		public ValidatableField(IFieldView<SOURCE> field, IValidator<SOURCE, ?> validator) {
			this.property = assertNotNull(field);
			this.validator = assertNotNull(validator);
		}
		
		@Override
		public Object getValidatedValue() throws StatusException {
			return validator.getValidatedField(property.getValue());
		}
	}
	
}