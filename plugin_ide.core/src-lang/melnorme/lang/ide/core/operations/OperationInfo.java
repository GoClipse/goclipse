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
package melnorme.lang.ide.core.operations;

import org.eclipse.core.resources.IProject;

import melnorme.utilbox.collections.HashMap2;

public class OperationInfo {
	
	protected final HashMap2<String, Object> properties = new HashMap2<>();
	
	public final IProject project; // can be null
	public final boolean clearConsole;
	public final String operationMessage;
	
	public OperationInfo(IProject project, boolean clearConsole, String operationMessage) {
		this.project = project;
		this.clearConsole = clearConsole;
		this.operationMessage = operationMessage;
	}
	
	public IProject getProject() {
		return project;
	}
	
	public void putProperty(String key, Object value) {
		properties.put(key, value);
	}
	
	public Object getProperty(String key) {
		return properties.get(key);
	}
	
	public OperationInfo createSubOperation(IProject project, boolean clearConsole, String operationMessage) {
		OperationInfo newOpInfo = new OperationInfo(project, clearConsole, operationMessage);
		newOpInfo.properties.putAll(properties); // Copy the properties
		return newOpInfo;
	}
	
}