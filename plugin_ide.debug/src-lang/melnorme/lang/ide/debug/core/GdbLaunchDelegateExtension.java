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
package melnorme.lang.ide.debug.core;

import org.eclipse.cdt.dsf.debug.service.IDsfDebugServicesFactory;
import org.eclipse.cdt.dsf.debug.sourcelookup.DsfSourceLookupDirector;
import org.eclipse.cdt.dsf.debug.sourcelookup.DsfSourceLookupParticipant;
import org.eclipse.cdt.dsf.gdb.launching.GdbLaunchDelegate;
import org.eclipse.cdt.dsf.gdb.launching.LaunchUtils;
import org.eclipse.cdt.dsf.service.DsfSession;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ISourceLocator;
import org.eclipse.debug.core.sourcelookup.ISourceLookupParticipant;

import melnorme.lang.ide.debug.core.services.LangDebugServicesExtensions;

public class GdbLaunchDelegateExtension extends GdbLaunchDelegate {
	
	@Override
	public ILaunch getLaunch(ILaunchConfiguration configuration, String mode) throws CoreException {
		setDefaultProcessFactory(configuration); // Reset process factory to what GdbLaunch expected
		
		ILaunch launch = super.getLaunch(configuration, mode);
		// workaround for DLTK bug: https://bugs.eclipse.org/bugs/show_bug.cgi?id=419273
		launch.setAttribute("org.eclipse.dltk.debug.debugConsole", "false");
		return launch;
	}
	
	@Override
	protected ISourceLocator getSourceLocator(ILaunchConfiguration configuration, DsfSession session)
			throws CoreException {
		return super.getSourceLocator(configuration, session);
	}
	
	@Override
	protected IPath checkBinaryDetails(ILaunchConfiguration config) throws CoreException {
		// Now verify we know the program to debug.
		IPath exePath = LaunchUtils.verifyProgramPath(config, null);
		// Finally, make sure the program is a proper binary.
		
		// BM: this code is disabled because without a project the binary verifier defaults to ELF on any platform
		//LaunchUtils.verifyBinary(config, exePath);
		return exePath;
	}
	
	@Override
	protected DsfSourceLookupDirector createDsfSourceLocator(ILaunchConfiguration lc, DsfSession session)
			throws CoreException {
		DsfSourceLookupDirector sourceLookupDirector = createSourceLookupDirector(lc, session);
		
		sourceLookupDirector.addParticipants(
				new ISourceLookupParticipant[]{ new DsfSourceLookupParticipant(session) } );
		return sourceLookupDirector;
	}
	
	@SuppressWarnings("unused")
	protected LangSourceLookupDirector createSourceLookupDirector(ILaunchConfiguration lc, DsfSession session) {
		return new LangSourceLookupDirector(session);
	}
	
	@Override
	protected IDsfDebugServicesFactory newServiceFactory(ILaunchConfiguration config, String version) {
		IDsfDebugServicesFactory parentServiceFactory = super.newServiceFactory(config, version);
		return createServicesExtensions(parentServiceFactory);
	}
	
	protected LangDebugServicesExtensions createServicesExtensions(IDsfDebugServicesFactory parentServiceFactory) {
		return new LangDebugServicesExtensions(parentServiceFactory);
	}
	
}