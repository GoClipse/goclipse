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

import melnorme.utilbox.misc.Location;


public interface ISourceFileStructure extends IStructureElementContainer {
	
	/** @return the location of this SourceFile. Can be null */
	Location getLocation2();
	
	@Override
	public String getModuleName();
	
}