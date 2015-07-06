package melnorme.lang.ide.debug.ui.viewmodel;

import org.eclipse.cdt.dsf.debug.ui.viewmodel.numberformat.IElementFormatProvider;
import org.eclipse.cdt.dsf.debug.ui.viewmodel.variable.SyncVariableDataAccess;
import org.eclipse.cdt.dsf.gdb.internal.ui.viewmodel.GdbVariableVMNode;
import org.eclipse.cdt.dsf.service.DsfSession;
import org.eclipse.cdt.dsf.ui.viewmodel.AbstractVMAdapter;
import org.eclipse.debug.internal.ui.viewers.model.provisional.IPresentationContext;

public class Lang_GdbExpressionVMProvider extends GdbExpressionVMProvider_Override
		implements IElementFormatProvider {
	
	public Lang_GdbExpressionVMProvider(AbstractVMAdapter adapter, IPresentationContext context, DsfSession session) {
		super(adapter, context, session);
	}
	
	@Override
	protected GdbVariableVMNode createGdbVariableVMNode(SyncVariableDataAccess varAccess) {
		return new GdbVariableVMNode_Override(this, getSession(), varAccess);
	}
	
}