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

import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ViewerSorter;

import com.googlecode.goclipse.ui.navigator.elements.GoPathEntryElement;
import com.googlecode.goclipse.ui.navigator.elements.GoRootElement;

/**
 * Sorter for the NCE extension
 */
public class GoNavigatorSorter extends ViewerSorter {
	
	@Override
	public int category(Object element) {
		
		if(element instanceof GoRootElement) {
			return -20;
		}
		if(element instanceof GoPathEntryElement) {
			return -10;
		}
		
		if(element instanceof IFolder) {
			return -2;
		}
		if (element instanceof IResource) {
			return 0;
		}
		
		if (element instanceof IFileStore) {
			IFileStore file = (IFileStore)element;
			return file.fetchInfo().isDirectory() ? -2 : 0;
		}
		return 0;
	}
	
}