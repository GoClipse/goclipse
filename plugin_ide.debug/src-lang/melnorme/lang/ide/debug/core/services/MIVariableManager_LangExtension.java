package melnorme.lang.ide.debug.core.services;

import org.eclipse.cdt.dsf.gdb.GDBTypeParser;
import org.eclipse.cdt.dsf.gdb.GDBTypeParser.GDBDerivedType;
import org.eclipse.cdt.dsf.gdb.GDBTypeParser.GDBType;
import org.eclipse.cdt.dsf.mi.service.MIVariableManager;
import org.eclipse.cdt.dsf.mi.service.command.output.MIVar;
import org.eclipse.cdt.dsf.service.DsfServicesTracker;
import org.eclipse.cdt.dsf.service.DsfSession;

public class MIVariableManager_LangExtension extends MIVariableManager {
	
	public MIVariableManager_LangExtension(DsfSession session, DsfServicesTracker tracker) {
		super(session, tracker);
	}
	
	@Override
	protected MIRootVariableObject createRootVariableObject(VariableObjectId id) {
		return new MIRootVariableObject_Extension(id);
	}
	
	@Override
	protected MIVariableObject createVariableObject(VariableObjectId id, MIVariableObject parentObj) {
		return new MIVariableObject_Extension(id, parentObj);
	}
	
	protected class MIRootVariableObject_Extension extends MIRootVariableObject {
		
		protected GDBType corrected_gdbType;
		
		private MIRootVariableObject_Extension(VariableObjectId id) {
			super(id);
		}
		
		@Override
		public void setType(String newTypeName) {
			super.setType(newTypeName);
			corrected_gdbType = getCorrectedGdbType(newTypeName, super.getGDBType());
		}
		
		@Override
		public GDBType getGDBType() {
			return corrected_gdbType;
		}
		
		@Override
		protected MIVariableObject createChild(VariableObjectId childId, String childFullExpression, int indexInParent,
				MIVar childData) {
			childFullExpression = createChild_getChildFullExpression(childFullExpression, childData);
			return super.createChild(childId, childFullExpression, indexInParent, childData);
		}
		
	}

	protected class MIVariableObject_Extension extends MIVariableObject {
		
		protected GDBType corrected_gdbType;
		
		public MIVariableObject_Extension(VariableObjectId id, MIVariableObject parentObj) {
			super(id, parentObj);
		}
		
		@Override
		public void setType(String newTypeName) {
			super.setType(newTypeName);
			corrected_gdbType = getCorrectedGdbType(newTypeName, super.getGDBType());
		}
		
		@Override
		public GDBType getGDBType() {
			return corrected_gdbType;
		}
		
		@Override
		protected MIVariableObject createChild(VariableObjectId childId, String childFullExpression, int indexInParent,
				MIVar childData) {
			childFullExpression = createChild_getChildFullExpression(childFullExpression, childData);
			return super.createChild(childId, childFullExpression, indexInParent, childData);
		}
		
	}
	
	protected final GDBTypeParser gdbTypeParser = new GDBTypeParser();

	@SuppressWarnings("unused")
	protected GDBType getCorrectedGdbType(String newTypeName, GDBType gdbType) {
		if(gdbType.getType() == GDBType.ARRAY && gdbType instanceof GDBDerivedType) {
			GDBDerivedType gdbDerivedType = (GDBDerivedType) gdbType;
			if(gdbDerivedType.getDimension() == 0) {
				// Correct a limitation in the GDBTypeParser when a "[]" type decl is present
				// CDT will think it's a pointer-like C-style array, and will try to display a custom node structure.
				// But in other languages, it's more likely to be a struct, so report as generic type.
				gdbType = gdbTypeParser.new GDBDerivedType(gdbDerivedType.getChild(), GDBType.GENERIC);
			}
		}
		return gdbType;
	}
	
	@SuppressWarnings("unused")
	public String createChild_getChildFullExpression(String childFullExpression, MIVar childData) {
		return childFullExpression;
	}
	
}