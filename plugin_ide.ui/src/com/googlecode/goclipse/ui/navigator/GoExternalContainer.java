package com.googlecode.goclipse.ui.navigator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;

import com.googlecode.goclipse.Activator;

public abstract class GoExternalContainer implements IGoSourceContainer, IAdaptable {
	
	private IFileStore store;
	
	protected GoExternalContainer(IFileStore store) {
		this.store = store;
	}

	public IFileStore getFileStore() {
		return store;
	}

	protected abstract Object createChild(IFileStore childStore);
	
	@Override
	public Object[] getChildren() {
		try {
			IFileStore[] storeArray = store.childStores(EFS.NONE, null);
			List<Object> result = new ArrayList<Object>();
			for(IFileStore childStore: storeArray) {
				if(childStore.fetchInfo().isDirectory()) {
					result.add(createChild(childStore));
				} else {
					result.add(childStore);
				}
			}
			return result.toArray();				
		} catch(CoreException e) {
			Activator.logError(e);
			return new Object[0];
		}
	}

	@Override
	public int findMaxProblemSeverity() {
		// ignore
		return -1;
	}

	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == IFileStore.class) {
			return store;
		}
		return store.getAdapter(adapter);
	}

	@Override
	public int hashCode() {
		return store.hashCode();
	}
	
	@Override
	public String toString() {
		return store.getName();
	}
}
