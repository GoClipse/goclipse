/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.editor;

import static melnorme.utilbox.core.CoreUtil.tryCast;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IReusableEditor;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.IURIEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.ide.ResourceUtil;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.IEditorStatusLine;
import org.eclipse.ui.texteditor.ITextEditor;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.ide.ui.utils.WorkbenchUtils;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

public class EditorUtils {
	
	public static IDocument getEditorDocument(ITextEditor textEditor) {
		return textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());
	}
	
	public static TextSelection getSelection(ITextEditor editor) {
		return (TextSelection) editor.getSelectionProvider().getSelection();
	}
	
	public static SourceRange getSelectionSR(ITextEditor editor) {
		TextSelection sel = getSelection(editor);
		return new SourceRange(sel.getOffset(), sel.getLength());
	}
	
	public static void setEditorSelection(ITextEditor textEditor, SourceRange sourceRange) {
		EditorUtils.setEditorSelection(textEditor, sourceRange.getOffset(), sourceRange.getLength()); 
	}
	
	public static void setEditorSelection(ITextEditor textEditor, int offset, int length) {
		textEditor.getSelectionProvider().setSelection(new TextSelection(offset, length)); 
	}
	
	/* ----------------- Editor input utils ----------------- */
	
	public static IProject getAssociatedProject(IEditorInput input) {
		if(input == null) {
			return null;
		}
		
		IResource resource = getAssociatedResource(input);
		if(resource != null) {
			return resource.getProject();
		}
		return null;
	}
	
	public static IResource getAssociatedResource(IEditorInput input) {
		if(input == null) {
			return null;
		}
		
		if(input instanceof IFileEditorInput) {
			return ((IFileEditorInput) input).getFile();
		}
		
		IResource resource = (IResource) input.getAdapter(IResource.class);
		if(resource != null) {
			return resource;
		}
		return (IProject) input.getAdapter(IProject.class);
	}
	
	public static IFile getAssociatedFile(IEditorInput editorInput) {
//		if (editorInput instanceof IFileEditorInput) {
//			return ((IFileEditorInput) editorInput).getFile();
//		}
		return ResourceUtil.getFile(editorInput);
	}
	
	/** Get a resource related to the input of this editor, or null if none. */
	public static IFile findFileOfEditor(IEditorPart editor) {
		return getAssociatedFile(editor.getEditorInput());
	}
	
	public static IEditorInput getBestEditorInputForLoc(Location fileLoc) {
		return getBestEditorInputForUri(fileLoc.toUri());
	}
	
	public static IEditorInput getBestEditorInputForUri(URI uri) {
		return getBestEditorInputForUri(uri, IResource.NONE);
	}
	
	public static IEditorInput getBestEditorInputForUri(URI uri, int memberFlags) {
		IFile[] files = ResourceUtils.getWorkspaceRoot().findFilesForLocationURI(uri, memberFlags);
		if(files.length != 0) {
			// As an improvement, if there is more than one file, we could try to see which one is more relevant
			// instead of just using the first one.
			IFile file = files[0]; 
			return new FileEditorInput(file);
		} else {
			//file not in workspace
			IFileStore fileOnLocalDisk = EFS.getLocalFileSystem().getStore(uri);
			return new FileStoreEditorInput(fileOnLocalDisk);
		}
	}
	
	public static Path getFilePathFromEditorInput(IEditorInput editorInput) {
		IURIEditorInput uriEditorInput;
		if(editorInput instanceof IURIEditorInput) {
			uriEditorInput = (IURIEditorInput) editorInput;
		} else {
			uriEditorInput = (IURIEditorInput) editorInput.getAdapter(IURIEditorInput.class);
		}
		if(uriEditorInput != null && uriEditorInput.getURI() != null) {
			try {
				return Paths.get(uriEditorInput.getURI());
			} catch (Exception e) {
			}
		}
		if(editorInput instanceof IStorageEditorInput) {
			IStorageEditorInput storageEditorInput = (IStorageEditorInput) editorInput;
			try {
				IPath fullPath = storageEditorInput.getStorage().getFullPath();
				if(fullPath != null) {
					return fullPath.toFile().toPath();
				}
			} catch (CoreException ce) {
				LangCore.logStatus(ce);
			}
		}
		
		return null;
	}
	
	public static Location getLocationFromEditorInput(IEditorInput editorInput) throws CommonException {
		Path filePath = getFilePathFromEditorInput(editorInput);
		if (filePath == null) {
			throw new CommonException("Error: Could not determine file path for editor.", null);
		}
		
		return Location.createValidLocation(filePath, "Invalid editorInput path: ");
	}
	
	public static Location getLocationOrNull(IEditorInput editorInput) {
		try {
			return getLocationFromEditorInput(editorInput);
		} catch(CommonException e) {
			return null;
		}
	}
	
	public static Location getInputLocationOrNull(IEditorPart editor) {
		Path inputPath = getFilePathFromEditorInput(editor.getEditorInput());
		return Location.createValidOrNull(inputPath);
	}
	
	public static Location getInputLocation(IEditorPart editor) throws CommonException {
		Location inputLocationOrNull = getInputLocationOrNull(editor);
		if(inputLocationOrNull == null) {
			throw new CommonException("Error, invalid location for editor input.");
		}
		return inputLocationOrNull;
	}
	
	/* -----------------  Editor opening utils  ----------------- */
	
	public static enum OpenNewEditorMode { ALWAYS, TRY_REUSING_EXISTING, NEVER }
	
	public static IEditorPart openEditor(IEditorInput newInput, String editorId) throws PartInitException {
		return openEditor(newInput, editorId, false);
	}
	
	public static IEditorPart openEditor(IEditorInput newInput, String editorId, boolean reuseExisitingEditor) 
			throws PartInitException {
		IWorkbenchPage page = WorkbenchUtils.getActiveWorkbenchWindow().getActivePage(); 
		int matchFlags = reuseExisitingEditor ? IWorkbenchPage.MATCH_INPUT : IWorkbenchPage.MATCH_NONE;
		return page.openEditor(newInput, editorId, true, matchFlags);
	}
	
	public static ITextEditor openTextEditorAndSetSelection(String editorId, IEditorInput newInput, 
			SourceRange sourceRange) throws CoreException {
		return openTextEditorAndSetSelection(null, editorId, newInput, OpenNewEditorMode.ALWAYS, sourceRange);
	}
	
	public static ITextEditor openTextEditorAndSetSelection(ITextEditor currentEditor, String editorId, 
			IEditorInput newInput, OpenNewEditorMode openNewEditor, SourceRange selectionRange) 
					throws PartInitException, CoreException {
		ITextEditor editor = openTextEditor(currentEditor, editorId, newInput, openNewEditor);
		if(selectionRange != null) {
			setEditorSelection(editor, selectionRange);
		}
		return editor;
	}
	
	public static ITextEditor openTextEditor(ITextEditor currentEditor, String editorId, IEditorInput newInput,
			OpenNewEditorMode openNewEditor) throws PartInitException, CoreException {
		IWorkbenchPage page;
		if(currentEditor == null) {
			page = WorkbenchUtils.getActivePage();
			openNewEditor = OpenNewEditorMode.ALWAYS;
		} else {
			page = currentEditor.getEditorSite().getWorkbenchWindow().getActivePage();
		}
		
		if(openNewEditor == OpenNewEditorMode.NEVER) {
			if(currentEditor.getEditorInput().equals(newInput)) {
				return currentEditor;
			} else if(currentEditor instanceof IReusableEditor) {
				IReusableEditor reusableEditor = (IReusableEditor) currentEditor;
				reusableEditor.setInput(newInput);
				return currentEditor;
			} else {
				openNewEditor = OpenNewEditorMode.ALWAYS;
			}
		}
		
		int matchFlags = openNewEditor == OpenNewEditorMode.ALWAYS ? 
			IWorkbenchPage.MATCH_NONE : IWorkbenchPage.MATCH_INPUT | IWorkbenchPage.MATCH_ID;
		IEditorPart editor = page.openEditor(newInput, editorId, true, matchFlags);
		ITextEditor targetEditor = tryCast(editor, ITextEditor.class);
		if(targetEditor == null) {
			throw LangCore.createCoreException("Not a text editor", null);
		}
		return targetEditor;
	}
	
	/* ----------------- ----------------- */
	
	public static void setStatusLineErrorMessage(ITextEditor editor, String message, Image image) {
		IEditorStatusLine statusLine= (IEditorStatusLine)editor.getAdapter(IEditorStatusLine.class);
		if(statusLine != null) {
			statusLine.setMessage(true, message, image);
		}
	}
	
	public static SourceRange getSelectedRange(ITextViewer viewer) {
		Point selectedRange = viewer.getSelectedRange();
		return new SourceRange(selectedRange.x, selectedRange.y);
	}
	
	/* ----------------- JDT ----------------- */
	
	/**
	 * Copy of {@link org.eclipse.jface.text.source.MatchingCharacterPainter#getSignedSelection()}
	 */
	public static final IRegion getSignedSelection(ITextViewer sourceViewer) {
		Point viewerSelection= sourceViewer.getSelectedRange();
	
		StyledText text= sourceViewer.getTextWidget();
		Point selection= text.getSelectionRange();
		if (text.getCaretOffset() == selection.x) {
			viewerSelection.x= viewerSelection.x + viewerSelection.y;
			viewerSelection.y= -viewerSelection.y;
		}
	
		return new Region(viewerSelection.x, viewerSelection.y);
	}
	
}