/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.navigator;

import org.eclipse.core.resources.IProject;

import melnorme.lang.ide.core.project_model.view.IBundleModelElement;

interface NavigatorElementsSwitcher_Default<RET> {
	
	default RET switchElement(Object element) {
		if(element instanceof IProject) {
			return visitProject((IProject) element);
		} 
		else if(element instanceof BuildTargetsContainer) {
			return visitBuildTargetsElement((BuildTargetsContainer) element);
		} 
		else if(element instanceof BuildTargetElement) {
			return visitBuildTarget((BuildTargetElement) element);
		} 
		else if(element instanceof IBundleModelElement) {
			return visitBundleElement2((IBundleModelElement) element);
		} 
		else {
			return visitOther(element);
		}
	}
	
	public abstract RET visitProject(IProject project);
	
	public abstract RET visitBundleElement2(IBundleModelElement bundleElement);
	
	public abstract RET visitBuildTargetsElement(BuildTargetsContainer buildTargetsContainer);
	
	public abstract RET visitBuildTarget(BuildTargetElement buildTarget);
	
	public abstract RET visitOther(Object element);
	
}