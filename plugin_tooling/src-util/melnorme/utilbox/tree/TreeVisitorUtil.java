/*******************************************************************************
 * Copyright (c) 2007 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial implementation
 *******************************************************************************/
package melnorme.utilbox.tree;


/** Some utility methods for visitors. */
public abstract class TreeVisitorUtil {
	
	/** Accepts the visitor on child. If child is null, nothing happens. */
	public static <T> void acceptChildren(T visitor, IVisitable<T> child) {
		if (child != null) {
			child.accept(visitor);
		}
	}
	
	/** Accepts the visitor on the children. If children is null, nothing happens. */
	public static <T> void acceptChildren(T visitor, Iterable<? extends IVisitable<T>> children) {
		if (children == null)
			return;
		
		for(IVisitable<T> child : children) {
			acceptChildren(visitor, child);
		}
	}
	
}