/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 * Contributors:
 *     ??? (DLTK) - initial API and implementation
 *     Bruno Medeiros - modifications, removed DLTK dependencies
 *******************************************************************************/
package melnorme.lang.ide.launching;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.io.IOException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IProcess;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.launch.LaunchMessages;
import melnorme.lang.ide.core.operations.build.VariablesResolver;
import melnorme.lang.tooling.commands.CommandInvocation;
import melnorme.lang.tooling.common.ops.CommonOperation;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.StringUtil;

/**
 * Helper class to launch an Eclipse IProcess.
 */
public class EclipseProcessLauncher {
	
	public final IProject project;
	public final CommonOperation buildOperation;
	public final Location programFileLocation;
	public final IPath workingDir;
	public final CommandInvocation unresolvedProgramInvocation;
	public String processType;
	
	public EclipseProcessLauncher(
			IProject project, 
			CommonOperation buildOperation, 
			Location programFileLocation, 
			IPath workingDir, 
			CommandInvocation unresolvedProgramInvocation,
			String processType
	) {
		this.project = assertNotNull(project);
		this.buildOperation = buildOperation; // can be null
		this.programFileLocation = assertNotNull(programFileLocation);
		this.workingDir = workingDir;
		this.unresolvedProgramInvocation = assertNotNull(unresolvedProgramInvocation);
		
		this.processType = processType;
	}
	
	protected CoreException abort(String message, Throwable exception) throws CoreException {
		throw LangCore.createCoreException(message, exception);
	}
	
	protected CoreException fail(String messagePattern, Object... arguments) throws CoreException {
		throw abort(MessageFormat.format(messagePattern, arguments), null);
	}
	
	protected IProcess launchProcess(final ILaunch launch) throws CoreException, CommonException {
		if(workingDir != null && !workingDir.toFile().exists()) {
			fail(LaunchMessages.errWorkingDirectoryDoesntExist, workingDir);
		}
		if(!programFileLocation.toFile().exists()) {
			fail(LaunchMessages.errExecutableFileDoesntExist, programFileLocation);
		}
		
		CommandInvocation programInvocation = unresolvedProgramInvocation.getResolvedCommandInvocation(
			new VariablesResolver(VariablesPlugin.getDefault().getStringVariableManager()));
		
		Indexable<String> cmdLine = programInvocation.parseCommandLineArguments();
		Process sp = newSystemProcess(programInvocation);
		
		return newEclipseProcessWithLabelUpdater(launch, cmdLine, sp);
	}
	
	protected Process newSystemProcess(CommandInvocation resolvedProgramInvocation) throws CoreException {
		
		ProcessBuilder processBuilder = resolvedProgramInvocation.getProcessBuilder();
		if(workingDir != null) {
			processBuilder.directory(workingDir.toFile());
		}
		
		Process sp= null;
		try {
			
			
			sp = processBuilder.start();
		} catch (IOException e) {
			abort(LaunchMessages.errNewJavaProcessFailed, e);
		}
		return sp;
	}
	
	public IProcess newEclipseProcessWithLabelUpdater(ILaunch launch, Indexable<String> cmdLine, Process sp)
			throws CoreException {
		
		final String cmdLineLabel = renderCommandLineLabel(cmdLine);
		final String processLabel = renderProcessLabel();
		
		IProcess process = newEclipseProcess(launch, sp, processLabel);
		process.setAttribute(IProcess.ATTR_CMDLINE, cmdLineLabel);
		
		return process;
	}
	
	protected IProcess newEclipseProcess(ILaunch launch, Process sp, String label) throws CoreException {
		// We ignore process factories, and create the class ourselves:
		return new RuntimeProcessExtension(launch, sp, label, getProcessAttributes());
		//return DebugPlugin.newProcess(launch, sp, label, getProcessAttributes());
	}
	
	protected Map<String, String> getProcessAttributes() {
		Map<String, String> map = new HashMap<String, String>();
		if(processType != null) {
			map.put(IProcess.ATTR_PROCESS_TYPE, processType);
		}
		return map;
	}
	
	protected static final DateFormat PROCESS_LABEL_DATE_FORMAT =
			DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
	
	protected String renderProcessLabel() {
		String timestampLabel = PROCESS_LABEL_DATE_FORMAT.format(new Date(System.currentTimeMillis()));
		String processFilePath = programFileLocation.toString();
		return MessageFormat.format("{0} ({1})", processFilePath, timestampLabel);
	}
	
	protected static String renderCommandLineLabel(Indexable<String> commandLine) {
		return StringUtil.collToString(commandLine, "\n");
	}
	
}