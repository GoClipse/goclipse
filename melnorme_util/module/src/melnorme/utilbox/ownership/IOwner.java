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

public interface IOwner {
	
	/**
	 * Bind given disposable to the lifecycle of this owner.
	 */
	void bind(IDisposable disposable);
	
	/**
	 * Unbind given disposable to the lifecycle of this owner.
	 * @return true if successfull, false if disposable wasn't bound to this owner.
	 */
	public boolean unbind(IDisposable disposable);
	
	/**
	 * Dispose the given disposable, if it is not null.
	 * @param A disposable that is currently bound to the lifecycle of this owner, or null.
	 */
	default void disposeOwned(IDisposable disposable) {
		if(disposable == null) {
			return;
		}
		boolean removed = unbind(disposable);
		assertTrue(removed);
		disposable.dispose();
	}
	
	/**
	 * Dispose all disposables bound to this owner.
	 */
	void disposeAll();
	
}