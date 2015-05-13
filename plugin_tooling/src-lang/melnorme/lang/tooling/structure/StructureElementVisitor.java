/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.tooling.structure;

public abstract class StructureElementVisitor {
	
	int level = 0;
	
	public StructureElementVisitor() {
	}
	
	protected void visitTree(StructureElement element) {
		level++;
		
		if(visitNode(element)) {
			for (StructureElement child : element.getChildren()) {
				visitTree(child);
			}
		}
		level--;
	}
	
	/** @return whether to visit children. */
	protected abstract boolean visitNode(StructureElement element);
	
}