/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.tooling.bundle;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import static melnorme.utilbox.core.CoreUtil.areEqual;

import java.nio.file.Path;

import melnorme.lang.tooling.BundlePath;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.MiscUtil;

/**
 * A valid directory path for a dub bundle.
 * Is normalized, absolute, and has at least one segment 
 */
public abstract class AbstractBundlePath {
	
	public static BundlePath create(String pathStr) {
		try {
			Path path = MiscUtil.createPath(pathStr);
			return BundlePath.create(path);
		} catch (CommonException e) {
			return null;
		}
	}
	
	public static BundlePath create(Path path) {
		if(isValidBundlePath(path)) {
			return new BundlePath(Location.fromAbsolutePath(path));
		}
		return null;
	}
	
	public static BundlePath create(Location location) {
		if(isValidBundlePath(location.path)) {
			return new BundlePath(location);
		}
		return null;
	}
	
	public static boolean isValidBundlePath(Path path) {
		assertNotNull(path);
		return path.isAbsolute() && path.getNameCount() > 0;
	}
	
	/* -----------------  ----------------- */
	
	public final Location location;
	
	public AbstractBundlePath(Location location) {
		assertTrue(BundlePath.isValidBundlePath(location.path));
		this.location = location;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof BundlePath)) return false;
		
		BundlePath other = (BundlePath) obj;
		
		return areEqual(location, other.location);
	}
	
	@Override
	public int hashCode() {
		return location.hashCode();
	}
	
	public Location getLocation() {
		return location;
	}
	
	public boolean hasBundleManifest() {
		return getManifestLocation(false) != null;
	}
	
	public abstract Location getManifestLocation(boolean provideDefault);
	
	public Location resolve(Path other) {
		return location.resolve(other);
	}
	
	@Override
	public String toString() {
		return location.toString();
	}
	
	/* -----------------  ----------------- */
	
	public static Location getManifest(Location bundleLoc, Indexable<Path> manifestSubPaths, Path defaultManifestPath) {
		for(Path manifestSubPath : manifestSubPaths) {
			Location manifestLoc = bundleLoc.resolve(manifestSubPath);
			if(manifestLoc.toFile().exists()) {
				return manifestLoc;
			}
		}
		
		return defaultManifestPath == null ? null : bundleLoc.resolve(defaultManifestPath);
	}
	
	/***
	 * Searches for a manifest file in any of the directories denoted by given path, starting in path. 
	 */
	public static BundlePath findBundleForPath(Location path) {
		if(path == null) {
			return null;
		}
		BundlePath bundlePath = create(path);
		if(bundlePath != null && bundlePath.hasBundleManifest()) {
			return bundlePath;
		}
		return findBundleForPath(path.getParent());
	}
	
}