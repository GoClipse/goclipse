/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.tooling.gocode;

import java.util.regex.Pattern;

import melnorme.lang.tooling.ops.AbstractToolOperation;
import melnorme.lang.tooling.ops.IOperationHelper;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

import com.googlecode.goclipse.tooling.env.GoEnvironment;


public class GocodeCompletionOperation extends AbstractToolOperation {
	
	public static final boolean USE_TCP = true;
	
	protected final GoEnvironment goEnvironment;
	protected final String gocodePath;
	
	public GocodeCompletionOperation(IOperationHelper opHelper, GoEnvironment goEnvironment, String gocodePath) {
		super(opHelper);
		this.goEnvironment = goEnvironment;
		this.gocodePath = gocodePath;
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
		
		runToolProcess(pb, null);
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
		
		ExternalProcessResult processResult = runToolProcess(pb, bufferText);
		
		if(processResult.exitValue != 0) {
			throw new CommonException("Error, gocode returned non-zero status: " + processResult.exitValue);
		}
		
		return processResult;
	}
	
	// TODO: move the code that process gocode result to here
	
	public static final Pattern LINE_SPLITTER = Pattern.compile("\n|(\r\n)|\r");

}