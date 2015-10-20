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
package melnorme.lang.ide.ui.actions;

import org.eclipse.jface.action.Action;

public class RunUIOperationAction extends Action {
	
	protected final AbstractUIOperation operation;
	
	public RunUIOperationAction(AbstractUIOperation operation) {
		this.operation = operation;
		
		setText(operation.getOperationName());
	}
	
	@Override
	public void run() {
		operation.executeAndHandle();
	}
	
}