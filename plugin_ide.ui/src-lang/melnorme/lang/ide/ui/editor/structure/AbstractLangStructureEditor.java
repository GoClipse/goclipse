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


import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import static melnorme.utilbox.core.CoreUtil.areEqual;
import static melnorme.utilbox.core.CoreUtil.array;
import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.engine.EngineClient;
import melnorme.lang.ide.core.engine.IStructureModelListener;
import melnorme.lang.ide.core.engine.StructureModelManager.StructureInfo;
import melnorme.lang.ide.ui.editor.AbstractLangEditor;
import melnorme.lang.ide.ui.editor.EditorUtils;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.lang.tooling.structure.SourceFileStructure;
import melnorme.lang.tooling.structure.StructureElement;
import melnorme.utilbox.fields.DomainField;
import melnorme.utilbox.misc.Location;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.part.IShowInTargetList;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

/**
 * Extension to {@link AbstractLangEditor} with functionality to hande {@link StructureElement}s
 */
public abstract class AbstractLangStructureEditor extends AbstractLangEditor {
	
	protected final EngineClient engineClient = LangCore.getEngineClient();
	protected final LangOutlinePage outlinePage = addOwned(init_createOutlinePage());
	
	public AbstractLangStructureEditor() {
		super();
		
		engineClient.getStructureManager().addListener(new IStructureModelListener() {
			@Override
			public void structureChanged(final SourceFileStructure structure, StructureInfo lockedStructureInfo) {
				final Object key = lockedStructureInfo.getKey();
				
				// Note: editorKey may be null at this stage.
				
				if(!areEqual(key, editorKey))
					return;
				
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						notifyEditorStructureUpdated(key, structure);
					}
				});
			}
		});
	}
	
	public EngineClient getEngineClient() {
		return engineClient;
	}
	
	protected Location editorLocation;
	protected volatile Object editorKey;
	
	@Override
	protected void internalDoSetInput(IEditorInput input) {
		super.internalDoSetInput(input);
		
		editorKey = getStructureModelKeyFromEditorInput(input);
		editorLocation = (Location) (editorKey instanceof Location ? editorKey : null);
		
		// From this moment onwards, the structure model listener will give updates to new key only
		
		// Send initial update
		SourceFileStructure storedStructure = engineClient.getStructureManager().getStoredStructure(editorKey);
		notifyEditorStructureUpdated(editorKey, storedStructure);
	}
	
	public static Object getStructureModelKeyFromEditorInput(IEditorInput input) {
		try {
			// Try to adapt as Location
			return EditorUtils.getLocationFromEditorInput(input);
		} catch(CoreException e) {
			// Is input thread-safe? We assume so since IEditorInput is supposed to be immutable.
			return input;
		}
	}
	
	protected final DomainField<SourceFileStructure> structureField = new DomainField<>();
	
	public DomainField<SourceFileStructure> getStructureField() {
		return structureField;
	}
	
	public SourceFileStructure getSourceFileStructure() {
		return structureField.getFieldValue();
	}
	
	protected void notifyEditorStructureUpdated(Object key, SourceFileStructure structure) {
		assertTrue(Display.getCurrent() != null);
		
		if(!areEqual(key, editorKey)) {
			return; // editorKey might have changed, so re-check
		}
		
		if(structure == null) {
			return; // Ignore
		}
		
		structureField.setFieldValue(structure);
	}
	
	/* ----------------- Outline ----------------- */
	
	protected LangOutlinePage init_createOutlinePage() {
		return new LangOutlinePage(this);
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public Object getAdapter(Class requestedClass) {
		if (IContentOutlinePage.class.equals(requestedClass)) {
			return outlinePage;
		}
		if(requestedClass == IShowInTargetList.class) {
			return new IShowInTargetList() {
				@Override
				public String[] getShowInTargetIds() {
					return array(IPageLayout.ID_OUTLINE);
				}
			};
		}
		return super.getAdapter(requestedClass);
	}
	
	/* -----------------  ----------------- */
	
	public void setElementSelection(StructureElement element) {
		setElementSelection(this, element);
		markInNavigationHistory();
	}
	
	public static void setElementSelection(ITextEditor editor, StructureElement element) {
		SourceRange nameSR = element.getNameSourceRange();
		if(nameSR != null) {
			editor.selectAndReveal(nameSR.getOffset(), nameSR.getLength());
		}
	}
	
}