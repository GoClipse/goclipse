package com.googlecode.goclipse.ui.navigator;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdapterFactory;

import com.googlecode.goclipse.Environment;

/**
 * 
 * @author Mihailo Vasiljevic
 * 
 */
public class GoModelResourceAdapter implements IAdapterFactory {

	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adaptableObject instanceof IFolder) {
			IFolder f = (IFolder) adaptableObject;
			if (Environment.INSTANCE.isCmdFile(f.getProjectRelativePath()) ||
					Environment.INSTANCE.isPkgFile(f.getProjectRelativePath()))
			{
				return new GoSourceFolder(f);
			}
			return null;
		}

		return null;
	}

	@Override
	public Class[] getAdapterList() {
		return new Class[] { IResource.class };
	}

}
