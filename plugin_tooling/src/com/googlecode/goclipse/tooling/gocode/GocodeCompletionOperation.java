/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.tooling.gocode;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.util.regex.Pattern;

import com.googlecode.goclipse.tooling.env.GoEnvironment;

import melnorme.lang.tooling.toolchain.ops.IProcessRunner;
import melnorme.lang.tooling.toolchain.ops.ToolOutputParseHelper;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.concurrency.ICancelMonitor;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;


public class GocodeCompletionOperation implements ToolOutputParseHelper {
	
	public static final boolean USE_TCP = true;
	
	protected final IProcessRunner toolRunner;
	protected final GoEnvironment goEnvironment;
	protected final String gocodePath;
	protected final ICancelMonitor cm;
	
	public GocodeCompletionOperation(IProcessRunner toolRunner, GoEnvironment goEnvironment, String gocodePath,
			ICancelMonitor cm) {
		this.toolRunner = assertNotNull(toolRunner);
		this.goEnvironment = assertNotNull(goEnvironment);
		this.gocodePath = assertNotNull(gocodePath);
		this.cm = assertNotNull(cm);
	}
	
	protected void setLibPathForEnvironment() throws CommonException, OperationCancellation {
		
		ArrayList2<String> arguments = new ArrayList2<>(gocodePath);
		
		if (USE_TCP) {
			arguments.add("-sock=tcp");
		}
		arguments.add("set");
		arguments.add("lib-path");
		arguments.add(goEnvironment.getGoPathString());
		
		ProcessBuilder pb = goEnvironment.createProcessBuilder(arguments, null, true);
		
		toolRunner.runProcess(pb, null, cm);
	}
	
	public ExternalProcessResult execute(String filePath, String bufferText, int offset) 
			throws CommonException, OperationCancellation {
		
		setLibPathForEnvironment();
		
		ArrayList2<String> arguments = new ArrayList2<String>(gocodePath);
		
		if (USE_TCP) {
			arguments.add("-sock=tcp");
		}
		arguments.add("-f=csv");
		arguments.add("autocomplete");
		arguments.add(filePath);
		arguments.add("c" + offset);
		
		ProcessBuilder pb = goEnvironment.createProcessBuilder(arguments, null, true);
		
		return toolRunner.runProcess(pb, bufferText, cm);
	}
	
	// TODO: move the code that process gocode result to here
	
	public static final Pattern LINE_SPLITTER = Pattern.compile("\n|(\r\n)|\r");

}