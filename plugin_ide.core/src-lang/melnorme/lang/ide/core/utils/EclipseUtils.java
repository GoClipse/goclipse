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
package melnorme.lang.ide.core.utils;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertUnreachable;

import java.util.ArrayList;
import java.util.List;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.tooling.data.StatusException;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.ArrayUtil;
import melnorme.utilbox.misc.Location;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

public class EclipseUtils extends ResourceUtils {
	
	public static Location location(java.nio.file.Path path) throws CoreException {
		try {
			return Location.create2(path);
		} catch (CommonException ce) {
			throw LangCore.createCoreException(ce);
		}
	}
	
	/** Convenience method to get the WorkspaceRoot. */
	public static IWorkspaceRoot getWorkspaceRoot() {
		return ResourcesPlugin.getWorkspace().getRoot();
	}
	
	@Deprecated
	public static Path path(java.nio.file.Path path) {
		return epath(path);
	}
	
	public static void startOtherPlugin(String pluginId) {
		try {
			Bundle debugPlugin = Platform.getBundle(pluginId);
			if(debugPlugin != null && debugPlugin.getState() != Bundle.STARTING) {
				debugPlugin.start(Bundle.START_TRANSIENT);
			}
		} catch (BundleException e) {
			LangCore.logError("Error trying to start plugin: " + pluginId, e);
		}
	}
	
	public static IProject[] getOpenedProjects(String natureId) throws CoreException {
		final List<IProject> result = new ArrayList<IProject>();
		
		final IProject[] projects = getWorkspaceRoot().getProjects();
		for (IProject project : projects) {
			if (project.exists() && project.isOpen() && (natureId == null || project.hasNature(natureId))) {
				result.add(project);
			}
		}
		
		return ArrayUtil.createFrom(result, IProject.class);
	}
	
	/** Adds a nature to the given project if it doesn't exist already. */
	public static void addNature(IProject project, String natureID) throws CoreException {
		IProjectDescription description = project.getDescription();
		String[] natures = description.getNatureIds();
		if(ArrayUtil.contains(natures, natureID))
			return;
		
		String[] newNatures = ArrayUtil.append(natures, natureID);
		description.setNatureIds(newNatures);
		project.setDescription(description, null);
	}
	
	/** Remove nature from given project, if project has given nature. */
	public static void removeNature(IProject project, String natureID) throws CoreException {
		IProjectDescription description = project.getDescription();
		String[] natures = description.getNatureIds();
		
		String[] newNatures = ArrayUtil.remove(natures, natureID);
		if(newNatures != natures) {
			description.setNatureIds(newNatures);
			project.setDescription(description, null);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getAdapter(Object adaptable, Class<T> adapterType) {
		return (T) Platform.getAdapterManager().getAdapter(adaptable, adapterType);
	}
	
	/* ----------------- concurrency ----------------- */
	
	public static void checkMonitorCancelation(IProgressMonitor progressMonitor) throws OperationCancellation {
		if(progressMonitor.isCanceled()) {
			throw new OperationCancellation();
		}
	}
	
	public static int statusLevelToEclipseSeverity(StatusException se) {
		switch (se.getStatusLevel()) {
		case OK: return IStatus.OK;
		case INFO: return IStatus.INFO;
		case WARNING: return IStatus.WARNING;
		case ERROR: return IStatus.ERROR;
		}
		throw assertUnreachable();
	}
	
}