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

import melnorme.utilbox.fields.IDomainField;

public interface IValidatedField<TYPE> extends IValidationSource {
	
	public TYPE getValidatedField() throws StatusException;
	
	@Override
	default StatusException getValidationStatus() {
		try {
			getValidatedField();
			return null;
		} catch (StatusException se) {
			return se;
		}
	}
	
	/* -----------------  ----------------- */
	
	public static class NullValidationSource<T> implements IValidatedField<T> {
		
		public static <T> NullValidationSource<T> create() {
			return new NullValidationSource<>();
		}
		
		@Override
		public T getValidatedField() throws StatusException {
			return null;
		}
	}
	
	public static class ValidatedField implements IValidatedField<Object> {
		
		public final IDomainField<String> field;
		public final IFieldValidator validator;
		
		public ValidatedField(IDomainField<String> field, IFieldValidator validator) {
			this.field = field;
			this.validator = validator;
		}
		
		@Override
		public Object getValidatedField() throws StatusException {
			String fieldValue = field.getFieldValue();
			return validator.getValidatedField(fieldValue);
		}
	}
	
}