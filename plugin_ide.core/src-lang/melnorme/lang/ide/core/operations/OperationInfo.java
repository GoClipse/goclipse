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
package melnorme.lang.ide.core.operations;

import static melnorme.utilbox.core.CoreUtil.assertCast;

import org.eclipse.core.resources.IProject;

import melnorme.utilbox.collections.HashMap2;

public class OperationInfo {
	
	public final IProject project; // can be null
	public final HashMap2<String, Object> properties = new HashMap2<>();
	
	protected boolean started = false;
	
	protected OperationInfo(IProject project) {
		this.project = project;
	}
	
	public IProject getProject() {
		return project;
	}
	
	public boolean isStarted() {
		return started;
	}
	
	public void setStarted(boolean started) {
		this.started = started;
	}
	
	public void putProperty(String key, Object value) {
		properties.put(key, value);
	}
	
	public Object getProperty(String key) {
		return properties.get(key);
	}
	
	public <T> T getProperty(String key, Class<T> klass) {
		return assertCast(properties.get(key), klass);
	}
	
}