/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.tooling.data;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

@SuppressWarnings("serial")
public class InfoResult extends Throwable {
	
	public InfoResult(String message) {
		super(assertNotNull(message));
	}
	
	@Override
	public String getMessage() {
		return super.getMessage();
	}
	
}