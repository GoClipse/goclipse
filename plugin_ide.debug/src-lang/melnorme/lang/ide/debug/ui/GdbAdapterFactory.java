/*******************************************************************************
 * Copyright (c) 2006, 2013 Wind River Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Wind River Systems - initial API and implementation
 *     Navid Mehregani (TI) - Bug 289526 - Migrate the Restart feature to the new one, as supported by the platform
 *     Patrick Chuong (Texas Instruments) - Add support for icon overlay in the debug view (Bug 334566)
 *     Alvaro Sanchez-Leon (Ericsson AB) - Support for Step into selection (bug 244865)
 *******************************************************************************/
package melnorme.lang.ide.debug.ui;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import melnorme.lang.ide.debug.ui.viewmodel.GdbViewModelAdapter;

import org.eclipse.cdt.debug.core.model.ICBreakpoint;
import org.eclipse.cdt.debug.core.model.IConnectHandler;
import org.eclipse.cdt.debug.core.model.IDebugNewExecutableHandler;
import org.eclipse.cdt.debug.core.model.IResumeWithoutSignalHandler;
import org.eclipse.cdt.debug.core.model.IReverseResumeHandler;
import org.eclipse.cdt.debug.core.model.IReverseStepIntoHandler;
import org.eclipse.cdt.debug.core.model.IReverseStepOverHandler;
import org.eclipse.cdt.debug.core.model.IReverseToggleHandler;
import org.eclipse.cdt.debug.core.model.ISaveTraceDataHandler;
import org.eclipse.cdt.debug.core.model.IStartTracingHandler;
import org.eclipse.cdt.debug.core.model.IStepIntoSelectionHandler;
import org.eclipse.cdt.debug.core.model.ISteppingModeTarget;
import org.eclipse.cdt.debug.core.model.IStopTracingHandler;
import org.eclipse.cdt.debug.core.model.IUncallHandler;
import org.eclipse.cdt.debug.ui.IPinProvider;
import org.eclipse.cdt.dsf.concurrent.Immutable;
import org.eclipse.cdt.dsf.concurrent.ThreadSafe;
import org.eclipse.cdt.dsf.debug.ui.actions.DsfResumeCommand;
import org.eclipse.cdt.dsf.debug.ui.actions.DsfStepIntoCommand;
import org.eclipse.cdt.dsf.debug.ui.actions.DsfStepIntoSelectionCommand;
import org.eclipse.cdt.dsf.debug.ui.actions.DsfStepOverCommand;
import org.eclipse.cdt.dsf.debug.ui.actions.DsfStepReturnCommand;
import org.eclipse.cdt.dsf.debug.ui.actions.DsfSuspendCommand;
import org.eclipse.cdt.dsf.debug.ui.sourcelookup.DsfSourceDisplayAdapter;
import org.eclipse.cdt.dsf.debug.ui.viewmodel.SteppingController;
import org.eclipse.cdt.dsf.debug.ui.viewmodel.actions.DefaultRefreshAllTarget;
import org.eclipse.cdt.dsf.debug.ui.viewmodel.actions.IRefreshAllTarget;
import org.eclipse.cdt.dsf.debug.ui.viewmodel.launch.DefaultDsfModelSelectionPolicyFactory;
import org.eclipse.cdt.dsf.gdb.internal.commands.ISelectNextTraceRecordHandler;
import org.eclipse.cdt.dsf.gdb.internal.commands.ISelectPrevTraceRecordHandler;
import org.eclipse.cdt.dsf.gdb.internal.ui.GdbDebugTextHover;
import org.eclipse.cdt.dsf.gdb.internal.ui.GdbPinProvider;
import org.eclipse.cdt.dsf.gdb.internal.ui.GdbSuspendTrigger;
import org.eclipse.cdt.dsf.gdb.internal.ui.actions.DsfTerminateCommand;
import org.eclipse.cdt.dsf.gdb.internal.ui.actions.GdbDisconnectCommand;
import org.eclipse.cdt.dsf.gdb.internal.ui.actions.GdbRestartCommand;
import org.eclipse.cdt.dsf.gdb.internal.ui.actions.GdbSteppingModeTarget;
import org.eclipse.cdt.dsf.gdb.internal.ui.commands.GdbConnectCommand;
import org.eclipse.cdt.dsf.gdb.internal.ui.commands.GdbDebugNewExecutableCommand;
import org.eclipse.cdt.dsf.gdb.internal.ui.commands.GdbResumeWithoutSignalCommand;
import org.eclipse.cdt.dsf.gdb.internal.ui.commands.GdbReverseResumeCommand;
import org.eclipse.cdt.dsf.gdb.internal.ui.commands.GdbReverseStepIntoCommand;
import org.eclipse.cdt.dsf.gdb.internal.ui.commands.GdbReverseStepOverCommand;
import org.eclipse.cdt.dsf.gdb.internal.ui.commands.GdbReverseToggleCommand;
import org.eclipse.cdt.dsf.gdb.internal.ui.commands.GdbSaveTraceDataCommand;
import org.eclipse.cdt.dsf.gdb.internal.ui.commands.GdbSelectNextTraceRecordCommand;
import org.eclipse.cdt.dsf.gdb.internal.ui.commands.GdbSelectPrevTraceRecordCommand;
import org.eclipse.cdt.dsf.gdb.internal.ui.commands.GdbStartTracingCommand;
import org.eclipse.cdt.dsf.gdb.internal.ui.commands.GdbStopTracingCommand;
import org.eclipse.cdt.dsf.gdb.internal.ui.commands.GdbUncallCommand;
import org.eclipse.cdt.dsf.gdb.launching.GdbLaunch;
import org.eclipse.cdt.dsf.gdb.launching.GdbLaunchDelegate;
import org.eclipse.cdt.dsf.service.DsfSession;
import org.eclipse.cdt.ui.text.c.hover.ICEditorTextHover;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchesListener2;
import org.eclipse.debug.core.commands.IDisconnectHandler;
import org.eclipse.debug.core.commands.IRestartHandler;
import org.eclipse.debug.core.commands.IResumeHandler;
import org.eclipse.debug.core.commands.IStepIntoHandler;
import org.eclipse.debug.core.commands.IStepOverHandler;
import org.eclipse.debug.core.commands.IStepReturnHandler;
import org.eclipse.debug.core.commands.ISuspendHandler;
import org.eclipse.debug.core.commands.ITerminateHandler;
import org.eclipse.debug.core.model.IDebugModelProvider;
import org.eclipse.debug.core.sourcelookup.ISourceLookupDirector;
import org.eclipse.debug.internal.ui.viewers.model.provisional.IColumnPresentationFactory;
import org.eclipse.debug.internal.ui.viewers.model.provisional.IElementContentProvider;
import org.eclipse.debug.internal.ui.viewers.model.provisional.IModelProxyFactory;
import org.eclipse.debug.internal.ui.viewers.model.provisional.IModelSelectionPolicyFactory;
import org.eclipse.debug.internal.ui.viewers.model.provisional.IViewerInputProvider;
import org.eclipse.debug.ui.contexts.ISuspendTrigger;
import org.eclipse.debug.ui.sourcelookup.ISourceDisplay;

