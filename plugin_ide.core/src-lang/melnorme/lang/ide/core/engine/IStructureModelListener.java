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

import melnorme.lang.ide.core.engine.SourceModelManager.StructureInfo;
import melnorme.lang.utils.concurrency.ConcurrentlyDerivedData.IDataChangedListener;

public interface IStructureModelListener extends IDataChangedListener<StructureInfo> {
	
}