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


/**
 * Common class for a field that supports explicit setting and getting of the field value.
 * Provides some helper/foundation methods.
 */
public abstract class CommonFieldComponent<VALUE> extends FieldValueNotifier implements IFieldComponent<VALUE> {
	
	public CommonFieldComponent() {
		super();
	}
	
	@Override
	public abstract VALUE getFieldValue();
	
	@Override
	public abstract void setFieldValue(VALUE projectName);
	
}