/*******************************************************************************
 * Copyright (c) 2014, 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package LANG_PROJECT_ID.ide.ui.navigator;

import java.text.Collator;

import melnorme.lang.ide.core.project_model.view.IBundleModelElement;
import melnorme.lang.ide.ui.navigator.LangNavigatorSorter;

public class LANGUAGE_NavigatorSorter extends LangNavigatorSorter {
	
	public LANGUAGE_NavigatorSorter() {
		super();
	}
	
	public LANGUAGE_NavigatorSorter(Collator collator) {
		super(collator);
	}
	
	@Override
	protected LangNavigatorSorter_Switcher switcher_Sorter() {
		return new LangNavigatorSorter_Switcher() {
			@Override
			public Integer visitBundleElement(IBundleModelElement bundleElement) {
				return new BundleModelElementsSorterSwitcher() { 
					
				}.switchBundleElement(bundleElement);
			}
		};
	}
	
}