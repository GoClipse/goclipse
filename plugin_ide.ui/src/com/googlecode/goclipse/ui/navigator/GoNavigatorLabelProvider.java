/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.ui.navigator;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import static melnorme.utilbox.core.CoreUtil.areEqual;
import melnorme.lang.ide.core.LangNature;
import melnorme.lang.ide.ui.views.AbstractLangLabelProvider;

import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.googlecode.goclipse.ui.GoPluginImages;
import com.googlecode.goclipse.ui.navigator.elements.GoPathEntryElement;
import com.googlecode.goclipse.ui.navigator.elements.GoRootElement;
import com.googlecode.goclipse.ui.navigator.elements.IGoProjectElement;

public class GoNavigatorLabelProvider extends AbstractLangLabelProvider  {
	
	protected static final RGB LOCATION_ANNOTATION_FG = new RGB(128, 128, 128);
	
	@Override
	public StyledString getStyledText(Object element) {
		
		if(element instanceof IGoProjectElement) {
			IGoProjectElement goProjectElement = (IGoProjectElement) element;
			
			StyledString baseText = new StyledString(goProjectElement.getName());
			
			if(element instanceof GoRootElement) {
				GoRootElement goRootElement = (GoRootElement) element;
				baseText.append(" - " + goRootElement.getDirectory().toString(), fgColor(LOCATION_ANNOTATION_FG));
				return baseText;
			}
			if(element instanceof GoPathEntryElement) {
				GoPathEntryElement goPathEntryElement = (GoPathEntryElement) element;
				baseText.append(" - " + goPathEntryElement.getDirectory().toString(), fgColor(LOCATION_ANNOTATION_FG));
				return baseText;
			}
			assertFail();
		}
		
		if (element instanceof IFileStore) {
			IFileStore fileStore = (IFileStore) element;
			return new StyledString(fileStore.getName());
		}
		
		return null;
	}
	
	@Override
	public Image getImage(Object element) {
		
		if(element instanceof IGoProjectElement) {
			if(element instanceof GoRootElement) {
				return GoPluginImages.NAVIGATOR_GOROOT_ENTRY.getImage();
			}
			if(element instanceof GoPathEntryElement) {
				return GoPluginImages.NAVIGATOR_GOPATH_ENTRY.getImage();
			}
			assertFail();
		}
		
		if (element instanceof IFileStore) {
			IFileStore fileStore = (IFileStore) element;
			
			try {
				if (fileStore.fetchInfo().isDirectory()) {
					return GoPluginImages.NAVIGATOR_SOURCE_PACKAGE_FOLDER.getImage();
				}
				
				// TODO: should cleanup up this.
				
				IEditorDescriptor descriptor = IDE.getEditorDescriptor(fileStore.getName());
				if (descriptor != null) {
					return descriptor.getImageDescriptor().createImage();
				} else {
					return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(
						ISharedImages.IMG_OBJ_FILE).createImage();
				}
			} catch (PartInitException e) {
				
			}
		}
		
		if(element instanceof IResource) {
			IResource resource = (IResource) element;
			try {
				assertTrue(resource.getProject().hasNature(LangNature.NATURE_ID));
			} catch (CoreException e) {
				assertFail();
			}
			
			ImageDescriptor decoratedImage = getResourceImageDescriptor(resource);
			if(decoratedImage != null) {
				return decoratedImage.createImage();
			}
		}
		
		return null;
	}
	
	protected ImageDescriptor getResourceImageDescriptor(IResource resource) {
		if (resource instanceof IFolder) {
			IFolder folder = (IFolder)resource;
			
			if(resource.getParent() instanceof IProject) {
				if("src".equals(resource.getName())) {
					return GoPluginImages.NAVIGATOR_SRC_FOLDER.getDescriptor();
				} else if("pkg".equals(resource.getName())) {
					return GoPluginImages.NAVIGATOR_PKG_FOLDER.getDescriptor();
				} else if("bin".equals(resource.getName())) {
					return GoPluginImages.NAVIGATOR_BIN_FOLDER.getDescriptor();
				}
			} else if(isSubpackage(folder)) {
				return GoPluginImages.NAVIGATOR_SOURCE_PACKAGE_FOLDER.getDescriptor();
			}
		} else if(resource instanceof IFile) {
			IFile file = (IFile) resource;
			
			if(areEqual("a", file.getFileExtension())) {
				return GoPluginImages.NAVIGATOR_GO_PACKAGE_FILE.getDescriptor();
			}
		}
		return null;
	}
	
	@SuppressWarnings("unused")
	protected static boolean isSubpackage(IFolder folder) {
		return false; // TODO:
	}
	
}