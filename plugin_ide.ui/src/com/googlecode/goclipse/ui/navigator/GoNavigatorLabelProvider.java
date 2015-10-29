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
package com.googlecode.goclipse.ui.navigator;

import static melnorme.lang.ide.ui.views.StylerHelpers.fgColor;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;
import static melnorme.utilbox.core.CoreUtil.areEqual;

import java.nio.file.Path;

import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.ui.GoPluginImages;
import com.googlecode.goclipse.ui.navigator.elements.GoPathElement;
import com.googlecode.goclipse.ui.navigator.elements.GoPathEntryElement;
import com.googlecode.goclipse.ui.navigator.elements.GoRootElement;

import melnorme.lang.ide.core.project_model.view.IBundleModelElement;
import melnorme.lang.ide.ui.views.LangNavigatorLabelProvider;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.MiscUtil;

public class GoNavigatorLabelProvider extends LangNavigatorLabelProvider  {
	
	protected static final RGB LOCATION_ANNOTATION_FG = new RGB(128, 128, 128);
	
	@Override
	protected DefaultGetStyledStringSwitcher getStyledString_switcher() {
		return new DefaultGetStyledStringSwitcher() {
			@Override
			public StyledString visitOther(Object element) {
				return null;
			}
			
			@Override
			public StyledString visitGoPathElement(GoPathElement goPathElement) {
				return getStyledText_GoPathElement(goPathElement);
			}
			
			@Override
			public StyledString visitFileStoreElement(IFileStore fileStore) {
				return new StyledString(fileStore.getName());
			}
			
			@Override
			public StyledString visitBundleElement(IBundleModelElement bundleElement) {
				return new BundleModelGetStyledStringSwitcher() {
					
				}.switchBundleElement(bundleElement);
			}
			
		};
	}
	
	public StyledString getStyledText_GoPathElement(GoPathElement goPathElement) {
			
		StyledString baseText = new StyledString(goPathElement.getName());
		
		if(goPathElement instanceof GoRootElement) {
			GoRootElement goRootElement = (GoRootElement) goPathElement;
			baseText.append(" - " + goRootElement.getDirectory().toString(), fgColor(LOCATION_ANNOTATION_FG));
			return baseText;
		}
		if(goPathElement instanceof GoPathEntryElement) {
			GoPathEntryElement goPathEntryElement = (GoPathEntryElement) goPathElement;
			
			baseText.append(" - ", fgColor(LOCATION_ANNOTATION_FG));
			
			String goPathEntryLocation = goPathEntryElement.getDirectory().toString();
			
			StyledString suffix;
			if(goPathEntryElement.isProjectInsideGoPath()) {
				suffix = new StyledString(goPathEntryLocation, 
					new ItalicStyler(fgColor(LOCATION_ANNOTATION_FG)));
			} else {
				suffix = new StyledString(goPathEntryLocation, fgColor(LOCATION_ANNOTATION_FG));
			}
			baseText.append(suffix);
			
			return baseText;
		}
		throw assertFail();
	}
	
	@Override
	protected DefaultGetImageSwitcher getBaseImage_switcher() {
		return new DefaultGetImageSwitcher() {
			
			@Override
			public ImageDescriptor visitOther(Object element) {
				if(element instanceof IResource) {
					IResource resource = (IResource) element;
					return getResourceImageDescriptor(resource);
				}
				return null;
			}
			
			@Override
			public ImageDescriptor visitGoPathElement(GoPathElement goPathElement) {
				if(goPathElement instanceof GoRootElement) {
					return GoPluginImages.NAV_LibraryNative;
				}
				if(goPathElement instanceof GoPathEntryElement) {
					return GoPluginImages.NAVIGATOR_GOPATH_ENTRY.getDescriptor();
				}
				throw assertFail();
			}

			@Override
			public ImageDescriptor visitFileStoreElement(IFileStore fileStore) {
				try {
					if (fileStore.fetchInfo().isDirectory()) {
						return GoPluginImages.NAV_SourceFolder;
					}
					
					// TODO: should cleanup up this.
					
					IEditorDescriptor descriptor = IDE.getEditorDescriptor(fileStore.getName());
					if (descriptor != null) {
						return descriptor.getImageDescriptor();
					} else {
						IWorkbench workbench = PlatformUI.getWorkbench();
						return workbench.getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_FILE);
					}
				} catch (PartInitException e) {
				}
				return null;
			}
			
			@Override
			public ImageDescriptor visitBundleElement(IBundleModelElement bundleElement) {
				return new BundleModelGetImageSwitcher() {
					
				}.switchBundleElement(bundleElement);
			}
			
		};
	}
	
	protected ImageDescriptor getResourceImageDescriptor(IResource resource) {
		if (resource instanceof IFolder) {
			IFolder folder = (IFolder)resource;
			
			IProject project = resource.getProject();
			boolean isProjecInsideGoPath;
			try {
				isProjecInsideGoPath = GoProjectEnvironment.isProjectInsideGoPathSourceFolder(project);
			} catch (CommonException e) {
				return null;
			}
			
			if(resource.getParent() instanceof IProject && !isProjecInsideGoPath) {
				if("src".equals(resource.getName())) {
					return GoPluginImages.NAV_SourceFolder;
				} else if("pkg".equals(resource.getName())) {
					return GoPluginImages.NAVIGATOR_PKG_FOLDER.getDescriptor();
				} else if("bin".equals(resource.getName())) {
					return GoPluginImages.NAV_OutputFolder;
				}
			} else if(isSourcePackageFolder(folder, isProjecInsideGoPath)) {
				return GoPluginImages.NAV_SourceFolder;
			}
		} else if(resource instanceof IFile) {
			IFile file = (IFile) resource;
			
			if(areEqual("a", file.getFileExtension())) {
				return GoPluginImages.NAVIGATOR_GO_PACKAGE_FILE.getDescriptor();
			}
		}
		return null;
	}
	
	protected static boolean isSourcePackageFolder(IFolder folder, boolean isProjecInsideGoPath) {
		Path path = folder.getProjectRelativePath().toFile().toPath();
		
		if(isProjecInsideGoPath) {
			return isValidSourcePackageName(path);
		} else {
			if(path.startsWith("src")) {
				path = MiscUtil.createValidPath("src").relativize(path);
				return isValidSourcePackageName(path);
			}
			return false;
		}
	}
	
	@SuppressWarnings("unused")
	protected static boolean isValidSourcePackageName(Path path) {
		return true; // TODO: check for invalid names
	}
	
}