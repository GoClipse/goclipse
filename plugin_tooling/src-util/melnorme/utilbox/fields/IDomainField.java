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

import melnorme.utilbox.ownership.IDisposable;
import melnorme.utilbox.ownership.IOwnedList;

public interface IDomainField<VALUE> {
	
	VALUE getFieldValue();
	
	void setFieldValue(VALUE value);
	
	void addValueChangedListener(IFieldValueListener listener);
	
	void removeValueChangedListener(IFieldValueListener listener);
	
	/* -----------------  ----------------- */
	
	default FieldListenerBinding addValueChangedListener2(IFieldValueListener listener) {
		addValueChangedListener(listener);
		return new FieldListenerBinding(this, listener);
	}
	
	public static class FieldListenerBinding implements IDisposable {
		
		protected final IDomainField<?> field;
		protected final IFieldValueListener listener;
		
		public FieldListenerBinding(IDomainField<?> field, IFieldValueListener listener) {
			this.field = field;
			this.listener = listener;
		}
		
		@Override
		public void dispose() {
			field.removeValueChangedListener(listener);
		}
		
		public void bindLifetime(IOwnedList<IDisposable> ownedList) {
			ownedList.add(this);
		}
		
	}
	
	/**
	 * Register a value changed listener, that automatically get unregistered when given ownedList is disposed.
	 */
	default void addOwnedListener(IOwnedList<IDisposable> ownedList, IFieldValueListener listener) {
		addValueChangedListener2(listener).bindLifetime(ownedList);
	}
	
}