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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.eclipse.core.resources.IProject;

public class MessageEventInfo {
	
	public final OperationInfo opInfo;
	public final String operationMessage;
	
	public MessageEventInfo(OperationInfo opInfo, String operationMessage) {
		this.opInfo = opInfo;
		this.operationMessage = operationMessage;
		assertTrue(opInfo != null && opInfo.isStarted());
	}
	
	public IProject getProject() {
		return opInfo.getProject();
	}
	
}