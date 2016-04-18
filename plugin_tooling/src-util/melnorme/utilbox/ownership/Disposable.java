/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.utilbox.ownership;

/**
 * General interface for an object that can (and should) be disposed.
 * 
 * Unless otherwise specified, {@link #dispose()} can only be called once, and after
 * that the underlying object will be in a dispose state, where most functionality is no longer available.
 */
public interface Disposable {
	
	void dispose();
	
	/* -----------------  ----------------- */
	
	/** 
	 * Dispose given disposable, and return null. 
	 * The null return is just syntax sugar to nullify a disposed variable in one line, like this:
	 * <code>myObject = IDisposable.dispose(myObject) </code>. 
	 */
	static <T extends Disposable> T dispose(T disposable) {
		if (disposable != null) {
			disposable.dispose();
		}
		return null;
	}
	
}