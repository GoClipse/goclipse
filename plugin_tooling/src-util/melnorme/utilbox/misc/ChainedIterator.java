/*******************************************************************************
 * Copyright (c) 2007, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial implementation
 *******************************************************************************/
package melnorme.utilbox.misc;

import java.util.Iterator;

@Deprecated
public class ChainedIterator<T> extends melnorme.utilbox.collections.ChainedIterator<T> {
	
	public ChainedIterator(Iterator<? extends T> firstIter, Iterator<? extends T> secondIter) {
		super(firstIter, secondIter);
	}

}

