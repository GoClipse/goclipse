/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.tooling.oracle;


import com.googlecode.goclipse.tooling.env.GoEnvironment;

import melnorme.lang.tooling.toolchain.ops.ToolOutputParseHelper;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

public class GuruDescribeOperation implements ToolOutputParseHelper {
	
	protected final String guruPath;
	
	public GuruDescribeOperation(String guruPath) {
		this.guruPath = guruPath;
	}
	
	public ProcessBuilder createProcessBuilder(GoEnvironment goEnv, Location fileLoc, int offset) 
			throws CommonException {
		
		ArrayList2<String> commandLine = ArrayList2.create(
			guruPath,
			"-format",
			"json",
			"describe",
			fileLoc.toPathString() + ":#" + offset + ",#" + offset
		);
		
		return goEnv.createProcessBuilder(commandLine, null, true);
	}
	
}
