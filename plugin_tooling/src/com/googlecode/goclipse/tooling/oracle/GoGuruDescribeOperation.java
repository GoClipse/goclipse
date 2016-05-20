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


import java.text.MessageFormat;

import com.googlecode.goclipse.tooling.GoPackageName;
import com.googlecode.goclipse.tooling.env.GoEnvironment;

import melnorme.lang.tooling.toolchain.ops.ToolOutputParseHelper;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

public class GoGuruDescribeOperation implements ToolOutputParseHelper {
	
	protected final String guruPath;
	
	public GoGuruDescribeOperation(String guruPath) {
		this.guruPath = guruPath;
	}
	
	public ProcessBuilder createProcessBuilder(GoEnvironment goEnv, Location fileLoc, int offset) 
			throws CommonException {
		GoPackageName goPackage = goEnv.findGoPackageForSourceFile(fileLoc);
		if(goPackage == null) {
			throw new CommonException(MessageFormat.format(
				"Could not determine Go package for Go file ({0}), file not in the Go environment.", fileLoc), 
				null);
		}
		
		ArrayList2<String> commandLine = new ArrayList2<>(
			guruPath,
			"-json",
			"describe",
			fileLoc.toPathString() + ":#" + offset + ",#" + offset
		);
		
		return goEnv.createProcessBuilder(commandLine, null, true);
	}
	
}