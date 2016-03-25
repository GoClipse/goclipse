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
package melnorme.lang.ide.core.project_model;


import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Platform;

import melnorme.utilbox.misc.SimpleLogger;

public abstract class CommonLangBundleModel<T extends AbstractBundleInfo> extends ProjectBasedModel<T> {
	
	public static SimpleLogger log = new SimpleLogger(Platform.inDebugMode());
	
	public CommonLangBundleModel() {
		super();
	}
	
	@Override
	protected SimpleLogger getLog() {
		return log;
	}
	
	public T getBundleInfo(IProject project) {
		return super.getProjectInfo(project);
	}
	
	public T setBundleInfo(IProject project, T newProjectInfo) {
		return super.setProjectInfo(project, newProjectInfo);
	}
	
}