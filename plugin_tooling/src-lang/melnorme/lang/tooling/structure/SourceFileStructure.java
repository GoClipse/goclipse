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
import melnorme.utilbox.misc.Location;

public class SourceFileStructure implements ISourceFileStructure {
	
	protected final Location location;
	protected final Iterable<IStructureElement> children;
	
	public SourceFileStructure(Location location, Iterable<IStructureElement> children) {
		this.location = assertNotNull(location);
		this.children = assertNotNull(children);
	}
	
	@Override
	public Location getLocation() {
		return location;
	}
	
	@Override
	public Iterable<IStructureElement> getChildren() {
		return children;
	}
	
	@Override
	public String getModuleName() {
		return location.getPath().getFileName().toString();
	}
	
}