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
package melnorme.lang.tooling.data;

public interface IValidator<VALUE, RESULT> {
	
	public RESULT getValidatedField(VALUE value) throws StatusException;
	
	static <SOURCE> IStatusMessage getFieldStatus(IValidator<SOURCE, ?> validator, SOURCE value) {
		if(validator == null) {
			return null;
		}
		try {
			validator.getValidatedField(value);
		} catch (StatusException se) {
			return se;
		}
		return null;
	} 
	
}