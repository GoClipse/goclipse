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

public interface IModelField<VALUE> extends IFieldView<VALUE>, IProperty<VALUE> {
	
	@Override
	default VALUE getValue() {
		return IFieldView.super.getValue();
	}
	
	@Override
	default void setValue(VALUE value) {
		setFieldValue(value);
	}
	
	void setFieldValue(VALUE value);
	
}