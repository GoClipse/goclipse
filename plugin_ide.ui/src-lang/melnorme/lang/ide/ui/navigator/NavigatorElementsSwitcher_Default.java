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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.project_model.view.IBundleModelElement;

interface NavigatorElementsSwitcher_Default<RET> {
	
	default RET switchElement(Object element) {
		if(element instanceof IResource) {
			IResource resource = (IResource) element;
			return visitResource(resource);
		}
		else if(element instanceof BuildTargetsContainer) {
			return visitBuildTargetsElement((BuildTargetsContainer) element);
		} 
		else if(element instanceof BuildTargetElement) {
			return visitBuildTarget((BuildTargetElement) element);
		} 
		else if(element instanceof IBundleModelElement) {
			return visitBundleElement((IBundleModelElement) element);
		} 
		
		return visitOther2(element);
	}
	
	default RET visitResource(IResource resource) {
		int type = resource.getType();
		if(type == IResource.PROJECT) {
			return visitProject((IProject) resource);
		} 
		if(type == IResource.FOLDER) {
			return visitFolder((IFolder) resource);
		}
		if(type == IResource.FILE) {
			return visitFile((IFile) resource);
		}
		return visitWorkspaceRoot((IWorkspaceRoot) resource);
	}
	
	default RET visitProject(IProject project) {
		return visitOther2(project);
	}
	default RET visitFolder(IFolder folder) {
		return visitOther2(folder);
	}
	default RET visitFile(IFile file){
		if(LangCore.getBundleModelManager().resourceIsManifest(file)) {
			return visitManifestFile(file);
		}
		return visitOther2(file);
	}
	default RET visitWorkspaceRoot(@SuppressWarnings("unused") IWorkspaceRoot workspaceRoot) {
		return null;
	}
	
	public abstract RET visitManifestFile(IFile element);
	
	public abstract RET visitBundleElement(IBundleModelElement bundleElement);
	
	public abstract RET visitBuildTargetsElement(BuildTargetsContainer buildTargetsContainer);
	
	public abstract RET visitBuildTarget(BuildTargetElement buildTarget);
	
	default RET visitOther2(@SuppressWarnings("unused") Object element) {
		return null;
	}
	
}