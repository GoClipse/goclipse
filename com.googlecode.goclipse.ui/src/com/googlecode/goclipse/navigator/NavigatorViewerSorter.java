package com.googlecode.goclipse.navigator;

import java.util.Comparator;

import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ViewerSorter;

/**
 * A sorter for the Go CNF viewer.
 * 
 * @author devoncarew
 */
public class NavigatorViewerSorter extends ViewerSorter {

	public NavigatorViewerSorter() {

	}

	@Override
	protected Comparator<?> getComparator() {
		return String.CASE_INSENSITIVE_ORDER;
	}

	@Override
	public int category(Object element) {
		if (element instanceof IFolder) {
			IFolder folder = (IFolder) element;

			if ("src".equals(folder)) {
				return 0;
			} else {
				return 1;
			}
		} else if (element instanceof IResource) {
			return 2;
		} else if (element instanceof IFileStore) {
			IFileStore file = (IFileStore) element;

			if (file.fetchInfo().isDirectory()) {
				return 3;
			} else {
				return 4;
			}
		} else {
			return 5;
		}
	}

}
