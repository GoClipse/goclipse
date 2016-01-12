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
package melnorme.lang.tooling;

import melnorme.lang.tooling.bundle.AbstractBundlePath;
import melnorme.utilbox.misc.Location;

public class BundlePath extends AbstractBundlePath {
	
	public BundlePath(Location location) {
		super(location);
	}
	
	@Override
	public Location getManifestLocation(boolean provideDefault) {
		return getLocation().resolve_fromValid("lang.bundle"); // TODO: Lang
	}
	
}