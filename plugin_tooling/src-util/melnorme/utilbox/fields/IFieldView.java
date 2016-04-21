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

import melnorme.utilbox.fields.FieldValueListener.FieldChangeListener;
import melnorme.utilbox.ownership.IDisposable;
import melnorme.utilbox.ownership.IOwner;

/**
 * A read-only access for an observable value.
 */
public interface IFieldView<VALUE> extends Supplier<VALUE> {
	
	@Override
	default VALUE get() {
		return getFieldValue();
	}
	
	VALUE getFieldValue();
	
	void addListener(FieldValueListener<VALUE> listener);
	
	default void addListener(boolean initialize, FieldValueListener<VALUE> listener) {
		addListener(listener);
		if(initialize) {
			listener.fieldValueChanged(getFieldValue());
		}
	}
	
	@SuppressWarnings("unchecked")
	default void addChangeListener(FieldChangeListener listener) {
		addListener(listener);
	}
	
	void removeListener(FieldValueListener<VALUE> listener);
	
	@SuppressWarnings("unchecked")
	default void removeChangeListener(FieldChangeListener listener) {
		removeListener(listener);
	}
	
	/* -----------------  ----------------- */
	
	default FieldListenerRegistration registerListener(FieldValueListener<VALUE> listener) {
		addListener(listener);
		return new FieldListenerRegistration(this, listener);
	}
	
	default FieldListenerRegistration registerListener(boolean initListener, FieldValueListener<VALUE> listener) {
		FieldListenerRegistration binding = registerListener(listener);
		if(initListener) {
			listener.fieldValueChanged(getFieldValue());
		}
		return binding;
	}
	
	@SuppressWarnings("unchecked")
	default FieldListenerRegistration registerChangeListener(boolean initListener, FieldChangeListener listener) {
		return registerListener(initListener, listener);
	}
	
	/**
	 * Register a value changed listener, that automatically get unregistered when given ownedList is disposed.
	 */
	default void bindOwnedListener(IOwner owner, FieldValueListener<VALUE> listener) {
		registerListener(listener).bindLifetime(owner);
	}
	
	default void bindOwnedListener(IOwner owner, boolean initListener, FieldValueListener<VALUE> listener) {
		registerListener(listener).bindLifetime(owner);
		if(initListener) {
			listener.fieldValueChanged(getFieldValue());
		}
	}
	
	public static class FieldListenerRegistration implements IDisposable {
		
		@SuppressWarnings("rawtypes")
		protected final IFieldView field;
		@SuppressWarnings("rawtypes")
		protected final FieldValueListener listener;
		
		public <T> FieldListenerRegistration(IFieldView<T> field, FieldValueListener<T> listener) {
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
		public void addListener(FieldValueListener<Object> listener) {
			// Do nothing
		}
		
		@Override
		public void removeListener(FieldValueListener<Object> listener) {
			// Do nothing
		}
		
	};
	
}