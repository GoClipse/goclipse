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

import org.eclipse.debug.core.DebugPlugin;

import melnorme.lang.tooling.data.IStatusMessage;
import melnorme.lang.tooling.data.IValidationSource;
import melnorme.lang.tooling.data.Severity;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.core.CommonException;

public class CommandInvocation implements IValidationSource {
	
	protected final String commandArguments;
	protected final VariablesResolver variablesResolver;
	
	public CommandInvocation(String commandArguments, VariablesResolver variablesResolver) {
		this.commandArguments = assertNotNull(commandArguments);
		this.variablesResolver = assertNotNull(variablesResolver);
	}
	
	public Indexable<String> getEffectiveCommandLine() throws CommonException {
		String evaluatedCommandLine = variablesResolver.performStringSubstitution(commandArguments);
		
		if(evaluatedCommandLine.trim().isEmpty()) {
			handleEmptyCommandLine();
		}
		
		String[] evaluatedArguments = DebugPlugin.parseArguments(evaluatedCommandLine);
		return new ArrayList2<>(evaluatedArguments);
	}
	
	protected void handleEmptyCommandLine() throws CommonException {
		throw new CommonException("No command specified.");
	}
	
	@Override
	public IStatusMessage getValidationStatus() {
		try {
			getEffectiveCommandLine();
		} catch(CommonException e) {
			return e.toStatusException(Severity.WARNING);
		}
		return null;
	}
	
}