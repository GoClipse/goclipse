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
import static melnorme.utilbox.core.CoreUtil.areEqual;

import org.eclipse.debug.core.DebugPlugin;

import melnorme.lang.tooling.data.Severity;
import melnorme.lang.tooling.data.StatusException;
import melnorme.lang.tooling.data.validation.ValidatedValueSource;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.HashcodeUtil;

public class CommandInvocation {
	
	protected final String commandArguments;
	
	public CommandInvocation(String commandArguments) {
		this.commandArguments = assertNotNull(commandArguments);
	}
	
	public String getCommandArguments() {
		return commandArguments;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof CommandInvocation)) return false;
		
		CommandInvocation other = (CommandInvocation) obj;
		
		return areEqual(commandArguments, other.commandArguments);
	}
	
	@Override
	public int hashCode() {
		return HashcodeUtil.combinedHashCode(commandArguments);
	}
	
	public void validate(VariablesResolver variablesResolver) throws StatusException {
		getValidatedCommandArguments(variablesResolver).validate();
	}
	
	public ValidatedCommandArgumentsSource getValidatedCommandArguments(VariablesResolver variablesResolver) {
		return new ValidatedCommandArgumentsSource(commandArguments, variablesResolver);
	}
	
	public static class ValidatedCommandArgumentsSource implements ValidatedValueSource<Indexable<String>> {
		
		public static final String MSG_NO_COMMAND_SPECIFIED = "No command specified.";
		
		protected final VariablesResolver variablesResolver;
		protected final String commandArguments;
		
		public ValidatedCommandArgumentsSource(String commandArguments, VariablesResolver variablesResolver) {
			this.commandArguments = commandArguments;
			this.variablesResolver = variablesResolver;
		}
		
		@Override
		public Indexable<String> getValidatedValue() throws StatusException {
			try {
				return doGetValidatedValue(variablesResolver);
			} catch(CommonException e) {
				throw e.toStatusException(Severity.WARNING);
			}
		}
		
		public Indexable<String> doGetValidatedValue(VariablesResolver variablesResolver) throws CommonException {
			String evaluatedCommandLine = variablesResolver.performStringSubstitution(commandArguments);
			
			if(evaluatedCommandLine.trim().isEmpty()) {
				handleEmptyCommandLine();
			}
			
			String[] evaluatedArguments = DebugPlugin.parseArguments(evaluatedCommandLine);
			return new ArrayList2<>(evaluatedArguments);
		}
		
		protected void handleEmptyCommandLine() throws CommonException {
			throw new CommonException(MSG_NO_COMMAND_SPECIFIED);
		}
		
	}
	
}