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

import melnorme.lang.ide.core.LangCore_Actual;

public abstract class StructureModelManager_WithActualTest extends StructureModelTest {
	
	@Override
	protected void initializeTestsEngineClient() {
		manager = LangCore_Actual.createSourceModelManager();
	}
	
	@Override
	public void checkCounts() {
		// Do nothing.
	}
	
}