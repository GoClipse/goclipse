package com.googlecode.goclipse.navigator;

import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.model.IWorkbenchAdapter;

import com.googlecode.goclipse.go.lang.GoPathElement;

/**
 * This adapter factory adapts GoPathElement and IFileStore objects to IWorkbenchAdapters.
 */
public class GoWorkbenchAdapterFactory implements IAdapterFactory {
	private GoPathElementWorkbenchAdapter workbenchAdapter = new GoPathElementWorkbenchAdapter();
	private FileStoreWorkbenchAdapter fileStoreAdapter = new FileStoreWorkbenchAdapter();

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adaptableObject instanceof GoPathElement) {
			return workbenchAdapter;
		} else if (adaptableObject instanceof IFileStore) {
			return fileStoreAdapter;
		}

		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class[] getAdapterList() {
		return new Class[] { IWorkbenchAdapter.class };
	}

}
