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

import java.util.Optional;
import java.util.function.Supplier;

import org.eclipse.core.resources.IProject;

import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.fields.IFieldView;


public interface IGlobalPreference<T> extends Supplier<T> {
	
	IFieldView<T> asField();
	
	@Override
	default T get() {
		return asField().get();
	}
	
	T getDefaultValue();
	
	void setInstanceScopeValue(T value) throws CommonException;
	
	
	IProjectPreference<T> getProjectPreference();
	
	default T getEffectiveValue(IProject project) {
		return getProjectPreference().getEffectiveValue(Optional.of(project));
	}
	
}