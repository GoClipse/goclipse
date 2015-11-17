/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.engine;

import melnorme.lang.ide.core.engine.StructureModelManager.StructureInfo;

public interface IStructureModelListener {
	
	/** 
	 * Indicates that the source file structure of the file for given key has changed.
	 * 
	 * This method runs under the scope of a {@link StructureInfo} lock, so listeners should respond quickly.
	 */
	void structureChanged(StructureInfo lockedStructureInfo);
	
}