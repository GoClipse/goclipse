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
package melnorme.lang.ide.core.operations;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.util.Map;

import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public class CompositeBuildOperation extends CommonBuildOperation {
	
	protected final Indexable<IBuildTargetOperation> operations;
	
	public CompositeBuildOperation(IProject project, LangProjectBuilder langProjectBuilder,
			Indexable<IBuildTargetOperation> operations) {
		super(project, langProjectBuilder);
		this.operations = assertNotNull(operations);
	}
	
	@Override
	public IProject[] execute(IProject project, int kind, Map<String, String> args, IProgressMonitor monitor)
			throws CoreException, CommonException, OperationCancellation {
		
		ArrayList2<IProject> combinedProjects = new ArrayList2<>();
		
		for (IBuildTargetOperation subOperation : operations) {
			IProject[] projects = subOperation.execute(project, kind, args, monitor);
			if(projects != null) {
				combinedProjects.addElements(projects);
			}
		}
		
		return combinedProjects.toArray(IProject.class);
	}
	
}