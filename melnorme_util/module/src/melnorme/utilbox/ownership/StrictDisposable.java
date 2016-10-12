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
package melnorme.utilbox.ownership;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

/**
 * A {@link IDisposable} that can only be disposed once. 
 */
public abstract class StrictDisposable implements IDisposable {
	
	private boolean isDisposed = false;
	
	@Override
	public final void dispose() {
		assertTrue(!isDisposed);
		isDisposed = true;
		disposeDo();
	}
	
	protected abstract void disposeDo();
	
}