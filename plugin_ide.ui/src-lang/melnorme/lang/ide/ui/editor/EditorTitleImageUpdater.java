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
package melnorme.lang.ide.ui.editor;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.utilbox.ownership.IDisposable;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorInput;

import _org.eclipse.jdt.internal.ui.viewsupport.IProblemChangedListener;
import _org.eclipse.jdt.ui.ProblemsLabelDecorator;


public class EditorTitleImageUpdater implements IProblemChangedListener, IDisposable {
	
	protected final AbstractLangEditor editor;
	protected final ProblemsLabelDecorator problemsDecorator = new ProblemsLabelDecorator(
		LangUIPlugin.getDefault().getImageDescriptorRegistry());
	
	public EditorTitleImageUpdater(AbstractLangEditor editor) {
		super();
		this.editor = assertNotNull(editor);
		LangUIPlugin.getDefault().getProblemMarkerManager().addListener(this);
	}
	
	@Override
	public void dispose() {
		LangUIPlugin.getDefault().getProblemMarkerManager().removeListener(this);
		problemsDecorator.dispose();
	}
	
	@Override
	public void problemsChanged(IResource[] changedResources, boolean isMarkerChange, boolean calledFromDisplayThread) {
		if(!isMarkerChange)
			return;
		
		IEditorInput input = editor.getEditorInput();
		if(input == null)
			return;
		
		IFile file = EditorUtils.getAssociatedFile(input);
		for (IResource changedResource : changedResources) {
			if(changedResource.equals(file)) {
				updateEditorImage(file);
			}
		}
	}
	
	public void updateEditorImage(IFile editorFile) {
		Image titleImage= editor.getTitleImage();
		if (titleImage == null)
			return;
		
		Image baseImage = editor.getBaseEditorImage().getImage();
		Image newImage = problemsDecorator.decorateImage(baseImage, editorFile);
		
		if(titleImage != newImage) {
			editor.setTitleImage(newImage);
		}
	}
	
}