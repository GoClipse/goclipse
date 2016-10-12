/*******************************************************************************
 * Copyright (c) 2013 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.utilbox.collections.iter;

import java.util.Iterator;

/**
 * Interface for an iterator that allows to create a copy of current state of iteration. 
 */
public interface ICopyableIterator<T> extends Iterator<T> {
	
	public ICopyableIterator<T> copyState();
	
	/** Returns a version of this object which has been optimized according to current state. 
	 * Can either return the receiver instance itself, or another object with equal state. In this later case, 
	 * the receiver instance is not valid anymore. */
	public ICopyableIterator<T> optimizedSelf();
	
}