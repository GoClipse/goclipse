/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.util.swt.components;

import melnorme.utilbox.misc.ListenerListHelper;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

// TODO: combine this class with FieldComponent
public abstract class FieldWithListeners<T> {
	
	public static interface IFieldValueListener {
		
		void fieldValueChanged();
		
	}
	
	public FieldWithListeners() {
		super();
	}
	
	protected final ListenerListHelper<IFieldValueListener> listeners = new ListenerListHelper<>();
	
	public void addValueChangedListener(IFieldValueListener listener) {
		listeners.addListener(listener);
	}
	
	public void removeValueChangedListener(IFieldValueListener listener) {
		listeners.removeListener(listener);
	}
	
	protected void fireFieldValueChanged() {
		for (IFieldValueListener listener : listeners.getListeners()) {
			listener.fieldValueChanged();
		}
	}
	
	public abstract Control createComponent(Composite parent);
	
}