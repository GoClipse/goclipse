/*******************************************************************************
 * Copyright (c) 2006, 2010 Wind River Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Wind River Systems - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.debug.ui.viewmodel;

import org.eclipse.cdt.dsf.concurrent.ThreadSafe;
import org.eclipse.cdt.dsf.debug.ui.IDsfDebugUIConstants;
import org.eclipse.cdt.dsf.debug.ui.viewmodel.SteppingController;
import org.eclipse.cdt.dsf.gdb.internal.ui.viewmodel.GdbExpressionVMProvider;
import org.eclipse.cdt.dsf.gdb.internal.ui.viewmodel.GdbVariableVMProvider;
import org.eclipse.cdt.dsf.gdb.internal.ui.viewmodel.GdbViewModelAdapter;
import org.eclipse.cdt.dsf.service.DsfSession;
import org.eclipse.cdt.dsf.ui.viewmodel.IVMProvider;
import org.eclipse.debug.internal.ui.viewers.model.provisional.IPresentationContext;
import org.eclipse.debug.ui.IDebugUIConstants;

@ThreadSafe
public class GdbExtendedViewModelAdapter extends GdbViewModelAdapter
{
    
    public GdbExtendedViewModelAdapter(DsfSession session, SteppingController controller) {
		super(session, controller);
	}
    
	@Override
    protected IVMProvider createViewModelProvider(IPresentationContext context) {
        if (IDebugUIConstants.ID_VARIABLE_VIEW.equals(context.getId()) ) {
            return createGdbVariableProvider(context);
        } else if (IDebugUIConstants.ID_EXPRESSION_VIEW.equals(context.getId()) ) {
            return createGdbExpressionProvider(context);
        } else if (IDsfDebugUIConstants.ID_EXPRESSION_HOVER.equals(context.getId()) ) {
            return createGdbExpressionHoverProvider(context);
        }
        return super.createViewModelProvider(context);
    }
    
	protected GdbVariableVMProvider createGdbVariableProvider(IPresentationContext context) {
		return new GdbVariableVMProvider(this, context, getSession());
	}
    
    protected GdbExpressionVMProvider createGdbExpressionProvider(IPresentationContext context) {
		return new GdbExpressionVMProvider(this, context, getSession());
	}
    
    protected GdbExpressionVMProvider createGdbExpressionHoverProvider(IPresentationContext context) {
		return new GdbExpressionVMProvider(this, context, getSession());
	}
    
}