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

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import melnorme.lang.ide.ui.LangEditorTextHoversRegistry;


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
		
		super.stop(context);
	}
	
}