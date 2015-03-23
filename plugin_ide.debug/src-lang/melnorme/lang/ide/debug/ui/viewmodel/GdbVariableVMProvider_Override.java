package melnorme.lang.ide.debug.ui.viewmodel;


import org.eclipse.cdt.dsf.debug.internal.ui.viewmodel.DsfCastToTypeSupport;
import org.eclipse.cdt.dsf.debug.ui.viewmodel.variable.SyncVariableDataAccess;
import org.eclipse.cdt.dsf.gdb.internal.ui.viewmodel.GdbVariableVMNode;
import org.eclipse.cdt.dsf.gdb.internal.ui.viewmodel.GdbVariableVMProvider;
import org.eclipse.cdt.dsf.service.DsfSession;
import org.eclipse.cdt.dsf.ui.viewmodel.AbstractVMAdapter;
import org.eclipse.cdt.dsf.ui.viewmodel.IRootVMNode;
import org.eclipse.cdt.dsf.ui.viewmodel.IVMNode;
import org.eclipse.cdt.dsf.ui.viewmodel.datamodel.RootDMVMNode;
import org.eclipse.debug.internal.ui.viewers.model.provisional.IPresentationContext;

//@SuppressWarnings("restriction")
public class GdbVariableVMProvider_Override extends GdbVariableVMProvider {
	
	public GdbVariableVMProvider_Override(AbstractVMAdapter adapter, IPresentationContext context,
			DsfSession session) {
		super(adapter, context, session);
	}
	
	// copied from super code
	@Override
	protected void configureLayout() {
        // Create the variable data access routines.
        SyncVariableDataAccess varAccess = new SyncVariableDataAccess(getSession()) ;
    	
        // Create the top level node to deal with the root selection.
        IRootVMNode rootNode = new RootDMVMNode(this);
        setRootNode(rootNode);
        
        // Create the next level which represents members of structs/unions/enums and elements of arrays.
        GdbVariableVMNode subExpressioNode = createGdbVariableVMNode(varAccess);
        addChildNodes(rootNode, new IVMNode[] { subExpressioNode });

		/* Wire up the casting support. IExpressions2 service is always available
		 * for gdb. No need to call hookUpCastingSupport */
		subExpressioNode.setCastToTypeSupport(new DsfCastToTypeSupport(getSession(), this, varAccess));
        
        // Configure the sub-expression node to be a child of itself.  This way the content
        // provider will recursively drill-down the variable hierarchy.
        addChildNodes(subExpressioNode, new IVMNode[] { subExpressioNode });
    }
	
	protected GdbVariableVMNode createGdbVariableVMNode(SyncVariableDataAccess varAccess) {
		return new GdbVariableVMNode(this, getSession(), varAccess);
	}
	
}