package com.googlecode.goclipse.navigator;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.model.IWorkbenchAdapter;

/**
 * An IWorkbenchAdapter implementation for IFileStore objects.
 */
class FileStoreWorkbenchAdapter implements IWorkbenchAdapter {
	
	private static final IResource[] NO_CHILDREN = new IResource[0];
	
	@Override
	public Object[] getChildren(Object object) {
		try {
			if (object instanceof IFileStore) {
				IFileStore fileStore = (IFileStore) object;
				return fileStore.childStores(EFS.NONE, null);
			}
		} catch (CoreException exception) {
			//fall through
		}
		return NO_CHILDREN;
	}
	
	@Override
	public ImageDescriptor getImageDescriptor(Object object) {
		if (object instanceof IFileStore) {
			IFileStore fileStore = (IFileStore) object;
			
			try {
				if (fileStore.fetchInfo().isDirectory()) {
					return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(
						ISharedImages.IMG_OBJ_FOLDER);
				}
				
				IEditorDescriptor descriptor = IDE.getEditorDescriptor(fileStore.getName());
				
				if (descriptor != null) {
					return descriptor.getImageDescriptor();
				} else {
					return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(
						ISharedImages.IMG_OBJ_FILE);
				}
			} catch (PartInitException e) {
				
			}
		}
		
		return null;
	}
	
	@Override
	public String getLabel(Object object) {
		if (object instanceof IFileStore) {
			IFileStore fileStore = (IFileStore) object;
			return fileStore.getName();
		} else {
			return null;
		}
	}
	
	@Override
	public Object getParent(Object object) {
		if (object instanceof IFileStore) {
			IFileStore fileStore = (IFileStore) object;
			return fileStore.getParent();
		} else {
			return null;
		}
	}
	
}