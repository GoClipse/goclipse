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
package melnorme.lang.tooling.ops;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import melnorme.utilbox.core.CommonException;

/**
 * Helper throwable result for "soft" failures: these are non-critical failures, 
 * such as invoking content assist on a source location where that operation cannot be performed.
 * This class exists so that the UI can report these failures to the user in a less intrusive way than
 * a hard error (like an unexpected termination of the Content Assist deamon).
 */
@SuppressWarnings("serial")
public class OperationSoftFailure extends Exception {
	
	public OperationSoftFailure(String message) {
		super(assertNotNull(message));
	}
	
	@Override
	public String getMessage() {
		return super.getMessage();
	}
	
	public CommonException toCommonException() {
		return new CommonException(getMessage());
	}
	
}