/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.tooling.data.IStatusMessage;
import melnorme.lang.tooling.data.StatusException;
import melnorme.lang.tooling.data.StatusLevel;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.misc.ArrayUtil;

public class EclipseUtils {
	
	public static IWorkspaceRoot getWorkspaceRoot() {
		return ResourcesPlugin.getWorkspace().getRoot();
	}
	
	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
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
	
	public static <T> T getAdapter(Object adaptable, Class<T> adapterType) {
		return (T) Platform.getAdapterManager().getAdapter(adaptable, adapterType);
	}
	
	/* ----------------- concurrency ----------------- */
	
	public static void checkMonitorCancelation(IProgressMonitor progressMonitor) throws OperationCancellation {
		if(progressMonitor.isCanceled()) {
			throw new OperationCancellation();
		}
	}
	
	public static void checkMonitorCancelation_OCE(IProgressMonitor progressMonitor) throws OperationCanceledException {
		if(progressMonitor.isCanceled()) {
			throw new OperationCanceledException();
		}
	}
	
	public static int statusLevelToEclipseSeverity(IStatusMessage se) {
		return statusLevelToEclipseSeverity(se.getStatusLevel());
	}
	
	public static int statusLevelToEclipseSeverity(StatusLevel statusLevel) {
		switch(statusLevel) {
		case OK: return IStatus.OK;
		case INFO: return IStatus.INFO;
		case WARNING: return IStatus.WARNING;
		case ERROR: return IStatus.ERROR;
		}
		throw assertUnreachable();
	}
	
	public static StatusLevel eclipseSeverityToStatusLevel(IStatus status) {
		switch(status.getSeverity()) {
		case IStatus.CANCEL: return null;
		case IStatus.OK: return StatusLevel.OK;
		case IStatus.INFO: return StatusLevel.INFO;
		case IStatus.WARNING: return StatusLevel.WARNING;
		case IStatus.ERROR: return StatusLevel.ERROR;
		}
		throw assertUnreachable();
	}
	
	public static StatusException statusToStatusException(IStatus status) {
		return new StatusException(eclipseSeverityToStatusLevel(status), status.getMessage(), status.getException());
	}
	
}