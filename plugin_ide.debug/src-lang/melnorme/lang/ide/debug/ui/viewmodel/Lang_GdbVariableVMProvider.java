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
package melnorme.lang.ide.debug.ui.viewmodel;

import org.eclipse.cdt.dsf.concurrent.DataRequestMonitor;
import org.eclipse.cdt.dsf.debug.ui.viewmodel.numberformat.IElementFormatProvider;
import org.eclipse.cdt.dsf.debug.ui.viewmodel.variable.SyncVariableDataAccess;
import org.eclipse.cdt.dsf.debug.ui.viewmodel.variable.VariableVMNode;
import org.eclipse.cdt.dsf.gdb.internal.ui.viewmodel.GdbVariableVMNode;
import org.eclipse.cdt.dsf.mi.service.MIExpressions;
import org.eclipse.cdt.dsf.service.DsfSession;
import org.eclipse.cdt.dsf.ui.viewmodel.AbstractVMAdapter;
import org.eclipse.cdt.dsf.ui.viewmodel.IVMContext;
import org.eclipse.cdt.dsf.ui.viewmodel.IVMNode;
import org.eclipse.debug.internal.ui.viewers.model.provisional.IPresentationContext;
import org.eclipse.jface.viewers.TreePath;

//@SuppressWarnings("restriction")
public class Lang_GdbVariableVMProvider extends GdbVariableVMProvider_Override implements IElementFormatProvider {
	
	public Lang_GdbVariableVMProvider(AbstractVMAdapter adapter, IPresentationContext context,
			DsfSession session) {
		super(adapter, context, session);
	}

	@Override
	public void getActiveFormat(IPresentationContext context, IVMNode node, Object viewerInput,
			TreePath elementPath, DataRequestMonitor<String> rm) {
		
		Object x = elementPath.getLastSegment();
		if (x instanceof VariableVMNode.VariableExpressionVMC) {
			rm.setData(MIExpressions.DETAILS_FORMAT);
			rm.done();
			return;
		}
		rm.setData(null);
		rm.done();
	}
	
	@Override
	public void setActiveFormat(IPresentationContext context, IVMNode[] node, Object viewerInput,
			TreePath[] elementPath, String format) {
		// Not supported at the moment, do nothing.
	}
	
	@Override
	public boolean supportFormat(IVMContext context) {
		if (context instanceof VariableVMNode.VariableExpressionVMC) {
			return true;
		}
		return false;
	}
	
	@Override
	protected GdbVariableVMNode createGdbVariableVMNode(SyncVariableDataAccess varAccess) {
		return new GdbVariableVMNode_Override(this, getSession(), varAccess);
	}

}