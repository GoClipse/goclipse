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
package melnorme.utilbox.core.fntypes;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

public abstract class ResultRunnable<RESULT> implements Runnable {
	
	protected volatile RESULT result;
	protected boolean executed = false;
	
	public abstract RESULT call();
	
	@Override
	public void run() {
		result = call();
		executed = true;
	}
	
	public RESULT getResult() {
		assertTrue(executed);
		return result;
	}
	
}