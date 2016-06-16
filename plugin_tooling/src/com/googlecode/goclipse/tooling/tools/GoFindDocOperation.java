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
package com.googlecode.goclipse.tooling.tools;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.io.IOException;

import com.googlecode.goclipse.tooling.oracle.GoDocParser;

import melnorme.lang.tooling.common.SourceLineColumnRange;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.lang.tooling.parser.SourceLinesInfo;
import melnorme.lang.tooling.toolchain.ops.AbstractToolOperation;
import melnorme.lang.tooling.toolchain.ops.OperationSoftFailure;
import melnorme.lang.tooling.toolchain.ops.SourceLocation;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.FileUtil;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.StringUtil;

public class GoFindDocOperation implements AbstractToolOperation<String> {
	
	protected final GoFindDefinitionOperation findDefOp;
	
	public GoFindDocOperation(GoFindDefinitionOperation findDefOp) {
		this.findDefOp = assertNotNull(findDefOp);
	}
	
	@Override
	public String executeToolOperation(IOperationMonitor om) throws CommonException, OperationCancellation {
		
		SourceLocation findDefResult;
		try {
			findDefResult = findDefOp.getValidResult(om);
		} catch(OperationSoftFailure e) {
			return null; // No documentation will be available
		}
		
		/* FIXME: review this code */
		String fileContents = readFileContents(findDefResult.getFileLocation());
		SourceLinesInfo linesInfo = new SourceLinesInfo(fileContents);
		
		SourceLineColumnRange sourceLCRange = findDefResult.getSourceRange();
		int offset = linesInfo.getOffsetForLine(sourceLCRange.getValidLineIndex()) + sourceLCRange.getValidColumnIndex();
		
		return new GoDocParser().parseDocForDefinitionAt(fileContents, offset);
	}
	
	public static String readFileContents(Location location) throws CommonException {
		try {
			// TODO: we might need to do auto-detect of encoding.
			return FileUtil.readStringFromFile(location.toPath(), StringUtil.UTF8);
		} catch(IOException e) {
			throw new CommonException(e.getMessage(), e.getCause());
		}
	}

}