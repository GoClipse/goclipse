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

import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.debug.core.DebugPlugin;

import melnorme.lang.tooling.data.validation.ValidatedValueSource;
import melnorme.lang.utils.ProcessUtils;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.HashMap2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.collections.MapAccess;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.HashcodeUtil;
import melnorme.utilbox.status.Severity;
import melnorme.utilbox.status.StatusException;

public class CommandInvocation {
	
	protected final String commandArguments;
	protected final MapAccess<String, String> environmentVars; // Can be null
	protected final boolean appendEnvironment;
	
	public CommandInvocation(String commandArguments) {
		this(commandArguments, null, true);
	}
	
	public CommandInvocation(String commandArguments, MapAccess<String, String> envVars, boolean appendEnv) {
		this.commandArguments = assertNotNull(commandArguments);
		this.environmentVars = envVars != null ? envVars : new HashMap2<>();
		this.appendEnvironment = appendEnv;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof CommandInvocation)) return false;
		
		CommandInvocation other = (CommandInvocation) obj;
		
		return 
			areEqual(commandArguments, other.commandArguments) &&
			areEqual(environmentVars, other.environmentVars) &&
			areEqual(appendEnvironment, other.appendEnvironment);
	}
	
	@Override
	public int hashCode() {
		return HashcodeUtil.combinedHashCode(commandArguments, environmentVars);
	}
	
	public String getCommandArguments() {
		return commandArguments;
	}
	
	public MapAccess<String, String> getEnvironmentVars() {
		return environmentVars;
	}
	
	public boolean isAppendEnvironment() {
		return appendEnvironment;
	}
	
	/* -----------------  ----------------- */
	
	public ProcessBuilder getProcessBuilder(VariablesResolver variablesResolver) throws StatusException {
		Indexable<String> CommandLine = validateCommandArguments(variablesResolver);
		
		ProcessBuilder pb = ProcessUtils.createProcessBuilder(CommandLine, null);
		
		setupEnvironment(pb.environment());
		
		return pb;
	}
	
	public void setupEnvironment(Map<String, String> environment) {
		if(!isAppendEnvironment()) {
			environment.clear();			
		}
		for (Entry<String, String> envVar : getEnvironmentVars()) {
			environment.put(envVar.getKey(), envVar.getValue());
		}
	}
	
	public void validate(VariablesResolver variablesResolver) throws StatusException {
		validateCommandArguments(variablesResolver);
	}
	
	public Indexable<String> validateCommandArguments(VariablesResolver variablesResolver) throws StatusException {
		return getValidatedCommandArguments(variablesResolver).getValidatedValue();
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
			if(commandArguments == null) {
				handleNoCommandLine();
			}
			
			String evaluatedCommandLine = variablesResolver.performStringSubstitution(commandArguments);
			
			if(evaluatedCommandLine.trim().isEmpty()) {
				handleNoCommandLine();
			}
			
			String[] evaluatedArguments = DebugPlugin.parseArguments(evaluatedCommandLine);
			return new ArrayList2<>(evaluatedArguments);
		}
		
		protected void handleNoCommandLine() throws CommonException {
			throw new CommonException(MSG_NO_COMMAND_SPECIFIED);
		}
		
	}
	
}