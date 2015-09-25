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

/**
 * Interface for tree elements.
 */
public interface IElement {
	
	// Empty array for optimization
	IElement[] NO_ELEMENTS = new IElement[0];
	
	/** Returns the parent of this node, or <code>null</code> if none. */
	IElement getParent();

	/**
	 * Returns whether this element has one or more immediate children. This is
	 * a convenience method, and may be more efficient than testing whether
	 * <code>getChildren</code> is an empty array.
	 */
	boolean hasChildren();

	/** Returns the node's children. */
	IElement[] getChildren();

}