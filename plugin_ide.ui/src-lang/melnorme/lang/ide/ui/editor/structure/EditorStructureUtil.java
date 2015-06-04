/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.editor.structure;

import static melnorme.lang.ide.ui.EditorSettings_Actual.EDITOR_ID;
import melnorme.lang.ide.ui.editor.EditorUtils;
import melnorme.lang.tooling.structure.ISourceFileStructure;
import melnorme.lang.tooling.structure.StructureElement;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.ITextEditor;

public class EditorStructureUtil {

	public static void openInEditorAndReveal(Object selectedElement) throws CoreException {
		
		if(selectedElement instanceof StructureElement) {
			StructureElement structureElement = (StructureElement) selectedElement;
			
			ISourceFileStructure fileStructure = structureElement.getContainingFileStructure();
			if(fileStructure == null || fileStructure.getLocation2() == null) {
				return;
			}
			
			IEditorInput newInput = EditorUtils.getBestEditorInputForLoc(fileStructure.getLocation2());
			
			IEditorPart part = EditorUtils.openEditor(newInput, EDITOR_ID, true);
			revealInEditor(part, structureElement);
		}
		
	}
	
	public static void revealInEditor(IEditorPart part, StructureElement element) {
		if(element == null || part == null)
			return;
		
		if(part instanceof AbstractLangStructureEditor) {
			((AbstractLangStructureEditor) part).setElementSelection(element);
			return;
		}
		
		if(part instanceof ITextEditor) {
			AbstractLangStructureEditor.setElementSelection((ITextEditor) part, element);
			return;
		}
		
	}
	
}