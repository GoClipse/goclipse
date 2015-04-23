package melnorme.lang.ide.debug.core;

import melnorme.lang.ide.debug.core.services.DebugServicesExtensions;

import org.eclipse.cdt.dsf.debug.service.IDsfDebugServicesFactory;
import org.eclipse.cdt.dsf.debug.service.IExpressions;
import org.eclipse.cdt.dsf.debug.sourcelookup.DsfSourceLookupDirector;
import org.eclipse.cdt.dsf.debug.sourcelookup.DsfSourceLookupParticipant;
import org.eclipse.cdt.dsf.gdb.launching.GdbLaunchDelegate;
import org.eclipse.cdt.dsf.gdb.launching.LaunchUtils;
import org.eclipse.cdt.dsf.gdb.service.GdbDebugServicesFactory;
import org.eclipse.cdt.dsf.gdb.service.GdbDebugServicesFactoryNS;
import org.eclipse.cdt.dsf.gdb.service.macos.MacOSGdbDebugServicesFactory;
import org.eclipse.cdt.dsf.service.DsfSession;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ISourceLocator;
import org.eclipse.debug.core.sourcelookup.ISourceLookupParticipant;

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
	protected DsfSourceLookupDirector createDsfSourceLocator(ILaunchConfiguration configuration, DsfSession session)
			throws CoreException {
		DsfSourceLookupDirector sourceLookupDirector = new LangSourceLookupDirector(session);
		
		sourceLookupDirector.addParticipants(
				new ISourceLookupParticipant[]{ new DsfSourceLookupParticipant(session) } );
		return sourceLookupDirector;
	}
	
	@Override
	protected IDsfDebugServicesFactory newServiceFactory(ILaunchConfiguration config, String version) {
		boolean fIsNonStopSession = LaunchUtils.getIsNonStopMode(config);
		
		if (fIsNonStopSession && isNonStopSupportedInGdbVersion(version)) {
			return new GdbDebugServicesFactoryNS_LangExtension(version);
		}
		
		if (version.contains(LaunchUtils.MACOS_GDB_MARKER)) {
			// The version string at this point should look like
			// 6.3.50-20050815APPLE1346, we extract the gdb version and apple version
			String versions [] = version.split(LaunchUtils.MACOS_GDB_MARKER);
			if (versions.length == 2) {
				return new MacOSGdbDebugServicesFactory_LangExtension(versions[0], versions[1]);
			}
		}
		
		return new GdbDebugServicesFactory_LangExtension(version);
	}
	
	protected final DebugServicesExtensions servicesExtensions = createServicesExtensions();
	
	protected DebugServicesExtensions createServicesExtensions() {
		return new DebugServicesExtensions();
	}
	
	protected class GdbDebugServicesFactoryNS_LangExtension extends GdbDebugServicesFactoryNS {
		
		public GdbDebugServicesFactoryNS_LangExtension(String version) {
			super(version);
		}
		
		@Override
		protected IExpressions createExpressionService(DsfSession session) {
			// See super.createExpressionService(session);
			return servicesExtensions.createExpressionService(session);
		}
	}
	
	protected class GdbDebugServicesFactory_LangExtension extends GdbDebugServicesFactory {
		public GdbDebugServicesFactory_LangExtension(String version) {
			super(version);
		}
		
		@Override
		protected IExpressions createExpressionService(DsfSession session) {
			// See super.createExpressionService(session);
			return servicesExtensions.createExpressionService(session);
		}
	}
	
	protected class MacOSGdbDebugServicesFactory_LangExtension extends MacOSGdbDebugServicesFactory {
		public MacOSGdbDebugServicesFactory_LangExtension(String gdbVersion, String appleVersion) {
			super(gdbVersion, appleVersion);
		}
		
		@Override
		protected IExpressions createExpressionService(DsfSession session) {
			// See super.createExpressionService(session);
			return servicesExtensions.createExpressionService(session);
		}
	}
	
}