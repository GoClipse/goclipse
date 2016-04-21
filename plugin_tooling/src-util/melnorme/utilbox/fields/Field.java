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

import melnorme.utilbox.collections.Indexable;

/**
 * Default implementation of a {@link IField}, an observable property.
 */
public class Field<TYPE> implements IField<TYPE> {
	
	protected final ListenerListHelper<FieldValueListener<TYPE>> listeners = new ListenerListHelper<>();
	
	private TYPE value; // private to prevent direct modifications.
	
	public Field() {
		this(null);
	}
	
	public Field(TYPE defaultFieldValue) {
		this.value = defaultFieldValue;
	}
	
	@Override
	public void addListener(FieldValueListener<TYPE> listener) {
		listeners.addListener(listener);
	}
	
	@Override
	public void removeListener(FieldValueListener<TYPE> listener) {
		listeners.removeListener(listener);
	}
	
	protected Indexable<FieldValueListener<TYPE>> getListeners() {
		return listeners.getListeners();
	}
	
	/* ----------------- listeners ----------------- */
	
	public void fireFieldValueChanged() {
		TYPE newFieldValue = getFieldValue();
		fieldValueChanged();
		for (FieldValueListener<? super TYPE> listener : listeners.getListeners()) {
			listener.fieldValueChanged(newFieldValue);
		}
	}
	
	// this methos allows listening by means of overriding this class
	protected void fieldValueChanged() {
		// Default: do nothing
	}
	
	@Override
	public TYPE getFieldValue() {
		return value;
	}
	
	@Override
	public void setFieldValue(TYPE value) {
		doSetFieldValue(value);
	}
	
	protected boolean notifyingListeners;
	
	public boolean isNotifyingListeners() {
		return notifyingListeners;
	}
	
	protected void doSetFieldValue(TYPE newValue) {
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
	
	protected void handleReentrantSetValue(TYPE newValue) {
		// This shouldn't happen in the first place, bad style.
		this.value = newValue;
	}
	
}