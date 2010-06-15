package com.googlecode.goclipse.ui.navigator;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.debug.ui.actions.ILaunchable;
import org.eclipse.ui.IContributorResourceAdapter;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.model.IWorkbenchAdapter;

/**
 * 
 * @author Mihailo Vasiljevic
 * 
 */
public class GoModelAdapter implements IAdapterFactory {

	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {

		
		if (adapterType == IResource.class) {
			if (adaptableObject instanceof IGoSourceContainer) {
				IFolder folderAdapter = ((IGoSourceContainer) adaptableObject)
						.getFolder();
				
				return folderAdapter;
			}
		}

		if (adaptableObject instanceof IAdaptable) {
			IAdaptable adaptable = (IAdaptable) adaptableObject;
			Object adapter = adaptable.getAdapter(adapterType);
			
			return adapter;
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class[] getAdapterList() {
		// this is overwritten by the adapter factory extension
		return new Class[] { IFolder.class, IFile.class, IContainer.class,
				IContributorResourceAdapter.class, IProject.class,
				ILaunchable.class, IPersistableElement.class,
				IContributorResourceAdapter.class, IWorkbenchAdapter.class };
	}

}
