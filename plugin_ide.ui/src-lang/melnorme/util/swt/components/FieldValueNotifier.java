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

import melnorme.utilbox.fields.IFieldValueListener;
import melnorme.utilbox.misc.ListenerListHelper;

public abstract class FieldValueNotifier {
	
	protected final ListenerListHelper<IFieldValueListener> listeners = new ListenerListHelper<>();
	
	public void addValueChangedListener(IFieldValueListener listener) {
		listeners.addListener(listener);
	}
	
	public void removeValueChangedListener(IFieldValueListener listener) {
		listeners.removeListener(listener);
	}
	
	protected final void fireFieldValueChanged() {
		fieldValueChanged();
		for (IFieldValueListener listener : listeners.getListeners()) {
			listener.fieldValueChanged();
		}
	}
	
	// this methos allows listening by means of overriding this class
	protected void fieldValueChanged() {
		// Default: do nothing
	}
	
}