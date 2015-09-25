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

import melnorme.utilbox.misc.ListenerListHelper;

/**
 * A value that listener/observer can connect to and listen for modification.
 */
public class DomainField<VALUE> implements IDomainField<VALUE> {
	
	private VALUE value; // private to prevent direct modifications.
	
	public DomainField(VALUE defaultFieldValue) {
		this.value = defaultFieldValue;
	}
	
	public DomainField() {
		this.value = getDefaultFieldValue();
	}
	
	public VALUE getDefaultFieldValue() {
		return null;
	}
	
	/* ----------------- listeners ----------------- */
	
	protected final ListenerListHelper<IFieldValueListener> listeners = new ListenerListHelper<>();
	
	@Override
	public void addValueChangedListener(IFieldValueListener listener) {
		listeners.addListener(listener);
	}
	//alias
	public void addListener(IFieldValueListener listener) {
		addValueChangedListener(listener);
	}
	
	@Override
	public void removeValueChangedListener(IFieldValueListener listener) {
		listeners.removeListener(listener);
	}
	//alias
	public void removeListener(IFieldValueListener listener) {
		removeValueChangedListener(listener);
	}
	
	
	public void fireFieldValueChanged() {
		fieldValueChanged();
		for (IFieldValueListener listener : listeners.getListeners()) {
			listener.fieldValueChanged();
		}
	}
	
	// this methos allows listening by means of overriding this class
	protected void fieldValueChanged() {
		// Default: do nothing
	}
	
	@Override
	public VALUE getFieldValue() {
		return value;
	}
	
	@Override
	public void setFieldValue(VALUE value) {
		doSetFieldValue(value);
	}
	
	protected boolean notifyingListeners;
	
	public boolean isNotifyingListeners() {
		return notifyingListeners;
	}
	
	protected void doSetFieldValue(VALUE newValue) {
		if(notifyingListeners) {
			handleReentrantSetValue(newValue);
		} else {
			
			this.value = newValue;
			
			notifyingListeners = true;
			try {
				fireFieldValueChanged();
			} finally {
				notifyingListeners = false;
			}
		}
		
	}
	
	protected void handleReentrantSetValue(VALUE newValue) {
		// This shouldn't happen in the first place, bad style.
		this.value = newValue;
	}
	
}