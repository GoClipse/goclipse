/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.tooling.data;

import melnorme.lang.tooling.data.IValidationSource.IValidationSourceX;
import melnorme.utilbox.fields.Field;

/* FIXME: create ValidatedField */
public class SelfValidatedField<TYPE extends IValidationSource> extends Field<TYPE> 
	implements IValidationSourceX, IValidatableField<TYPE> {
	
	public SelfValidatedField() {
		super();
	}
	
	public SelfValidatedField(TYPE defaultFieldValue) {
		super(defaultFieldValue);
	}
	
	@Override
	public void validate() throws StatusException {
		get().validate();
	}
	
}