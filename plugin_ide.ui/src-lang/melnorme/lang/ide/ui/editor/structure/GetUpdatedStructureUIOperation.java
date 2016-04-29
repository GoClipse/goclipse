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
import melnorme.lang.ide.core.engine.SourceModelManager;
import melnorme.lang.ide.core.engine.SourceModelManager.StructureInfo;
import melnorme.lang.ide.ui.utils.operations.CalculateValueUIOperation;
import melnorme.lang.tooling.structure.SourceFileStructure;
import melnorme.lang.tooling.structure.StructureElement;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public class GetUpdatedStructureUIOperation extends CalculateValueUIOperation<SourceFileStructure> {
	
	protected final SourceModelManager modelManager;
	protected final AbstractLangStructureEditor editor;
	
	public GetUpdatedStructureUIOperation(AbstractLangStructureEditor editor) {
		this(LangCore.getSourceModelManager(), editor);
	}
	
	public GetUpdatedStructureUIOperation(SourceModelManager modelManager, AbstractLangStructureEditor editor) {
		super("Awaiting Structure Calculation");
		this.modelManager = assertNotNull(modelManager);
		this.editor = assertNotNull(editor);
	}
	
	@Override
	protected String getTaskName() {
		return operationName;
	}
	
	@Override
	protected void prepareOperation() throws CoreException {
		if(editor.modelRegistration == null) {
			throw LangCore.createCoreException("Editor not connected to a structure model.", null);
		}
		structureInfo = editor.modelRegistration.structureInfo;
	}
	
	protected StructureInfo structureInfo;
	
	@Override
	protected boolean isBackgroundComputationNecessary() throws CommonException {
		if(structureInfo.isStale()) {
			return true;
		} else {
			result = structureInfo.getStoredData();
			return false;
		}
	}
	
	@Override
	protected SourceFileStructure doBackgroundValueComputation(IProgressMonitor pm) throws OperationCancellation {
		return structureInfo.awaitUpdatedData(pm);
	}
	
	@Override
	protected void handleComputationResult() throws CommonException {
		if(result == null) {
			throw new CommonException("Could not retrieve source file structure for: " + structureInfo.getKey2());
		}
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