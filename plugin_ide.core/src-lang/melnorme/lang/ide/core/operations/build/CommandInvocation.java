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
package melnorme.lang.ide.core.operations.build;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.util.Optional;

import org.eclipse.core.resources.IProject;
import org.eclipse.debug.core.DebugPlugin;

import melnorme.lang.ide.core.operations.ToolManager;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.core.CommonException;

public class CommandInvocation {
	
	protected final String commandInvocation;
	protected final ToolManager toolManager;
	protected final Optional<IProject> project; 
	
	public CommandInvocation(String commandInvocation, ToolManager toolManager, Optional<IProject> project) {
		this.commandInvocation = assertNotNull(commandInvocation);
		this.toolManager = assertNotNull(toolManager);
		this.project = assertNotNull(project);
	}
	
	public ToolManager getToolManager() {
		return toolManager;
	}
	
	public Indexable<String> getEffectiveCommandLine() throws CommonException {
		VariablesResolver variablesManager = getToolManager().getVariablesManager(project);
		
		String evaluatedCommandLine = variablesManager.performStringSubstitution(commandInvocation);
		
		String[] evaluatedArguments = DebugPlugin.parseArguments(evaluatedCommandLine);
		return new ArrayList2<>(evaluatedArguments);
	}
	
}