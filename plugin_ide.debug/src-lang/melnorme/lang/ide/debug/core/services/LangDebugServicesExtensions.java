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
package melnorme.lang.ide.debug.core.services;

import java.util.Map;

import org.eclipse.cdt.dsf.concurrent.DataRequestMonitor;
import org.eclipse.cdt.dsf.concurrent.RequestMonitorWithProgress;
import org.eclipse.cdt.dsf.concurrent.Sequence;
import org.eclipse.cdt.dsf.datamodel.DMContexts;
import org.eclipse.cdt.dsf.debug.service.IDsfDebugServicesFactory;
import org.eclipse.cdt.dsf.debug.service.IExpressions;
import org.eclipse.cdt.dsf.debug.service.command.ICommandControl;
import org.eclipse.cdt.dsf.gdb.internal.GdbPlugin;
import org.eclipse.cdt.dsf.gdb.launching.FinalLaunchSequence_7_7;
import org.eclipse.cdt.dsf.gdb.service.GDBBackend;
import org.eclipse.cdt.dsf.gdb.service.GDBPatternMatchingExpressions;
import org.eclipse.cdt.dsf.gdb.service.GdbDebugServicesFactory;
import org.eclipse.cdt.dsf.gdb.service.command.CommandFactory_6_8;
import org.eclipse.cdt.dsf.gdb.service.command.GDBControl;
import org.eclipse.cdt.dsf.gdb.service.command.GDBControl_7_7;
import org.eclipse.cdt.dsf.mi.service.IMIBackend;
import org.eclipse.cdt.dsf.mi.service.IMIExpressions;
import org.eclipse.cdt.dsf.mi.service.MIExpressions;
import org.eclipse.cdt.dsf.mi.service.MIVariableManager;
import org.eclipse.cdt.dsf.mi.service.command.CommandFactory;
import org.eclipse.cdt.dsf.mi.service.command.commands.ExprMetaGetValue;
import org.eclipse.cdt.dsf.mi.service.command.output.ExprMetaGetValueInfo;
import org.eclipse.cdt.dsf.mi.service.command.output.MIDataEvaluateExpressionInfo;
import org.eclipse.cdt.dsf.service.DsfServicesTracker;
import org.eclipse.cdt.dsf.service.DsfSession;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunchConfiguration;

import melnorme.utilbox.misc.ReflectionUtils;

public class LangDebugServicesExtensions implements IDsfDebugServicesFactory {
	
	protected final IDsfDebugServicesFactory parentServiceFactory;
	