/**
 * This implementation of platform adapter factory only retrieves the adapters
 * for the launch object.  But it also manages the creation and destruction
 * of the session-based adapters which are returned by the
 * IDMContext.getAdapter() methods.
 */
@ThreadSafe
public class GdbAdapterFactory
    implements IAdapterFactory, ILaunchesListener2
{
    @Immutable
    protected class SessionAdapterSet {
        final GdbLaunch fLaunch;
        final GdbViewModelAdapter fViewModelAdapter;
        final DsfSourceDisplayAdapter fSourceDisplayAdapter;
        final DsfStepIntoCommand fStepIntoCommand;
        final DsfStepIntoSelectionCommand fStepIntoSelectionCommand;
        final GdbReverseStepIntoCommand fReverseStepIntoCommand;
        final DsfStepOverCommand fStepOverCommand;
        final GdbReverseStepOverCommand fReverseStepOverCommand;
        final DsfStepReturnCommand fStepReturnCommand;
        final GdbUncallCommand fUncallCommand;
        final DsfSuspendCommand fSuspendCommand;
        final DsfResumeCommand fResumeCommand;
        final GdbReverseResumeCommand fReverseResumeCommand;
        final GdbResumeWithoutSignalCommand fResumeWithoutSignalCommand;
        final GdbRestartCommand fRestartCommand;
        final DsfTerminateCommand fTerminateCommand;
        final GdbDebugNewExecutableCommand fDebugNewExecutableCommand;
        final GdbConnectCommand fConnectCommand;
        final GdbDisconnectCommand fDisconnectCommand;
        final IDebugModelProvider fDebugModelProvider;
        final GdbSuspendTrigger fSuspendTrigger;
		final GdbSteppingModeTarget fSteppingModeTarget;
		final IModelSelectionPolicyFactory fModelSelectionPolicyFactory;
		final SteppingController fSteppingController;
        final DefaultRefreshAllTarget fRefreshAllTarget;
        final GdbReverseToggleCommand fReverseToggleTarget;
        final GdbStartTracingCommand fStartTracingTarget;
        final GdbStopTracingCommand fStopTracingTarget;
        final GdbSaveTraceDataCommand fSaveTraceDataTarget;
        final GdbSelectNextTraceRecordCommand fSelectNextRecordTarget;
        final GdbSelectPrevTraceRecordCommand fSelectPrevRecordTarget;
        final GdbDebugTextHover fDebugTextHover;
        final GdbPinProvider fPinProvider;
        
        SessionAdapterSet(GdbLaunch launch) {
            fLaunch = launch;
            DsfSession session = launch.getSession();
            
            // register stepping controller
            fSteppingController = new SteppingController(session);
            session.registerModelAdapter(SteppingController.class, fSteppingController);

            fViewModelAdapter = createGdbViewModelAdapter(session, fSteppingController);
            session.registerModelAdapter(IViewerInputProvider.class, fViewModelAdapter);
            
            if (launch.getSourceLocator() instanceof ISourceLookupDirector) {
                fSourceDisplayAdapter = new DsfSourceDisplayAdapter(session, (ISourceLookupDirector)launch.getSourceLocator(), fSteppingController);
            } else {
                fSourceDisplayAdapter = null;
            }
            session.registerModelAdapter(ISourceDisplay.class, fSourceDisplayAdapter);
            
            fSteppingModeTarget = new GdbSteppingModeTarget(session);
            fStepIntoCommand = new DsfStepIntoCommand(session, fSteppingModeTarget);
            fStepIntoSelectionCommand = new DsfStepIntoSelectionCommand(session);
            fReverseStepIntoCommand = new GdbReverseStepIntoCommand(session, fSteppingModeTarget);
            fStepOverCommand = new DsfStepOverCommand(session, fSteppingModeTarget);
            fReverseStepOverCommand = new GdbReverseStepOverCommand(session, fSteppingModeTarget);
            fStepReturnCommand = new DsfStepReturnCommand(session);
            fUncallCommand = new GdbUncallCommand(session, fSteppingModeTarget);
            fSuspendCommand = new DsfSuspendCommand(session);
            fResumeCommand = new DsfResumeCommand(session);
            fReverseResumeCommand = new GdbReverseResumeCommand(session);
            fResumeWithoutSignalCommand = new GdbResumeWithoutSignalCommand(session);
            fRestartCommand = new GdbRestartCommand(session, fLaunch);
            fTerminateCommand = new DsfTerminateCommand(session);
            fDebugNewExecutableCommand = new GdbDebugNewExecutableCommand(session, fLaunch);
            fConnectCommand = new GdbConnectCommand(session, fLaunch);
            fDisconnectCommand = new GdbDisconnectCommand(session);
            fSuspendTrigger = new GdbSuspendTrigger(session, fLaunch);
            fModelSelectionPolicyFactory = new DefaultDsfModelSelectionPolicyFactory();
            fRefreshAllTarget = new DefaultRefreshAllTarget();
            fReverseToggleTarget = new GdbReverseToggleCommand(session);
            fStartTracingTarget = new GdbStartTracingCommand(session);
            fStopTracingTarget = new GdbStopTracingCommand(session);
            fSaveTraceDataTarget = new GdbSaveTraceDataCommand(session);
            fSelectNextRecordTarget = new GdbSelectNextTraceRecordCommand(session);
            fSelectPrevRecordTarget = new GdbSelectPrevTraceRecordCommand(session);
            fPinProvider = new GdbPinProvider(session);

            session.registerModelAdapter(ISteppingModeTarget.class, fSteppingModeTarget);
            session.registerModelAdapter(IStepIntoHandler.class, fStepIntoCommand);
            session.registerModelAdapter(IStepIntoSelectionHandler.class, fStepIntoSelectionCommand);
            session.registerModelAdapter(IReverseStepIntoHandler.class, fReverseStepIntoCommand);
            session.registerModelAdapter(IStepOverHandler.class, fStepOverCommand);
            session.registerModelAdapter(IReverseStepOverHandler.class, fReverseStepOverCommand);
            session.registerModelAdapter(IStepReturnHandler.class, fStepReturnCommand);
            session.registerModelAdapter(IUncallHandler.class, fUncallCommand);
            session.registerModelAdapter(ISuspendHandler.class, fSuspendCommand);
            session.registerModelAdapter(IResumeHandler.class, fResumeCommand);
            session.registerModelAdapter(IReverseResumeHandler.class, fReverseResumeCommand);
            session.registerModelAdapter(IResumeWithoutSignalHandler.class, fResumeWithoutSignalCommand);
            session.registerModelAdapter(IRestartHandler.class, fRestartCommand);
            session.registerModelAdapter(ITerminateHandler.class, fTerminateCommand);
            session.registerModelAdapter(IConnectHandler.class, fConnectCommand);
            session.registerModelAdapter(IDebugNewExecutableHandler.class, fDebugNewExecutableCommand);
            session.registerModelAdapter(IDisconnectHandler.class, fDisconnectCommand);
            session.registerModelAdapter(IModelSelectionPolicyFactory.class, fModelSelectionPolicyFactory);
            session.registerModelAdapter(IRefreshAllTarget.class, fRefreshAllTarget);
            session.registerModelAdapter(IReverseToggleHandler.class, fReverseToggleTarget);
            session.registerModelAdapter(IStartTracingHandler.class, fStartTracingTarget);
            session.registerModelAdapter(IStopTracingHandler.class, fStopTracingTarget);
            session.registerModelAdapter(ISaveTraceDataHandler.class, fSaveTraceDataTarget);
            session.registerModelAdapter(ISelectNextTraceRecordHandler.class, fSelectNextRecordTarget);
            session.registerModelAdapter(ISelectPrevTraceRecordHandler.class, fSelectPrevRecordTarget);
            session.registerModelAdapter(IPinProvider.class, fPinProvider);

            fDebugModelProvider = new IDebugModelProvider() {
                // @see org.eclipse.debug.core.model.IDebugModelProvider#getModelIdentifiers()
                @Override
                public String[] getModelIdentifiers() {
                    return new String[] { GdbLaunchDelegate.GDB_DEBUG_MODEL_ID, ICBreakpoint.C_BREAKPOINTS_DEBUG_MODEL_ID, "org.eclipse.cdt.gdb" }; //$NON-NLS-1$
                }
            };
            session.registerModelAdapter(IDebugModelProvider.class, fDebugModelProvider);

            /*
             * Registering the launch as an adapter, ensures that this launch,
             * and debug model ID will be associated with all DMContexts from this
             * session.
             */
            session.registerModelAdapter(ILaunch.class, fLaunch);
            
            /*
             * Register debug hover adapter (bug 309001).
             */
            fDebugTextHover = new GdbDebugTextHover();
            session.registerModelAdapter(ICEditorTextHover.class, fDebugTextHover);
        }
        
        void dispose() {
            DsfSession session = fLaunch.getSession();
            
            fViewModelAdapter.dispose();
            session.unregisterModelAdapter(IViewerInputProvider.class);

            session.unregisterModelAdapter(ISourceDisplay.class);
            if (fSourceDisplayAdapter != null) fSourceDisplayAdapter.dispose();

            session.unregisterModelAdapter(SteppingController.class);
            fSteppingController.dispose();

            session.unregisterModelAdapter(ISteppingModeTarget.class);
            session.unregisterModelAdapter(IStepIntoHandler.class);
            session.unregisterModelAdapter(IStepIntoSelectionHandler.class);
            session.unregisterModelAdapter(IReverseStepIntoHandler.class);
            session.unregisterModelAdapter(IStepOverHandler.class);
            session.unregisterModelAdapter(IReverseStepOverHandler.class);
            session.unregisterModelAdapter(IStepReturnHandler.class);
            session.unregisterModelAdapter(IUncallHandler.class);
            session.unregisterModelAdapter(ISuspendHandler.class);
            session.unregisterModelAdapter(IResumeHandler.class);
            session.unregisterModelAdapter(IReverseResumeHandler.class);
            session.unregisterModelAdapter(IResumeWithoutSignalHandler.class);
            session.unregisterModelAdapter(IRestartHandler.class);
            session.unregisterModelAdapter(ITerminateHandler.class);
            session.unregisterModelAdapter(IConnectHandler.class);
            session.unregisterModelAdapter(IDebugNewExecutableHandler.class);
            session.unregisterModelAdapter(IDisconnectHandler.class);
            session.unregisterModelAdapter(IModelSelectionPolicyFactory.class);
            session.unregisterModelAdapter(IRefreshAllTarget.class);
            session.unregisterModelAdapter(IReverseToggleHandler.class);
            session.unregisterModelAdapter(IStartTracingHandler.class);
            session.unregisterModelAdapter(IStopTracingHandler.class);
            session.unregisterModelAdapter(ISaveTraceDataHandler.class);
            session.unregisterModelAdapter(ISelectNextTraceRecordHandler.class);
            session.unregisterModelAdapter(ISelectPrevTraceRecordHandler.class);
            session.unregisterModelAdapter(IPinProvider.class);
            
            session.unregisterModelAdapter(IDebugModelProvider.class);
            session.unregisterModelAdapter(ILaunch.class);

            session.unregisterModelAdapter(ICEditorTextHover.class);

            fSteppingModeTarget.dispose();
            fStepIntoCommand.dispose();
            fStepIntoSelectionCommand.dispose();
            fReverseStepIntoCommand.dispose();
            fStepOverCommand.dispose();
            fReverseStepOverCommand.dispose();
            fStepReturnCommand.dispose();
            fUncallCommand.dispose();
            fSuspendCommand.dispose();
            fResumeCommand.dispose();
            fReverseResumeCommand.dispose();
            fResumeWithoutSignalCommand.dispose();
            fRestartCommand.dispose();
            fTerminateCommand.dispose();
            fConnectCommand.dispose();
            fDebugNewExecutableCommand.dispose();
            fDisconnectCommand.dispose();
            fSuspendTrigger.dispose();
            fReverseToggleTarget.dispose();
            fStartTracingTarget.dispose();
            fStopTracingTarget.dispose();
            fSaveTraceDataTarget.dispose();
            fSelectNextRecordTarget.dispose();
            fSelectPrevRecordTarget.dispose();
            fPinProvider.dispose();
        }
    }

    /**
     * Active adapter sets.  They are accessed using the launch instance 
     * which owns the debug services session. 
     */
    private static Map<GdbLaunch, SessionAdapterSet> fgLaunchAdapterSets =
        Collections.synchronizedMap(new HashMap<GdbLaunch, SessionAdapterSet>());

    /**
     * Map of launches for which adapter sets have already been disposed.
     * This map (used as a set) is maintained in order to avoid re-creating an 
     * adapter set after the launch was removed from the launch manager, but 
     * while the launch is still being held by other classes which may 
     * request its adapters.  A weak map is used to avoid leaking 
     * memory once the launches are no longer referenced.
     * <p>
     * Access to this map is synchronized using the fgLaunchAdapterSets 
     * instance.
     * </p>
     */
    private static Map<ILaunch, SessionAdapterSet> fgDisposedLaunchAdapterSets =
        new WeakHashMap<ILaunch, SessionAdapterSet>();

	static void disposeAdapterSet(ILaunch launch) {
		synchronized(fgLaunchAdapterSets) {
		    if ( fgLaunchAdapterSets.containsKey(launch) ) {
		        fgLaunchAdapterSets.remove(launch).dispose();
		        fgDisposedLaunchAdapterSets.put(launch, null);
		    }
		}
	}

    public GdbAdapterFactory() {
        DebugPlugin.getDefault().getLaunchManager().addLaunchListener(this);
    }

    /**
     * This method only actually returns adapters for the launch object.
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Object getAdapter(Object adaptableObject, Class adapterType) {
        if (!(adaptableObject instanceof GdbLaunch)) return null;

        GdbLaunch launch = (GdbLaunch)adaptableObject;

        // Check for valid session.  
        // Note: even if the session is no longer active, the adapter set 
        // should still be returned.  This is because the view model may still
        // need to show elements representing a terminated process/thread/etc.
        DsfSession session = launch.getSession();
        if (session == null) return null;

        // Find the correct set of adapters based on the launch session-ID.  If not found
        // it means that we have a new launch and new session, and we have to create a
        // new set of adapters.

        SessionAdapterSet adapterSet;
        synchronized(fgLaunchAdapterSets) {
            // The adapter set for the given launch was already disposed.  
            // Return a null adapter.
            if (fgDisposedLaunchAdapterSets.containsKey(launch)) {
                return null;
            }
            adapterSet = fgLaunchAdapterSets.get(launch);
            if (adapterSet == null) {
            	// If the first time we attempt to create an adapterSet is once the session is
            	// already inactive, we should not create it and return null.
            	// This can happen, for example, when we run JUnit tests and we don't actually
            	// have a need for any adapters until the launch is actually being removed.
            	// Note that we must do this here because fgDisposedLaunchAdapterSets
            	// may not already know that the launch has been removed because of a race
            	// condition with the caller which is also processing a launchRemoved method.
            	// Bug 334687 
            	if (session.isActive() == false) {
            		return null;
            	}
                adapterSet = new SessionAdapterSet(launch);
                fgLaunchAdapterSets.put(launch, adapterSet);
            }
        }
        
        // Returns the adapter type for the launch object.
        if (adapterType.equals(IElementContentProvider.class)) return adapterSet.fViewModelAdapter;
        else if (adapterType.equals(IModelProxyFactory.class)) return adapterSet.fViewModelAdapter;
        else if (adapterType.equals(IColumnPresentationFactory.class)) return adapterSet.fViewModelAdapter;
        else if (adapterType.equals(ISuspendTrigger.class)) return adapterSet.fSuspendTrigger;
        else return null;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class[] getAdapterList() {
        return new Class[] {
            IElementContentProvider.class, IModelProxyFactory.class, ISuspendTrigger.class,
            IColumnPresentationFactory.class,
            };
    }

    @Override
    public void launchesRemoved(ILaunch[] launches) {
        // Dispose the set of adapters for a launch only after the launch is
        // removed.
        for (ILaunch launch : launches) {
            if (launch instanceof GdbLaunch) {
                disposeAdapterSet(launch);
            }
        }
    }

    @Override
    public void launchesTerminated(ILaunch[] launches) {
    }

    @Override
    public void launchesAdded(ILaunch[] launches) {
    }
    
    @Override
    public void launchesChanged(ILaunch[] launches) {
    }
    
	protected GdbViewModelAdapter createGdbViewModelAdapter(DsfSession session, SteppingController steppingController) {
		return new GdbViewModelAdapter(session, steppingController);
	}
    
}