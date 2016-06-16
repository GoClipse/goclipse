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
package com.googlecode.goclipse.debug.core;

import org.eclipse.cdt.dsf.debug.service.IDsfDebugServicesFactory;
import org.eclipse.cdt.dsf.gdb.GDBTypeParser.GDBDerivedType;
import org.eclipse.cdt.dsf.gdb.GDBTypeParser.GDBType;
import org.eclipse.cdt.dsf.mi.service.MIVariableManager;
import org.eclipse.cdt.dsf.mi.service.command.output.MIVar;
import org.eclipse.cdt.dsf.service.DsfServicesTracker;
import org.eclipse.cdt.dsf.service.DsfSession;

import melnorme.lang.ide.debug.core.services.LangDebugServicesExtensions;
import melnorme.lang.ide.debug.core.services.MIVariableManager_LangExtension;

public class GoDebugServicesExtensions extends LangDebugServicesExtensions {
	
	public GoDebugServicesExtensions(IDsfDebugServicesFactory parentServiceFactory) {
		super(parentServiceFactory);
	}
	
	@Override
	protected MIVariableManager services_MIExpressions_createMIVariableManager(DsfSession session,
			DsfServicesTracker servicesTracker) {
		return new MIVariableManager_LangExtension(session, servicesTracker) {
			@Override
			protected GDBType getCorrectedGdbType(String newTypeName, GDBType gdbType) {
				if(gdbType.getType() == GDBType.POINTER && gdbType instanceof GDBDerivedType) {
					GDBDerivedType gdbDerivedType = (GDBDerivedType) gdbType;
					return gdbTypeParser.new GDBDerivedType(gdbDerivedType.getChild(), GDBType.REFERENCE);
				}
				return super.getCorrectedGdbType(newTypeName, gdbType);
			}
			
			@Override
			public String createChild_getChildFullExpression(String childFullExpression, MIVar childData) {
				return childFullExpression.replace(")->", ").");
			}
		};
	}
	
}