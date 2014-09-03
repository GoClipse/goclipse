package com.googlecode.goclipse.ui.navigator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IContributorResourceAdapter;

import com.googlecode.goclipse.Activator;

public abstract class GoSourceContainer implements IGoSourceContainer, IAdaptable,
IContributorResourceAdapter {

	private IFolder folder;

	protected GoSourceContainer(IFolder folder) {
		this.folder = folder;
	}
	
	public IFolder getFolder() {
		return folder;
	}

	public IProject getProject() {
		return folder.getProject();
	}

	protected abstract Object createChild(IFolder childFolder);
	
	@Override
	public Object[] getChildren() {
		try {
			List<Object> result = new ArrayList<Object>();
			//folder.refreshLocal(3, new NullProgressMonitor());
			if (folder.exists()) {
  				for (IResource res : folder.members()) {
  					if (res instanceof IFolder) {
  						if (!res.getProjectRelativePath().lastSegment().startsWith("_")) {
  							result.add(createChild((IFolder)res));
  						}
  					} else {
  						result.add(res);
  					}
  				}
			}
			return result.toArray();
		} catch (CoreException e) {
			Activator.logError(e);	
			return new Object[0];
		}
	}

	@Override
	public int findMaxProblemSeverity() {
		try {
			return folder.findMaxProblemSeverity(
					IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
		} catch(CoreException e) {
			// ignore
			return -1;
		}
	}

	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == IResource.class) {
			return folder;
		}
		if (adapter == IContributorResourceAdapter.class) {
			return this;
		}
		return folder.getAdapter(adapter);
	}

	@Override
	public IResource getAdaptedResource(IAdaptable adaptable) {
		return folder;
	}

	@Override
	public int hashCode() {
		return folder.hashCode();
	}
	
	@Override
	public String toString() {
		return folder.getName();
	}
	
}
