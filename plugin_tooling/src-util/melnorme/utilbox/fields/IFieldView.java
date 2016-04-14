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

import java.util.function.Supplier;

import melnorme.utilbox.ownership.IDisposable;
import melnorme.utilbox.ownership.IOwner;

/**
 * A read-only access for an observable value.
 */
public interface IFieldView<TYPE> extends Supplier<TYPE> {
	
	@Override
	default TYPE get() {
		return getFieldValue();
	}
	
	TYPE getFieldValue();
	
	void addListener(IFieldValueListener listener);
	
	void removeListener(IFieldValueListener listener);
	
	/* -----------------  ----------------- */
	
	default FieldListenerRegistration registerListener(IFieldValueListener listener) {
		addListener(listener);
		return new FieldListenerRegistration(this, listener);
	}
	
	default FieldListenerRegistration registerListener(boolean initListener, IFieldValueListener listener) {
		FieldListenerRegistration binding = registerListener(listener);
		if(initListener) {
			listener.fieldValueChanged();
		}
		return binding;
	}
	
	/**
	 * Register a value changed listener, that automatically get unregistered when given ownedList is disposed.
	 */
	default void bindOwnedListener(IOwner owner, IFieldValueListener listener) {
		registerListener(listener).bindLifetime(owner);
	}
	
	default void bindOwnedListener(IOwner owner, boolean initListener, IFieldValueListener listener) {
		registerListener(listener).bindLifetime(owner);
		if(initListener) {
			listener.fieldValueChanged();
		}
	}
	
	public static class FieldListenerRegistration implements IDisposable {
		
		protected final IFieldView<?> field;
		protected final IFieldValueListener listener;
		
		public FieldListenerRegistration(IFieldView<?> field, IFieldValueListener listener) {
			this.field = field;
			this.listener = listener;
		}
		
		@Override
		public void dispose() {
			field.removeListener(listener);
		}
		
		public void bindLifetime(IOwner owner) {
			owner.bind(this);
		}
		
	}
	
	/* -----------------  ----------------- */
	
	@SuppressWarnings("unchecked")
	public static <T> IFieldView<T> NULL_FIELD_VIEW() {
		return (IFieldView<T>) NULL_FIELD_VIEW;
	}
	
	public static final IFieldView<Object> NULL_FIELD_VIEW = new IFieldView<Object>() {
		
		@Override
		public Object getFieldValue() {
			return null;
		}
		
		@Override
		public void addListener(IFieldValueListener listener) {
			// Do nothing
		}
		
		@Override
		public void removeListener(IFieldValueListener listener) {
			// Do nothing
		}
		
	};
	
}