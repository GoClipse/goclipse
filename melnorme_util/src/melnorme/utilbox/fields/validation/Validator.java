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

import java.util.function.Supplier;

import melnorme.utilbox.status.StatusException;

/**
 * Validates a given VALUE into a RESULT.
 */
public interface Validator<VALUE, RESULT> {
	
	public RESULT validateField(VALUE value) throws StatusException;
	
	/* -----------------  ----------------- */
	
	default ValidationSource toValidationSource(Supplier<VALUE> supplier) {
		return ValidationSource.fromRunnable(() -> this.validateField(supplier.get()));
	}
	
}