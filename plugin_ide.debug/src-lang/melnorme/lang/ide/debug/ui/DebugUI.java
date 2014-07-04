/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.debug.ui;

import melnorme.lang.ide.ui.LangEditorTextHoversRegistry;

import org.eclipse.cdt.dsf.gdb.launching.GdbLaunch;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.osgi.framework.BundleContext;


public class DebugUI extends Plugin {
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		// Register debug hover
		LangEditorTextHoversRegistry.addTextHoverSpecToBeggining(DelegatingDebugTextHover.class);
	}
	
	@Override
	public void stop(BundleContext context) throws Exception {
		LangEditorTextHoversRegistry.removeTextHoverSpec(DelegatingDebugTextHover.class);
		
		disposeAdapterSets();
		super.stop(context);
	}
	
	/**
	 * Dispose adapter sets for all launches.
	 */
	private void disposeAdapterSets() {
	    DebugPlugin debugPlugin = DebugPlugin.getDefault();
	    // This can happen in certain cases where this DebugUI plugin was activated programatically,
	    // but Eclipse's DebugPlugin was never activated because it was never used.
	    if(debugPlugin == null) return;
	    
		for (ILaunch launch : debugPlugin.getLaunchManager().getLaunches()) {
	        if (launch instanceof GdbLaunch) {
	            GdbAdapterFactory.disposeAdapterSet(launch);
	        }
	    }
	}
	
}