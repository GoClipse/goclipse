/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.tooling.data;


public class AbstractValidator {
	
	public AbstractValidator() {
	}
	
	public static StatusException getFieldStatus(IFieldValidator<?> validator, String value) {
		if(validator == null) {
			return null;
		}
		try {
			validator.getValidatedField(value);
		} catch (StatusException e) {
			return e;
		}
		return null;
	}
	
}