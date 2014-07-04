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
package melnorme.lang.ide.debug.core.services;

import org.eclipse.cdt.dsf.debug.service.IExpressions;
import org.eclipse.cdt.dsf.gdb.service.GDBPatternMatchingExpressions;
import org.eclipse.cdt.dsf.gdb.service.command.CommandFactory_6_8;
import org.eclipse.cdt.dsf.gdb.service.macos.MacOSCommandFactory;
import org.eclipse.cdt.dsf.mi.service.IMIExpressions;
import org.eclipse.cdt.dsf.mi.service.MIExpressions;
import org.eclipse.cdt.dsf.mi.service.MIVariableManager;
import org.eclipse.cdt.dsf.mi.service.command.CommandFactory;
import org.eclipse.cdt.dsf.service.DsfServicesTracker;
import org.eclipse.cdt.dsf.service.DsfSession;

public class DebugServicesExtensions {
	
	public IExpressions createExpressionService(DsfSession session) {
		IMIExpressions originialExpressionService = new MIExpressions(session) {
			@Override
			protected MIVariableManager createMIVariableManager() {
				return services_createMIVariableManager(getSession(), getServicesTracker());
			}

		};
		return new GDBPatternMatchingExpressions(session, originialExpressionService);
	}
	
	protected MIVariableManager services_createMIVariableManager(DsfSession session,
			DsfServicesTracker servicesTracker) {
		return new MIVariableManager_LangExtension(session, servicesTracker);
	}
	
	public CommandFactory_6_8 createCommandFactory_6_8() {
		return new CommandFactory_6_8();
	}
	
	public CommandFactory createCommandFactory() {
		return new CommandFactory();
	}
	
	public CommandFactory createMacOSCommandFactory() {
		return new MacOSCommandFactory();
	}
	
}