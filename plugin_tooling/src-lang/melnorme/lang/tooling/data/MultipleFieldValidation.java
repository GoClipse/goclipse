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

import java.util.ArrayList;

import melnorme.lang.tooling.data.IValidatedField.ValidatedField;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.fields.DomainField;
import melnorme.utilbox.fields.IDomainField;

public class MultipleFieldValidation extends DomainField<IStatusMessage> implements IValidationSource {
	
	protected final ArrayList<ValidatedField> fields = new ArrayList2<>();
	
	public void addValidatedField(IDomainField<String> field, IFieldValidator validator) {
		fields.add(new ValidatedField(field, validator));
		field.addValueChangedListener2(true, () -> updateFieldValue());
	}
	
	protected void updateFieldValue() {
		try {
			for(ValidatedField validatedField : fields) {
				validatedField.getValidatedField(); // result is ignored, only care about throw
			}
			setFieldValue(null);
		} catch(StatusException se) {
			setFieldValue(se);
		}
	}
	
	@Override
	public IStatusMessage getValidationStatus() {
		return getFieldValue();
	}
	
}