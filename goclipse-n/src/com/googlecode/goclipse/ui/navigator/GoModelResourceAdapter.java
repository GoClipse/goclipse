package com.googlecode.goclipse.ui.navigator;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IAdapterFactory;

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
			if (f.getName().equals("src")) {
				return new GoSourceFolder(f);
			}
			return null;
		}

		return null;
	}

	@Override
	public Class[] getAdapterList() {

		return null;
	}

}
