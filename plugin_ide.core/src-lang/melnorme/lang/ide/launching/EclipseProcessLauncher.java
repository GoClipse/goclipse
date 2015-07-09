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

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import melnorme.lang.ide.core.LangCore;
import melnorme.utilbox.misc.ArrayUtil;
import melnorme.utilbox.misc.StringUtil;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IProcess;

/**
 * Helper class to launch an Eclipse IProcess.
 */
public class EclipseProcessLauncher {
	
	protected final IPath workingDir;
	protected final IPath processFile;
	protected final String[] processArguments;
	protected final Map<String, String> environment;
	protected final boolean appendEnvironment;
	
	protected String processType;
	
	public EclipseProcessLauncher(IPath workingDir, IPath processFile, String[] processArgs,
			Map<String, String> environment, boolean appendEnvironment, String processType) {
		this.workingDir = workingDir;
		this.processFile = processFile;
		assertNotNull(processFile);
		this.processArguments = processArgs;
		this.environment = environment;
		this.appendEnvironment = appendEnvironment;
		
		this.processType = processType;
	}
	
	protected CoreException abort(String message, Throwable exception) throws CoreException {
		throw LangCore.createCoreException(message, exception);
	}
	
	protected CoreException fail(String messagePattern, Object... arguments) throws CoreException {
		throw abort(MessageFormat.format(messagePattern, arguments), null);
	}
	
	protected IProcess launchProcess(final ILaunch launch) throws CoreException {
		if(workingDir != null && !workingDir.toFile().exists()) {
			fail(LaunchMessages.errWorkingDirectoryDoesntExist, workingDir);
		}
		if(!processFile.toFile().exists()) {
			fail(LaunchMessages.errExecutableFileDoesntExist, processFile);
		}
		
		String[] cmdLine = getCommandLine();
		Process sp = newSystemProcess(cmdLine);
		
		return newEclipseProcessWithLabelUpdater(launch, cmdLine, sp);
	}
	
	/** Create the {@link java.lang.Process}. */
	protected Process newSystemProcess(String[] cmdLine) throws CoreException {
		
		File workingDirectory = workingDir.toFile();
		Process sp= null;
		try {
			
			ProcessBuilder processBuilder = new ProcessBuilder(cmdLine).directory(workingDirectory);
			setupEnvironment(processBuilder);
			
			sp = processBuilder.start();
		} catch (IOException e) {
			abort(LaunchMessages.errNewJavaProcessFailed, e);
		}
		return sp;
	}
	
	protected void setupEnvironment(ProcessBuilder processBuilder) throws CoreException {
		try {
			// This is a non-standard map that can throw some exceptions, see doc
			Map<String, String> env = processBuilder.environment();
			if(!appendEnvironment) {
				env.clear();
			}
			
			if(environment != null) {
				for (String key : environment.keySet()) {
					String value = environment.get(key);
					env.put(key, value);
				}
			}
		} catch (UnsupportedOperationException e) {
			abort(LaunchMessages.errFailedToSetupProcessEnvironment, e);
		} catch (IllegalArgumentException e) {
			abort(LaunchMessages.errFailedToSetupProcessEnvironment, e);
		}
	}
	
	protected final String[] getCommandLine() {
		List<String> items = new ArrayList<String>();
		prepareCommandLine(items);
		return ArrayUtil.createFrom(items, String.class);
	}
	
	protected void prepareCommandLine(List<String> commandLine) {
		commandLine.add(processFile.toOSString());
		commandLine.addAll(Arrays.asList(processArguments));
	}
	
	public IProcess newEclipseProcessWithLabelUpdater(ILaunch launch, String[] cmdLine, Process sp)
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
		String processFilePath = processFile.toOSString();
		return MessageFormat.format("{0} ({1})", processFilePath, timestampLabel);
	}
	
	protected static String renderCommandLineLabel(String[] commandLine) {
		return StringUtil.collToString(commandLine, "\n");
	}
	
}