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
package melnorme.utilbox.fields.validation;

import melnorme.utilbox.status.StatusException;

public interface ValidatedValueSource<VALUE> extends ValidationSource {
	
	public VALUE getValidatedValue() throws StatusException;
	
	@Override
	default StatusException getValidationStatus() {
		try {
			getValidatedValue();
			return null;
		} catch (StatusException se) {
			return se;
		}
	}
	
}