/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.utilbox.fields;

/**
 * A property that can be modifed (set/get) and observed.
 */
public interface IField<TYPE> extends IFieldView<TYPE>, IProperty<TYPE> {
	
	@Override
	default TYPE get() {
		return IFieldView.super.get();
	}
	
	@Override
	default void set(TYPE value) {
		setFieldValue(value);
	}
	
	void setFieldValue(TYPE value);
	
}