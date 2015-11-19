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
package melnorme.lang.ide.core;

public class LangCore2 extends LangCore_Actual {
	
	public LangCore2() {
	}
	
	protected void shutdown() {
		buildManager.dispose();
		bundleManager.shutdownManager();
		sourceModelManager.dispose();
		toolManager.shutdownNow();
	}
	
	/** 
	 * Start core agents, and do other initizaliation after UI is started.
	 * @param langCore TODO
	 */
	public void startAgentsAfterUIStart(LangCore langCore) {
		bundleManager.startManager();
	}
	
}