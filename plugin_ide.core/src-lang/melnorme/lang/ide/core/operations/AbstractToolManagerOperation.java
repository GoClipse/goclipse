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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.core.resources.IProject;

import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.IToolOperationMonitor;
import melnorme.lang.ide.core.operations.ToolManager.RunToolTask;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.tooling.commands.CommandInvocation;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.lang.tooling.common.ops.Operation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

public abstract class AbstractToolManagerOperation implements Operation {
	
	protected final ToolManager toolManager;
	protected final IProject project;
	
	public AbstractToolManagerOperation(ToolManager toolManager, IProject project) {
		this.toolManager = assertNotNull(toolManager);
		this.project = project;
	}
	
	protected ToolManager getToolManager() {
		return toolManager;
	}
	
	public IProject getProject() {
		return project;
	}
	
	protected Location getProjectLocation() throws CommonException {
		return ResourceUtils.getProjectLocation2(project);
	}
	
	/**
	 * Note: buildCommand is used only for UI display purposes
	 */
	public RunToolTask getRunToolTask(
		IToolOperationMonitor opMonitor, 
		ProcessBuilder pb, 
		String buildTargetName, CommandInvocation buildCommand, 
		IOperationMonitor om
	) {
		return getToolManager().newRunProcessTask(opMonitor, pb, buildTargetName, buildCommand, om);
	}
	
}