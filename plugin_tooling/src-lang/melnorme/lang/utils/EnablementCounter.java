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
package melnorme.lang.utils;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import melnorme.utilbox.ownership.Disposable;
import melnorme.utilbox.ownership.Disposable.CheckedDisposable;

public class EnablementCounter {
	
	protected int enablementCounter = 0;
	
	public boolean isEnabled() {
		return enablementCounter == 0;
	}
	
	public Disposable enterDisable() {
		enablementCounter++;
		return new CheckedDisposable(() -> leaveDisable());
	}
	
	public void leaveDisable() {
		assertTrue(enablementCounter > 0);
		enablementCounter--;
	}
	
}