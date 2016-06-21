/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.tooling;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.CoreUtil.areEqual;

import melnorme.utilbox.misc.HashcodeUtil;
import melnorme.utilbox.misc.Location;

/**
 * Handle to a location.
 */
public class LocationHandle {
	
	protected final Location location;
	
	public LocationHandle(Location location) {
		this.location = assertNotNull(location);
	}
	
	/** @return the location for this handle. Immutable, not null. */
	public Location getLocation() {
		return location;
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof LocationHandle)) return false;
		
		LocationHandle other = (LocationHandle) obj;
		
		return areEqual(location, other.location);
	}
	
	@Override
	public int hashCode() {
		return HashcodeUtil.combinedHashCode(location);
	}
	
}