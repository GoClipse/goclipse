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

import org.eclipse.core.filesystem.IFileStore;

import com.googlecode.goclipse.ui.navigator.elements.GoPathElement;
import com.googlecode.goclipse.ui.navigator.elements.GoPathEntryElement;
import com.googlecode.goclipse.ui.navigator.elements.GoRootElement;

import melnorme.lang.ide.core.project_model.view.IBundleModelElement;
import melnorme.lang.ide.ui.navigator.LangNavigatorSorter;

/**
 * Sorter for the NCE extension
 */
public class GoNavigatorSorter extends LangNavigatorSorter {
	
	@Override
	protected LangNavigatorSorter_Switcher switcher_Sorter() {
		return new LangNavigatorSorter_Switcher() {
			
			@Override
			public Integer visitBundleElement(IBundleModelElement bundleElement) {
				return new BundleModelElementsSorterSwitcher() {
					
				}.switchBundleElement(bundleElement);
			}
			
			@Override
			public Integer visitGoPathElement(GoPathElement goPathElement) {
				if(goPathElement instanceof GoRootElement) {
					return -20;
				}
				if(goPathElement instanceof GoPathEntryElement) {
					return -10;
				}
				throw assertFail();
			}
			
			@Override
			public Integer visitFileStoreElement(IFileStore fileStore) {
				return fileStore.fetchInfo().isDirectory() ? -2 : 0;
			}
		};
	}
	
}