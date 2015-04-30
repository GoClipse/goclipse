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


import static melnorme.utilbox.core.CoreUtil.array;
import melnorme.lang.ide.ui.editor.AbstractLangEditor;
import melnorme.lang.tooling.structure.IStructureElement;
import melnorme.lang.tooling.structure.SourceFileStructure;
import melnorme.lang.tooling.structure.StructureElement;
import melnorme.utilbox.misc.Location;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.part.IShowInTargetList;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

/**
 * Extension to {@link AbstractLangEditor} with functionality to hande {@link StructureElement}s
 */
public abstract class AbstractLangStructureEditor extends AbstractLangEditor {
	
	protected final LangOutlinePage outlinePage = addOwned(init_createOutlinePage());
	
	public AbstractLangStructureEditor() {
		super();
	}
	
	@Override
	protected void internalDoSetInput(IEditorInput input) {
		super.internalDoSetInput(input);
		
		updateOutlinePageInput();
	}
	
	/* ----------------- Outline ----------------- */
	
	protected LangOutlinePage init_createOutlinePage() {
		return new LangOutlinePage(this);
	}
	
	public void updateOutlinePageInput() {
		outlinePage.updateInputFromEditor();
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
	
	/* ----------------- Structure helpers ----------------- */
	
	public IStructureElement getStructureElementAt(int offset) {
		Location inputLocation;
		try {
			inputLocation = getInputLocation();
		} catch(CoreException e) {
			return null;
		}
		
		SourceFileStructure structure;
		if(true) {
			structure = StructureModelManager.getDefault().getStructure(inputLocation);
		} else {
//			structure = StructureModelManager.getDefault().getNonNullStructureSentinel(inputLocation).get();
		}
		
		/*FIXME: BUG here TODO */
		return null;
//		return structure.getStructureElementAt(offset);
	}
	
}