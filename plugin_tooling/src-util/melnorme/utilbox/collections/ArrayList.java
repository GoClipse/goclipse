/*******************************************************************************
 * Copyright (c) 2014, 2014 IBM Corporation and others.
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


/**
 * Alias to ArrayListExt, if people want to use the ArrayList name.
 */
public class ArrayList<E> extends ArrayList2<E> {
	
	private static final long serialVersionUID = -50917612746064995L;
	
	public ArrayList() {
		super();
	}
	
	public ArrayList(Collection<? extends E> c) {
		super(c);
	}
	
	@SafeVarargs
	public ArrayList(E... elements) {
		super(elements);
	}
	
	public ArrayList(int initialCapacity) {
		super(initialCapacity);
	}
	
	
}