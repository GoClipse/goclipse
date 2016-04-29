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
package melnorme.util.swt.components.fields;


import melnorme.util.swt.components.CompositeWidget;
import melnorme.utilbox.fields.Field;

public class FieldCompositeWidget<VALUE> extends CompositeWidget {
	
	protected final Field<VALUE> valueField = init_valueField();
	
	public FieldCompositeWidget(boolean createInlined) {
		super(createInlined);
		
		valueField.addChangeListener(this::updateWidgetFromInput);
	}
	
	protected Field<VALUE> init_valueField() {
		return new Field<>();
	}
	
	public Field<VALUE> field() {
		return valueField;
	}
	
	public VALUE getFieldValue() {
		return field().get();
	}
	
	public void setFieldValue(VALUE newValue) {
		field().set(newValue);
	}
	
}