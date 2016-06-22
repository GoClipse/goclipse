/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.navigator;


import java.text.Collator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ViewerComparator;

import melnorme.lang.ide.core.project_model.view.BundleErrorElement;
import melnorme.lang.ide.core.project_model.view.BundleModelElementKind.BundleModelElementsSwitcher;
import melnorme.lang.ide.core.project_model.view.DependenciesContainer;
import melnorme.lang.ide.core.project_model.view.IBundleModelElement;
import melnorme.lang.ide.core.project_model.view.RawDependencyElement;

public abstract class LangNavigatorSorter extends ViewerComparator {
	
	public LangNavigatorSorter() {
		super();
	}
	
	public LangNavigatorSorter(Collator collator) {
		super(collator);
	}
	
	@Override
	public int category(Object element) {
		Integer result = switcher_Sorter().switchElement(element);
		if(result == null) {
			return 0;
		}
		return result;
	}
	
	protected abstract LangNavigatorSorter_Switcher switcher_Sorter();
	
	protected static abstract class LangNavigatorSorter_Switcher implements NavigatorElementsSwitcher<Integer> {
		
		@Override
		public Integer visitProject(IProject project) {
			return -2;
		}
		@Override
		public Integer visitFolder(IFolder folder) {
			return -2;
		}
		
		@Override
		public Integer visitManifestFile(IFile element) {
			return 0;
		}
		
		@Override
		public Integer visitBundleElement(IBundleModelElement bundleElement) {
			return new BundleModelElementsSorterSwitcher().switchBundleElement(bundleElement);
		}
		
		@Override
		public Integer visitBuildTargetsElement(BuildTargetsContainer buildTargetsElement) {
			return -8;
		}
		
		@Override
		public Integer visitBuildTarget(BuildTargetElement buildTargetElement) {
			return buildTargetElement.getOrder();
		}
		
	}
	
	public static abstract class BundleModelElementsSorterSwitcher_Default 
		implements BundleModelElementsSwitcher<Integer> {
		
		@Override
		public Integer visitDepContainer(DependenciesContainer element) {
			return -10;
		}
		
		@Override
		public Integer visitRawDepElement(RawDependencyElement element) {
			return 0;
		}
		
		@Override
		public Integer visitErrorElement2(BundleErrorElement element) {
			return -10;
		}
		
	}
	
}