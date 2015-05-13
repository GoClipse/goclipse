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
package melnorme.lang.ide.ui.editor.structure;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.util.concurrent.TimeUnit;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.actions.CalculateValueUIOperation;
import melnorme.lang.ide.ui.editor.EditorUtils;
import melnorme.lang.ide.ui.editor.structure.StructureModelManager.StructureInfo;
import melnorme.lang.tooling.structure.StructureElement;
import melnorme.lang.tooling.structure.SourceFileStructure;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.misc.Location;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.EditorPart;

public class GetUpdatedStructureUIOperation extends CalculateValueUIOperation<SourceFileStructure> {
	
	protected final IEditorInput editorInput;
	
	protected Location inputLocation;
	protected StructureInfo structureInfo;
	
	public GetUpdatedStructureUIOperation(EditorPart editor) {
		this(assertNotNull(editor).getEditorInput());
	}
	
	public GetUpdatedStructureUIOperation(IEditorInput editorInput) {
		super("Awaiting Structure Calculation");
		this.editorInput = editorInput;
	}
	
	@Override
	protected String getTaskName() {
		return operationName;
	}
	
	@Override
	protected void prepareOperation() throws CoreException {
		inputLocation = EditorUtils.getLocationFromEditorInput(editorInput);
		structureInfo = StructureModelManager.getDefault().getStructureInfo(inputLocation);
	}
	
	@Override
	protected void performLongRunningComputation() throws OperationCancellation, CoreException {
		if(!structureInfo.isStale()) {
			// No need for background computation
			resultValue = structureInfo.getStructure();
		} else {
			super.performLongRunningComputation();
		}
	}
	
	@Override
	protected SourceFileStructure calculateValue(IProgressMonitor pm) throws OperationCancellation {
		while(true) {
			if(pm.isCanceled()) {
				throw new OperationCancellation();
			}
			
			try {
				return structureInfo.getUpdatedStructure(100, TimeUnit.MILLISECONDS);
			} catch(InterruptedException e) {
				continue;
			}
		}
	}
	
	@Override
	protected void handleNonCanceledNullResult() throws CoreException {
		throw LangCore.createCoreException(
			"Could not retrieve source file structure for: " + inputLocation, null);
	}
	
	/* ----------------- util ----------------- */
	
	public static StructureElement getUpdatedStructureElementAt(EditorPart editor, int offset) {
		GetUpdatedStructureUIOperation op = new GetUpdatedStructureUIOperation(editor);
		SourceFileStructure sourceFileStructure = op.executeAndGetHandledResult();
		
		if(sourceFileStructure == null) {
			return null; // Note, possible error result has already been handled and reported to the user.
		}
		return sourceFileStructure.getStructureElementAt(offset);
	}
	
}