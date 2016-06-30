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

import java.util.Optional;

import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.texteditor.ITextEditor;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.engine.SourceModelManager;
import melnorme.lang.ide.core.engine.SourceModelManager.StructureInfo;
import melnorme.lang.ide.core.engine.SourceModelManager.StructureModelRegistration;
import melnorme.lang.ide.core.text.ISourceBufferExt;
import melnorme.lang.ide.ui.editor.EditorSourceBuffer;
import melnorme.lang.ide.ui.utils.operations.CalculateValueUIOperation;
import melnorme.lang.tooling.LocationKey;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.lang.tooling.structure.SourceFileStructure;
import melnorme.lang.tooling.structure.StructureElement;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.core.fntypes.Result;
import melnorme.utilbox.misc.Location;

public class GetUpdatedStructureUIOperation extends CalculateValueUIOperation<SourceFileStructure> {
	
	protected final SourceModelManager modelManager;
	protected final LocationKey locationKey;
	protected final IDocument document;
	
	public GetUpdatedStructureUIOperation(LocationKey locKey, IDocument document) {
		this(LangCore.getSourceModelManager(), locKey, document);
	}
	
	public GetUpdatedStructureUIOperation(SourceModelManager modelManager, LocationKey locKey, IDocument document) {
		super("Awaiting Structure Calculation");
		this.modelManager = assertNotNull(modelManager);
		this.locationKey = assertNotNull(locKey);
		this.document = assertNotNull(document);
	}
	
	@Override
	protected String getTaskName() {
		return operationName;
	}
	
	protected StructureInfo structureInfo;
	
	@Override
	public void doExecute() throws CommonException, OperationCancellation {
		StructureModelRegistration registration = modelManager.connectStructureUpdates(locationKey, document, (__) -> { });
		structureInfo = registration.structureInfo;
		try {
			super.doExecute();
		} finally {
			registration.dispose();
		}
	}
	
	@Override
	protected void executeBackgroundOperation() throws CommonException, OperationCancellation {
		if(!structureInfo.isStale()) {
			Result<SourceFileStructure, CommonException> structureResult = structureInfo.getStoredData();
			if(structureResult.isException()) {
				// TODO: retry computation if in error
			}
			// No need for background op
			result = structureResult.get();
			return;
		} else {
			super.executeBackgroundOperation();
		}
	}
	
	@Override
	protected SourceFileStructure doBackgroundValueComputation(IOperationMonitor om) 
			throws OperationCancellation, CommonException {
		return structureInfo.awaitUpdatedData(om).get();
	}
	
	@Override
	protected void handleComputationResult(SourceFileStructure result) throws CommonException {
		if(result == null) {
			throw new CommonException(
				"Could not retrieve source file structure for: " + structureInfo.getKey2().getLabel());
		}
	}
	
	/* ----------------- helpers ----------------- */
	
	public static SourceFileStructure run_GetUpdatedStructure_UserOperation(ISourceBufferExt sourceBuffer) {
		Optional<Location> location = sourceBuffer.getLocation_opt();
		
		LocationKey key;
		if(location.isPresent()) {
			key = new LocationKey(location.get());
		} else {
			key = new LocationKey(sourceBuffer.getKeyForCurrentInput(), "[Unlocated source buffer]");
		}
		IDocument doc = sourceBuffer.getDocument(); // TODO: try to refactor out the need to for a IDocument
		
		GetUpdatedStructureUIOperation op = new GetUpdatedStructureUIOperation(key, doc);
		return op.executeAndGetHandledResult();
	}
	
	public static SourceFileStructure run_GetUpdatedStructure(ITextEditor editor) {
		return run_GetUpdatedStructure_UserOperation(new EditorSourceBuffer(editor));
	}
	
	public static StructureElement run_GetUpdatedStructureElementAt(AbstractLangStructureEditor editor, int offset) {
		SourceFileStructure sourceFileStructure = run_GetUpdatedStructure(editor);
		
		if(sourceFileStructure == null) {
			return null; // Note, possible error result has already been handled and reported to the user.
		}
		return sourceFileStructure.getStructureElementAt(offset);
	}
	
}