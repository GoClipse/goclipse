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
package melnorme.lang.ide.ui.text;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.io.IOException;

import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.tooling.oracle.GoDocParser;
import com.googlecode.goclipse.tooling.oracle.GoOperationContext;
import com.googlecode.goclipse.ui.actions.GoFindDefinitionOperation;

import melnorme.lang.ide.ui.editor.hover.AbstractDocDisplayInfoSupplier;
import melnorme.lang.ide.ui.utils.operations.ComputeValueUIOperation;
import melnorme.lang.tooling.LANG_SPECIFIC;
import melnorme.lang.tooling.common.ISourceBuffer;
import melnorme.lang.tooling.common.SourceLineColumnRange;
import melnorme.lang.tooling.parser.SourceLinesInfo;
import melnorme.lang.tooling.toolchain.ops.SourceLocation;
import melnorme.lang.tooling.toolchain.ops.ToolResponse;
import melnorme.lang.tooling.toolchain.ops.ToolResponse.StatusValidation;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.FileUtil;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.StringUtil;

@LANG_SPECIFIC
public class DocDisplayInfoSupplier extends AbstractDocDisplayInfoSupplier {
	
	public DocDisplayInfoSupplier(ISourceBuffer sourceBuffer, int offset) {
		super(sourceBuffer, offset);
	}
	
	@Override
	protected ComputeValueUIOperation<ToolResponse<String>> getOpenDocumentationOperation2(
			ISourceBuffer sourceBuffer, int offset) {
		GoOperationContext goOpContext = GoProjectEnvironment.getGoOperationContext(sourceBuffer, offset);
		return new GoFindDocUIOperation(new GoFindDefinitionOperation(goOpContext));
	}
	
	public static class GoFindDocUIOperation extends ComputeValueUIOperation<ToolResponse<String>> {
		
		protected final GoFindDefinitionOperation findDefOp;
		
		public GoFindDocUIOperation(GoFindDefinitionOperation findDefOp) {
			this.findDefOp = assertNotNull(findDefOp);
		}
		
		@Override
		public ToolResponse<String> call() throws CommonException, OperationCancellation {
			
			ToolResponse<SourceLocation> findDefOpResult = findDefOp.call();
			
			SourceLocation findDefResult;
			try {
				findDefResult = findDefOpResult.getValidResult();
			} catch(StatusValidation e) {
				return new ToolResponse<>(null, e);
			}
			
			/* FIXME: review this code */
			String fileContents = readFileContents(findDefResult.getFileLocation());
			SourceLinesInfo linesInfo = new SourceLinesInfo(fileContents);
			
			SourceLineColumnRange sourceLCRange = findDefResult.getSourceRange();
			int offset = linesInfo.getOffsetForLine(sourceLCRange.getValidLineIndex()) + sourceLCRange.getValidColumnIndex();
			
			String goDoc = new GoDocParser().parseDocForDefinitionAt(fileContents, offset);
			
			return new ToolResponse<>(goDoc, findDefOpResult.getStatusMessage());
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
	
}