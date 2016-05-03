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
package melnorme.lang.ide.ui.operations;

import org.eclipse.core.resources.IProject;

import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.StartOperationOptions;
import melnorme.lang.ide.core.operations.RunToolOperation;
import melnorme.lang.ide.ui.utils.operations.UIOperation;
import melnorme.utilbox.collections.Indexable;

public class RunToolUIOperation extends UIOperation {
			
	public RunToolUIOperation(String operationName, IProject project, Indexable<String> commands,
			StartOperationOptions opViewOptions) {
		super(operationName, new RunToolOperation(project, commands, opViewOptions));
	}
	
	public static class RunSDKUIToolOperation extends UIOperation {
		
		public RunSDKUIToolOperation(String operationName, IProject project, Indexable<String> commands) {
			super(operationName, new RunToolOperation.RunSDKToolOperation(project, commands)); 
		}
		
	}
	
}