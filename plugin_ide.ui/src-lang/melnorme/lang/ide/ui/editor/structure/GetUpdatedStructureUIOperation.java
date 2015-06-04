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

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.engine.StructureModelManager;
import melnorme.lang.ide.core.engine.StructureModelManager.StructureInfo;
import melnorme.lang.ide.ui.actions.CalculateValueUIOperation;
import melnorme.lang.tooling.structure.SourceFileStructure;
import melnorme.lang.tooling.structure.StructureElement;
import melnorme.utilbox.concurrency.OperationCancellation;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public class GetUpdatedStructureUIOperation extends CalculateValueUIOperation<SourceFileStructure> {
	
	protected final StructureModelManager modelManager;
	protected final StructureInfo structureInfo;
	
	public GetUpdatedStructureUIOperation(AbstractLangStructureEditor editor) {
		this(LangCore.getEngineClient(), editor.editorStructureInfo);
	}
	
	public GetUpdatedStructureUIOperation(StructureModelManager modelManager, StructureInfo structureInfo) {
		super("Awaiting Structure Calculation");
		this.modelManager = modelManager;
		this.structureInfo = structureInfo;
	}
	
	@Override
	protected String getTaskName() {
		return operationName;
	}
	
	@Override
	protected void prepareOperation() throws CoreException {
		if(structureInfo == null) {
			throw LangCore.createCoreException("StructureInfo not available", null);
		}
	}
	
	@Override
	protected void performLongRunningComputation() throws OperationCancellation, CoreException {
		if(!structureInfo.isStale()) {
			// No need for background computation
			resultValue = structureInfo.getStoredStructure();
		} else {
			super.performLongRunningComputation();
		}
	}
	
	@Override
	protected SourceFileStructure calculateValue(IProgressMonitor pm) throws OperationCancellation {
		return structureInfo.getUpdatedStructure(pm);
	}
	
	@Override
	protected void handleNonCanceledNullResult() throws CoreException {
		throw LangCore.createCoreException(
			"Could not retrieve source file structure for: " + structureInfo.getKey(), null);
	}
	
	/* ----------------- util ----------------- */
	
	public static StructureElement getUpdatedStructureElementAt(AbstractLangStructureEditor editor, int offset) {
		GetUpdatedStructureUIOperation op = new GetUpdatedStructureUIOperation(editor);
		SourceFileStructure sourceFileStructure = op.executeAndGetHandledResult();
		
		if(sourceFileStructure == null) {
			return null; // Note, possible error result has already been handled and reported to the user.
		}
		return sourceFileStructure.getStructureElementAt(offset);
	}
	
}