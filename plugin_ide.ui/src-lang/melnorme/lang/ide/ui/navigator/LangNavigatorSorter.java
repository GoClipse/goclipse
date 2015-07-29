/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corporation and others.
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

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ViewerSorter;

public abstract class LangNavigatorSorter extends ViewerSorter {
	
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
			return visitFolder();
		}
		
		@Override
		public Integer visitBuildTargetsElement(BuildTargetsContainer buildTargetsElement) {
			return -8;
		}
		
		@Override
		public Integer visitBuildTarget(BuildTargetElement buildTargetElement) {
			return buildTargetElement.getOrder();
		}
		
		@Override
		public Integer visitOther(Object element) {
			if(element instanceof IFolder) {
				return visitFolder();
			}
			return 0;
		}
		
		protected int visitFolder() {
			return -2;
		}
	}
	
}