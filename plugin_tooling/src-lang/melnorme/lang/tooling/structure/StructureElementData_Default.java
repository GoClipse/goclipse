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


abstract class StructureElementData_Default {
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof StructureElementData)) return false;
		
		StructureElementData other = (StructureElementData) obj;
		
		return equals_subClass(other);
	}
	
	protected abstract boolean equals_subClass(StructureElementData other);
	
	@Override
	public int hashCode() {
		return hashCode_subClass();
	}
	
	protected abstract int hashCode_subClass();
	
}