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


import org.eclipse.cdt.dsf.debug.ui.viewmodel.SteppingController;
import org.eclipse.cdt.dsf.gdb.internal.ui.GdbAdapterFactory;
import org.eclipse.cdt.dsf.gdb.internal.ui.GdbSessionAdapters;
import org.eclipse.cdt.dsf.gdb.internal.ui.viewmodel.GdbExpressionVMProvider;
import org.eclipse.cdt.dsf.gdb.internal.ui.viewmodel.GdbVariableVMProvider;
import org.eclipse.cdt.dsf.service.DsfSession;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.internal.ui.viewers.model.provisional.IPresentationContext;
import org.eclipse.debug.internal.ui.viewers.model.provisional.IViewerInputProvider;

import melnorme.lang.ide.debug.ui.viewmodel.GdbExtendedViewModelAdapter;
import melnorme.lang.ide.debug.ui.viewmodel.Lang_GdbExpressionVMProvider;
import melnorme.lang.ide.debug.ui.viewmodel.Lang_GdbVariableVMProvider;

public abstract class AbstractLangGdbAdapterFactory extends GdbAdapterFactory {
	
    @Override
    protected GdbSessionAdapters createGdbSessionAdapters(ILaunch launch, DsfSession session) {
    	return new GdbExtendedSessionAdapters(launch, session, getAdapterList());
    }
    
    public class GdbExtendedSessionAdapters extends GdbSessionAdapters {
        
    	public GdbExtendedSessionAdapters(ILaunch launch, DsfSession session, Class<?>[] launchAdapterTypes) {
    		super(launch, session, launchAdapterTypes);
    	}
        
    	@SuppressWarnings("unchecked")
    	@Override
    	protected <T> T createModelAdapter(Class<T> adapterType, ILaunch launch, DsfSession session) {
    		if (IViewerInputProvider.class.equals(adapterType)) {
    			return (T) createGdbViewModelAdapter(session, getSteppingController());
    		}
    		
    		return super.createModelAdapter(adapterType, launch, session);
    	}
    }
	
	protected GdbExtendedViewModelAdapter createGdbViewModelAdapter(DsfSession session, 
			SteppingController steppingController) {
		return new GdbExtendedViewModelAdapter(session, steppingController) {
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