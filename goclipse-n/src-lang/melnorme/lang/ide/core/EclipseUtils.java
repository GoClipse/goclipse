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
package melnorme.lang.ide.core;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

public class EclipseUtils {

	public static void startOtherPlugin(String pluginId) {
		try {
			Bundle debugPlugin = Platform.getBundle(pluginId);
			if(debugPlugin != null) {
				debugPlugin.start(Bundle.START_TRANSIENT);
			}
		} catch (BundleException e) {
			LangCore.log(e);
		}
	}
	
}