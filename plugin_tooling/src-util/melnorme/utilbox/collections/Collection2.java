/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.utilbox.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.RandomAccess;

/**
 * Alternative interface to {@link Collection}, that prevents modifications
 */
public interface Collection2<E> extends Iterable<E>, RandomAccess {
	
	@Override
	Iterator<E> iterator();
	
	/** @return the number of elements in this collection */
	int size();
	
	/** @return <tt>true</tt> if this collection contains no elements */
	boolean isEmpty();
	
	/** @return the receiver (this), with the type parameter recast. 
	 * WARNING: This operation is only safe if: 
	 * - The bound T is a supertype of E, and the returned collection is used only for reading. */
	public <T> Collection2<T> upcastTypeParameter();
	
}