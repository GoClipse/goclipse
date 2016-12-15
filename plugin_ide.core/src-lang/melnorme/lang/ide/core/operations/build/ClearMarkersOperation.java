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
package melnorme.lang.ide.core.operations.build;

import static java.text.MessageFormat.format;
import static melnorme.lang.ide.core.operations.build.BuildManagerMessages.MSG_ClearingMarkers;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import melnorme.lang.ide.core.EclipseCore;
import melnorme.lang.ide.core.LangCoreMessages;
import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.IToolOperationMonitor;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.lang.tooling.common.ops.IOperationMonitor.IOperationSubMonitor;
import melnorme.lang.tooling.common.ops.Operation;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

public class ClearMarkersOperation implements Operation {
	
	protected final Location location;
	protected final String projectName;
	protected final IToolOperationMonitor toolMonitor;
	
	public ClearMarkersOperation(
		IProject project, IToolOperationMonitor opMonitor
	) throws CommonException {
		this(ResourceUtils.getLocation(project), project.getName(), opMonitor);
	}
	
	public ClearMarkersOperation(
		Location location, String projectName, IToolOperationMonitor toolMonitor
	) {
		this.location = assertNotNull(location);
		this.projectName = assertNotNull(projectName);
		this.toolMonitor = assertNotNull(toolMonitor);
	}
	
	@Override
	public void execute(IOperationMonitor om) throws CommonException, OperationCancellation {
		boolean hadDeletedMarkers = doDeleteProjectMarkers(BuildOperationCreator.buildProblemId, om);
		if(hadDeletedMarkers) {
			toolMonitor.writeInfoMessage(format(MSG_ClearingMarkers, projectName) + "\n");
		}
	}
	
	protected boolean doDeleteProjectMarkers(String markerType, IOperationMonitor parentOM) 
			throws OperationCancellation {
		
		try(IOperationSubMonitor om = parentOM.enterSubTask(LangCoreMessages.BUILD_ClearingProblemMarkers)) {
			
			ArrayList2<IResource> resources = ResourceUtils.getResourcesAt(location);
			for (IResource container : resources) {
			try {
				IMarker[] findMarkers = container.findMarkers(markerType, true, IResource.DEPTH_INFINITE);
				parentOM.checkCancellation();
				if(findMarkers.length != 0) {
					container.deleteMarkers(markerType, true, IResource.DEPTH_INFINITE);
					return true;
				}
			} catch (CoreException ce) {
				EclipseCore.logStatus(ce);
			}
			}
		}
		return false;
	}
	
}