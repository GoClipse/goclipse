/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.project_model;

import org.eclipse.core.runtime.Platform;

import melnorme.lang.ide.core.BundleInfo;
import melnorme.utilbox.misc.SimpleLogger;

public class LangBundleModel extends ProjectBasedModel<BundleInfo> {
	
	public static SimpleLogger log = new SimpleLogger(Platform.inDebugMode());
	
	public LangBundleModel() {
		super();
	}
	
	@Override
	protected SimpleLogger getLog() {
		return log;
	}
	
}