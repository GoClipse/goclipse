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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.misc.MiscUtil;

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
			addParticipants( new ISourceLookupParticipant[]{ new DsfSourceLookupParticipantExtension(session) } );
		}
	}
	
	protected static class DsfSourceLookupParticipantExtension extends DsfSourceLookupParticipant {
		
		protected DsfSourceLookupParticipantExtension(DsfSession session) {
			super(session);
		}
		
		protected static final Pattern CYGDRIVE_PATTERN = Pattern.compile("/cygdrive/([a-zA-Z])/(.*)");
		
		@Override
		public String getSourceName(Object object) throws CoreException {
			String sourceName = super.getSourceName(object);
			if(sourceName != null & MiscUtil.OS_IS_WINDOWS) {
				// Check and fix a potential path issue when using cygwin GDB
				Matcher matcher = CYGDRIVE_PATTERN.matcher(sourceName);
				if(matcher.matches()) {
					sourceName = matcher.group(1) + ":/" + matcher.group(2) ;
				}
			}
			return sourceName;
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
	
	public class LangSourcePathComputer implements ISourcePathComputerDelegate {
		@Override
		public ISourceContainer[] computeSourceContainers(ILaunchConfiguration configuration, IProgressMonitor monitor)
				throws CoreException {
			ISourceContainer[] common = getSourceLookupDirector().getSourceContainers();
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
	
	protected ISourceLookupDirector getSourceLookupDirector() {
		ISourceLookupDirector commonSourceLookupDirector = new AbstractSourceLookupDirector() {
			@Override
			public void initializeParticipants() {
			}
		};
		
		ArrayList2<ISourceContainer> containers = new ArrayList2<>();
		containers.add(new AbsolutePathSourceContainer());
		containers.add(new ProgramRelativePathSourceContainer());
		
		customizeDefaultSourceContainers(containers);
		commonSourceLookupDirector.setSourceContainers(containers.toArray(ISourceContainer.class));
		
		return commonSourceLookupDirector;
	}
	
	@SuppressWarnings("unused") 
	protected void customizeDefaultSourceContainers(ArrayList2<ISourceContainer> containers) {
	}
	
}