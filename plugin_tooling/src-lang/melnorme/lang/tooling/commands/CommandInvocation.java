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
package melnorme.lang.tooling.commands;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.CoreUtil.areEqual;

import java.util.Map;
import java.util.Map.Entry;

import melnorme.lang.tooling.utils.ArgumentsParser;
import melnorme.lang.utils.EnvUtils;
import melnorme.lang.utils.ProcessUtils;
import melnorme.utilbox.collections.HashMap2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.collections.MapAccess;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.fields.validation.ValidatedValueSource;
import melnorme.utilbox.misc.HashcodeUtil;
import melnorme.utilbox.status.Severity;
import melnorme.utilbox.status.StatusException;

public class CommandInvocation {
	
	protected final String commandLine;
	protected final MapAccess<String, String> environmentVars; // Can be null
	protected final boolean appendEnvironment;
	
	public CommandInvocation(String commandLine) {
		this(commandLine, null, true);
	}
	
	public CommandInvocation(String commandLine, MapAccess<String, String> envVars, boolean appendEnv) {
		this.commandLine = assertNotNull(commandLine);
		this.environmentVars = envVars != null ? envVars : new HashMap2<>();
		this.appendEnvironment = appendEnv;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof CommandInvocation)) return false;
		
		CommandInvocation other = (CommandInvocation) obj;
		
		return 
			areEqual(commandLine, other.commandLine) &&
			areEqual(environmentVars, other.environmentVars) &&
			areEqual(appendEnvironment, other.appendEnvironment);
	}
	
	@Override
	public int hashCode() {
		return HashcodeUtil.combinedHashCode(commandLine, environmentVars);
	}
	
	public String getCommandLine() {
		return commandLine;
	}
	
	public Indexable<String> parseCommandLineArguments() {
		return ArgumentsParser.parse(this.commandLine);
	}
	
	public MapAccess<String, String> getEnvironmentVars() {
		return environmentVars;
	}
	
	public boolean isAppendEnvironment() {
		return appendEnvironment;
	}
	
	/* -----------------  ----------------- */
	
	public CommandInvocation getResolvedCommandInvocation(IVariablesResolver variablesResolver)
			throws StatusException, CommonException {
		String commandLine = evaluateCommandArguments(variablesResolver);
		
		HashMap2<String, String> resolvedEnvVars = new HashMap2<>();
		for (Entry<String, String> envVar : getEnvironmentVars()) {
			String newValue = variablesResolver.performStringSubstitution(envVar.getValue());
			resolvedEnvVars.put(envVar.getKey(), newValue);
		}
		
		return new CommandInvocation(commandLine, resolvedEnvVars, isAppendEnvironment());
	}
	
	public ProcessBuilder getProcessBuilder(IVariablesResolver variablesResolver) throws CommonException {
		return getResolvedCommandInvocation(variablesResolver).getProcessBuilder();
	}
	
	public ProcessBuilder getProcessBuilder() {
		Indexable<String> commandLine = parseCommandLineArguments();
		
		ProcessBuilder pb = ProcessUtils.createProcessBuilder(commandLine, null);
		Map<String, String> environment = pb.environment();
		
		if(!isAppendEnvironment()) {
			environment.clear();			
		}
		for (Entry<String, String> envVar : getEnvironmentVars()) {
			EnvUtils.putVarInEnvMap(environment, envVar.getKey(), envVar.getValue());
		}
		
		return pb;
	}
	
	public String evaluateCommandArguments(IVariablesResolver variablesResolver) throws StatusException {
		return getValidatedCommandArguments(variablesResolver).getValidatedValue();
	}
	
	protected ValidatedCommandArgumentsSource getValidatedCommandArguments(IVariablesResolver variablesResolver) {
		return new ValidatedCommandArgumentsSource(commandLine, variablesResolver);
	}
	
	public static class ValidatedCommandArgumentsSource implements ValidatedValueSource<String> {
		
		public static final String MSG_NO_COMMAND_SPECIFIED = "No command specified.";
		
		protected final IVariablesResolver variablesResolver;
		protected final String commandArguments;
		
		public ValidatedCommandArgumentsSource(String commandArguments, IVariablesResolver variablesResolver) {
			this.commandArguments = commandArguments;
			this.variablesResolver = variablesResolver;
		}
		
		@Override
		public String getValidatedValue() throws StatusException {
			try {
				return doGetValidatedValue(variablesResolver);
			} catch(CommonException e) {
				throw e.toStatusException(Severity.WARNING);
			}
		}
		
		public String doGetValidatedValue(IVariablesResolver variablesResolver) throws CommonException {
			if(commandArguments == null) {
				handleNoCommandLine();
			}
			
			String evaluatedCommandLine = variablesResolver.performStringSubstitution(commandArguments);
			
			if(evaluatedCommandLine.trim().isEmpty()) {
				handleNoCommandLine();
			}
			
			return evaluatedCommandLine;
		}
		
		protected void handleNoCommandLine() throws CommonException {
			throw new CommonException(MSG_NO_COMMAND_SPECIFIED);
		}
		
	}
	
}