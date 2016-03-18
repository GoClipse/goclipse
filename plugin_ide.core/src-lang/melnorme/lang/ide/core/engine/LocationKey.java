/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.engine;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.CoreUtil.areEqual;

import melnorme.utilbox.misc.HashcodeUtil;
import melnorme.utilbox.misc.Location;

/**
 * 
 * A {@link LocationKey} is an immutable, value object used as a key.
 * For the most common and typically case, it is simply based on a {@link Location} object, 
 * but alternatively, it can also be based on a non-{@link Location} object. 
 *
 */
public class LocationKey {
	
	protected final Location location;
	protected final Object alternative;
	
	public LocationKey(Location location) {
		this.location = assertNotNull(location);
		this.alternative = null;
	}
	
	public LocationKey(Object alternative) {
		this.location = null;
		this.alternative = alternative;
	}
	
	/** @return the underlying location. Can be null if this key is a non-location  */
	public Location getLocation() {
		return location;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof LocationKey)) return false;
		
		LocationKey other = (LocationKey) obj;
		
		return areEqual(location, other.location) && areEqual(other, other.alternative);
	}
	
	@Override
	public int hashCode() {
		return HashcodeUtil.combinedHashCode(alternative);
	}
	
	@Override
	public String toString() {
		return "KEY:" + (location != null ? location.toString() : "[alternative]"); 
	}
	
}