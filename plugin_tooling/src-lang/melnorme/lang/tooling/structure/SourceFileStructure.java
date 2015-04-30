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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.CoreUtil.areEqual;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.misc.HashcodeUtil;
import melnorme.utilbox.misc.Location;

public class SourceFileStructure implements ISourceFileStructure {
	
	protected final Location location;
	protected final Indexable<IStructureElement> children;
	
	public SourceFileStructure(Location location, Indexable<IStructureElement> children) {
		this.location = assertNotNull(location);
		this.children = assertNotNull(children);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof SourceFileStructure)) return false;
		
		SourceFileStructure other = (SourceFileStructure) obj;
		
		return 
			areEqual(location, other.location) &&
			areEqual(children, other.children);
	}
	
	@Override
	public int hashCode() {
		return HashcodeUtil.combinedHashCode(location, children.size());
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + location;
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public Location getLocation() {
		return location;
	}
	
	@Override
	public Indexable<IStructureElement> getChildren() {
		return children;
	}
	
	@Override
	public String getModuleName() {
		return location.getPath().getFileName().toString();
	}
	
}