	public LangDebugServicesExtensions(IDsfDebugServicesFactory parentServiceFactory) {
		this.parentServiceFactory = parentServiceFactory;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <V> V createService(Class<V> clazz, DsfSession session, Object... optionalArguments) {
		
		if(IExpressions.class.isAssignableFrom(clazz)) {
			return (V) createExpressionService(session);
		}
		
		if(IMIBackend.class.isAssignableFrom(clazz)) {
			for (Object arg : optionalArguments) {
				if (arg instanceof ILaunchConfiguration) {
					return (V) createBackendGDBService(session, (ILaunchConfiguration) arg);
				}
			}
		}
		
		if(ICommandControl.class.isAssignableFrom(clazz)) {
			for(Object arg : optionalArguments) {
				if(arg instanceof ILaunchConfiguration) {
					ILaunchConfiguration config = (ILaunchConfiguration) arg;
					
					if(parentServiceFactory instanceof GdbDebugServicesFactory) {
						GdbDebugServicesFactory gdbSvcFactory = (GdbDebugServicesFactory) parentServiceFactory;
						
						GDBControl gdbControl = getGdbControl_override(session, config, gdbSvcFactory);
						if(gdbControl != null) {
							return (V) gdbControl;
						}
					}
					
				}
			}
		}
		
		return parentServiceFactory.createService(clazz, session, optionalArguments);
	}
	
	protected GDBControl getGdbControl_override(DsfSession session, ILaunchConfiguration config, 
			GdbDebugServicesFactory gdbSvcFactory) {
		GDBControl gdbControl = null;
		
		if(GdbDebugServicesFactory.GDB_7_7_VERSION.compareTo(gdbSvcFactory.getVersion()) <= 0) {
			gdbControl = new GDBControl_7_7(session, config, new CommandFactory_6_8()) {
				@Override
				protected Sequence getCompleteInitializationSequence(Map<String, Object> attributes,
						RequestMonitorWithProgress rm) {
					return getCompleteInitializationSequence__GDBControl_7_7__ext(getSession(), attributes, rm);
				}
			};
		}
		return gdbControl;
	}
	
	protected Sequence getCompleteInitializationSequence__GDBControl_7_7__ext(DsfSession session, Map<String, Object> attributes, 
			RequestMonitorWithProgress rm) {
		return new FinalLaunchSequence_7_7(session, attributes, rm);
	}
	
	/* -----------------  ----------------- */
	
	public IExpressions createExpressionService(DsfSession session) {
		IMIExpressions originialExpressionService = new MIExpressions_Override(session);
		return new GDBPatternMatchingExpressions(session, originialExpressionService);
	}
	
	public class MIExpressions_Override extends MIExpressions {
		public MIExpressions_Override(DsfSession session) {
			super(session);
		}
		
		@Override
		protected MIVariableManager createMIVariableManager() {
			return services_MIExpressions_createMIVariableManager(getSession(), getServicesTracker());
		}
		
		@Override
		public void getFormattedExpressionValue(FormattedValueDMContext dmc,
				DataRequestMonitor<FormattedValueDMData> rm) {
			try {
				getFormattedExpressionValue_override(dmc, rm);
			} catch(NoSuchFieldException e) {
				// Fall-backt to normal code
				super.getFormattedExpressionValue(dmc, rm);
			}
		}
		
		public void getFormattedExpressionValue_override(FormattedValueDMContext dmc,
				DataRequestMonitor<FormattedValueDMData> rm) throws NoSuchFieldException {
			// We need to make sure the FormattedValueDMContext also holds an ExpressionContext,
			// or else this method cannot do its work.
			// Note that we look for MIExpressionDMC and not IExpressionDMC, because
			// looking for IExpressionDMC could yield InvalidContextExpressionDMC which is still
			// not what we need.
			MIExpressionDMC exprDmc = DMContexts.getAncestorOfType(dmc, MIExpressionDMC.class);
			if (exprDmc == null ) {
				rm.setStatus(new Status(IStatus.ERROR, GdbPlugin.PLUGIN_ID, INVALID_HANDLE, "Invalid context for evaluating expressions.", null)); //$NON-NLS-1$
				rm.done();
			} else {
				if (DETAILS_FORMAT.equals(dmc.getFormatID())) {
//					if (exprDmc.getExpressionInfo().hasDynamicAncestor()) {
//						// -data-evaluate-expression does not work for children of
//						// dynamic varobjs, since there is no full expression
//						// that gdb could evaluate.
//						rm.setData(new FormattedValueDMData(Messages.MIExpressions_NotAvailableBecauseChildOfDynamicVarobj));
//						rm.done();
//					} else {
						// This format is obtained through a different GDB command.
						// It yields more details than the variableObject output.
						// Starting with GDB 7.0, this format automatically supports pretty-printing, as long as
						// GDB has been configured to support it.
					
					CommandFactory fCommandFactory = ReflectionUtils.readField(this, "fCommandFactory");
					boolean fTraceVisualization = ReflectionUtils.readField(this, "fTraceVisualization");
					
						fExpressionCache.execute(
								fCommandFactory.createMIDataEvaluateExpression(exprDmc), 
								new DataRequestMonitor<MIDataEvaluateExpressionInfo>(getExecutor(), rm) {
									@Override
									protected void handleSuccess() {
										rm.setData(new FormattedValueDMData(getData().getValue()));
										rm.done();
									}
									@Override
									protected void handleError() {
										if (fTraceVisualization) {
											rm.setData(new FormattedValueDMData("")); //$NON-NLS-1$
											rm.done();
										} else {
											super.handleError();
										}
									}
								});
//					}
				} else {
					fExpressionCache.execute(
							new ExprMetaGetValue(dmc),
							new DataRequestMonitor<ExprMetaGetValueInfo>(getExecutor(), rm) {
								@Override
								protected void handleSuccess() {
									rm.setData(new FormattedValueDMData(getData().getValue()));
									rm.done();
								}
							});
				}
			}
		}
	}
	
	protected MIVariableManager services_MIExpressions_createMIVariableManager(DsfSession session,
			DsfServicesTracker servicesTracker) {
		return new MIVariableManager_LangExtension(session, servicesTracker);
	}
	
	public IMIBackend createBackendGDBService(DsfSession session, ILaunchConfiguration lc) {
		return new GDBBackend(session, lc);
	}
	
}