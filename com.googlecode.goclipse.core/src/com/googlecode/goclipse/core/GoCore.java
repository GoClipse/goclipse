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
package com.googlecode.goclipse.core;


import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.framework.BundleContext;

import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.builder.GoBuilder;

import melnorme.lang.ide.core.LangCore;

public class GoCore extends LangCore {
	
	public static final String PLUGIN_ID = "com.googlecode.goclipse.core";
	
	public static final String CONTENT_ASSIST_EXTENSION_ID = "com.googlecode.goclipse.contentassistprocessor";

	
	@Override
	protected void doCustomStart(BundleContext context) {
		// Force construction of singleton
		Environment.INSTANCE.toString();
		
		GoBuilder.checkForCompilerUpdates(true);
	}
	
	@Override
	protected void doCustomStop(BundleContext context) {
	}
	
	public static GoCore getDefault() {
		return (GoCore) getInstance();
	}
	
	private ScopedPreferenceStore preferenceStore;
	
	public IPreferenceStore getPreferenceStore() {
		// Create the preference store lazily.
		if (preferenceStore == null) {
			preferenceStore = new ScopedPreferenceStore(InstanceScope.INSTANCE, getBundle().getSymbolicName());
			
		}
		return preferenceStore;
	}
	
}