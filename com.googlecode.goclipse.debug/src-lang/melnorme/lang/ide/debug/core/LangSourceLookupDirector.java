/*******************************************************************************
 * Copyright (c) 2013, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.debug.core;

import org.eclipse.cdt.debug.core.sourcelookup.AbsolutePathSourceContainer;
import org.eclipse.cdt.debug.core.sourcelookup.ProgramRelativePathSourceContainer;
import org.eclipse.cdt.dsf.debug.sourcelookup.DsfSourceLookupDirector;
import org.eclipse.cdt.dsf.debug.sourcelookup.DsfSourceLookupParticipant;
import org.eclipse.cdt.dsf.service.DsfSession;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.sourcelookup.AbstractSourceLookupDirector;
import org.eclipse.debug.core.sourcelookup.ISourceContainer;
import org.eclipse.debug.core.sourcelookup.ISourceContainerType;
import org.eclipse.debug.core.sourcelookup.ISourceLookupDirector;
import org.eclipse.debug.core.sourcelookup.ISourceLookupParticipant;
import org.eclipse.debug.core.sourcelookup.ISourcePathComputer;
import org.eclipse.debug.core.sourcelookup.ISourcePathComputerDelegate;

/**
 * This class removes some CDT-specific lookup mechanisms
 */
public class LangSourceLookupDirector extends DsfSourceLookupDirector {
	
	protected DsfSession session;
	
	/** This should be used only for configuring containers and saving a memento. 
	 * Otherwise, for full use,  a session must be provided. */
	public LangSourceLookupDirector() {
		this(null);
	}
	
	public LangSourceLookupDirector(DsfSession session) {
		super(session);
		this.session = session;
	}
	
	@Override
	public void initializeParticipants() {
		// Do not use CSourceLoookupDirector
		if(session != null) {
			addParticipants( new ISourceLookupParticipant[]{ new DsfSourceLookupParticipant(session) } );
		}
	}
	
	@Override
	public ISourcePathComputer getSourcePathComputer() {
		ISourcePathComputer sourcePathComputer = super.getSourcePathComputer();
		if(sourcePathComputer != null) {
			return sourcePathComputer;
		}
		
		return new ISourcePathComputer() {
			
			LangSourcePathComputer langSourcePathComputer = new LangSourcePathComputer();
			
			@Override
			public ISourceContainer[] computeSourceContainers(ILaunchConfiguration configuration, IProgressMonitor monitor)
					throws CoreException {
				return langSourcePathComputer.computeSourceContainers(configuration, monitor);
			}
			
			@Override
			public String getId() {
				return LangDebug.LANG_SOURCE_LOOKUP_DIRECTOR;
			}
		};
	} 
	
	public static class LangSourcePathComputer implements ISourcePathComputerDelegate {
		@Override
		public ISourceContainer[] computeSourceContainers(ILaunchConfiguration configuration, IProgressMonitor monitor)
				throws CoreException {
			ISourceContainer[] common = getCommonSourceLookupDirector().getSourceContainers();
			ISourceContainer[] containers = new ISourceContainer[common.length];
			
			for (int i = 0; i < common.length; i++) {
				ISourceContainer container = common[i];
				ISourceContainerType type = container.getType();
				// Clone the container to make sure that the original can be safely disposed.
				container = type.createSourceContainer(type.getMemento(container));
				containers[i] = container;
			}
			return containers;
		}

	}
	
	protected static ISourceLookupDirector commonSourceLookupDirector;
	
	protected static synchronized ISourceLookupDirector getCommonSourceLookupDirector() {
		if(commonSourceLookupDirector == null) {
			commonSourceLookupDirector = new AbstractSourceLookupDirector() {
				@Override
				public void initializeParticipants() {
				}
			};
			
			ISourceContainer[] containers = new ISourceContainer[2];
			containers[0] = new AbsolutePathSourceContainer();
			containers[1] = new ProgramRelativePathSourceContainer();
			commonSourceLookupDirector.setSourceContainers(containers);
		}
		
		return commonSourceLookupDirector;
	}
}