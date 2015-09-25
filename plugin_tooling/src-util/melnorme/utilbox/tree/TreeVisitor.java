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
 * Generic tree walker that can walk any TreeNode. Uses TreeNode.getChildren();
 */
public abstract class TreeVisitor {

	/** Traverses the node. */
	public final void traverse(IElement node) {
		if(enterNode(node)) {
			traverseChildren(node);
		}
		leaveNode(node);
	}
	
	protected final void traverseChildren(IElement node) {
		for(IElement child : node.getChildren()) {
			traverse(child);
		}
	}
	
	/** Performs the specific work on this node, on entry. */
	protected abstract boolean enterNode(IElement node);
	
	/** Performs the specific work on this node, on exit. */
	protected abstract void leaveNode(IElement node);
	
}