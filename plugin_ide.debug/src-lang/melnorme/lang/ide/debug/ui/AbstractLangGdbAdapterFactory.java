/*******************************************************************************
 * Copyright (c) 2014, 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.debug.ui;


import melnorme.lang.ide.debug.ui.viewmodel.GdbViewModelAdapter;
import melnorme.lang.ide.debug.ui.viewmodel.Lang_GdbExpressionVMProvider;
import melnorme.lang.ide.debug.ui.viewmodel.Lang_GdbVariableVMProvider;

import org.eclipse.cdt.dsf.debug.ui.viewmodel.SteppingController;
import org.eclipse.cdt.dsf.gdb.internal.ui.viewmodel.GdbExpressionVMProvider;
import org.eclipse.cdt.dsf.gdb.internal.ui.viewmodel.GdbVariableVMProvider;
import org.eclipse.cdt.dsf.service.DsfSession;
import org.eclipse.debug.internal.ui.viewers.model.provisional.IPresentationContext;

public abstract class AbstractLangGdbAdapterFactory extends GdbAdapterFactory {
	
	public AbstractLangGdbAdapterFactory() {
		super();
	}
	
	@Override
	protected GdbViewModelAdapter createGdbViewModelAdapter(DsfSession session, SteppingController steppingController) {
		return new GdbViewModelAdapter(session, steppingController) {
			@Override
			protected GdbVariableVMProvider createGdbVariableProvider(IPresentationContext context) {
				return new Lang_GdbVariableVMProvider(this, context, getSession());
			}
			
			@Override
			protected GdbExpressionVMProvider createGdbExpressionProvider(IPresentationContext context) {
				return new Lang_GdbExpressionVMProvider(this, context, getSession());
			}
			
			@Override
			protected GdbExpressionVMProvider createGdbExpressionHoverProvider(IPresentationContext context) {
				return new Lang_GdbExpressionVMProvider(this, context, getSession());
			}
			
		};
	}
	
}