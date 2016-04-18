/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.tooling.bundle;

import java.text.MessageFormat;

public interface BuildConfigMessages {
	
	public static String BuildConfig_NotFound(String buildConfigName) {
		return MessageFormat.format("Build configuration `{0}` not found.", buildConfigName);
	}
	
}