/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.utils.prefs;

import org.osgi.service.prefs.BackingStoreException;

import melnorme.utilbox.fields.IFieldView;


public interface IGlobalPreference<T> {
	
	IFieldView<T> asField();
	
	default T get() {
		return asField().getValue();
	}
	
	T getDefaultValue();
	
	void setInstanceScopeValue(T value) throws BackingStoreException;
	
}