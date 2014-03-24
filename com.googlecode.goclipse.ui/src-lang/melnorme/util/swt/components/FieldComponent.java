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

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * A more high level SWT component.
 * XXX: needs more work @see also {@link FieldWithListeners}
 */
public abstract class FieldComponent<T> {
	
 	public final Control createComponent(Composite parent, Object layoutData) {
 		Control control = createComponent(parent);
 		control.setLayoutData(layoutData);
 		return control;
 	}
 	
 	public abstract Control createComponent(Composite parent);
	
	public abstract T getFieldValue();

	public abstract void setFieldValue(T projectName);
	
	@SuppressWarnings("unused") 
	protected void fieldValueChanged(T newFieldValue) {
	}
	
}