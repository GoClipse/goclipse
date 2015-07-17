/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.utilbox.fields;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

public class NonNullDomainField<VALUE> extends DomainField<VALUE> {
	
	protected final VALUE defaultFieldValue;
	
	public NonNullDomainField(VALUE defaultFieldValue) {
		super(defaultFieldValue);
		this.defaultFieldValue = defaultFieldValue;
	}
	
	@Override
	public void setFieldValue(VALUE value) {
		if(value == null) {
			value = defaultFieldValue;
		}
		doSetFieldValue(value);
	}
	
	@Override
	public VALUE getFieldValue() {
		return assertNotNull(super.getFieldValue());
	}
	
}