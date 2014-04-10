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

import java.util.Map;

import melnorme.lang.ide.ui.LangUIPlugin;

import org.eclipse.cdt.dsf.debug.ui.viewmodel.IDebugVMConstants;
import org.eclipse.cdt.dsf.debug.ui.viewmodel.numberformat.FormattedValueLabelText;
import org.eclipse.cdt.dsf.debug.ui.viewmodel.variable.SyncVariableDataAccess;
import org.eclipse.cdt.dsf.gdb.internal.ui.viewmodel.GdbVariableVMNode;
import org.eclipse.cdt.dsf.mi.service.MIExpressions;
import org.eclipse.cdt.dsf.service.DsfSession;
import org.eclipse.cdt.dsf.ui.viewmodel.datamodel.AbstractDMVMProvider;
import org.eclipse.cdt.dsf.ui.viewmodel.properties.LabelAttribute;
import org.eclipse.cdt.dsf.ui.viewmodel.properties.LabelColumnInfo;
import org.eclipse.cdt.dsf.ui.viewmodel.properties.PropertiesBasedLabelProvider;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.debug.internal.ui.viewers.model.provisional.IElementLabelProvider;

@SuppressWarnings("restriction")
public class GdbVariableVMNode_Override extends GdbVariableVMNode {
	GdbVariableVMNode_Override(AbstractDMVMProvider provider, DsfSession session,
			SyncVariableDataAccess syncVariableDataAccess) {
		super(provider, session, syncVariableDataAccess);
	}
	
	@Override
	protected IElementLabelProvider createLabelProvider() {
		IElementLabelProvider labelProvider = super.createLabelProvider();
		if(labelProvider instanceof PropertiesBasedLabelProvider) { // Shoud be true
			PropertiesBasedLabelProvider provider = (PropertiesBasedLabelProvider) labelProvider;
			customizeLabelProvider(provider);
			
		}
		return labelProvider;
	}
	
	protected static void customizeLabelProvider(PropertiesBasedLabelProvider provider) {
		LabelColumnInfo columnInfo = provider.getColumnInfo(IDebugVMConstants.COLUMN_ID__VALUE);
		LabelAttribute[] labelAttributes = columnInfo.getLabelAttributes();
		
		
		int preferredIndex = -1;
		
		// Find the best FormattedValueLabelText to replace (the one that would print "details" format)
		// this should the one at index 1, according to super.createLabelProvider() in
		// org.eclipse.cdt.dsf.ui_2.4.0.201402142303
		for (int i = 0; i < labelAttributes.length; i++) {
			LabelAttribute labelAttribute = labelAttributes[i];
			if(labelAttribute instanceof FormattedValueLabelText) {
				preferredIndex = i;
			}
		}
		if(preferredIndex != -1) {
			labelAttributes[preferredIndex] = new FormattedValueLabelText_Override();
		} else {
			LangUIPlugin.logError("Internal Error: " +
				" Failed to replace CDT's FormattedValueLabelText for the Variables view");
		}
		
		provider.setColumnInfo(IDebugVMConstants.COLUMN_ID__VALUE, new LabelColumnInfo(labelAttributes));
	}
	
	public static class FormattedValueLabelText_Override extends FormattedValueLabelText {
		
	    protected String PROP_ACTIVE_FORMAT = IDebugVMConstants.PROP_FORMATTED_VALUE_ACTIVE_FORMAT;
	    protected String PROP_ACTIVE_FORMAT_VALUE = IDebugVMConstants.PROP_FORMATTED_VALUE_ACTIVE_FORMAT_VALUE;

		public FormattedValueLabelText_Override() {
		}
		
		@Override
		protected Object getPropertyValue(String propertyName, IStatus status, Map<String, Object> properties) {
	        if ( PROP_ACTIVE_FORMAT_VALUE.equals(propertyName) ) {
	            Object activeFormat = properties.get(PROP_ACTIVE_FORMAT);
	            if(activeFormat.equals(MIExpressions.DETAILS_FORMAT)){
	            	// Override parent behaviour: don't add "(Details)" suffix
	            	return properties.get(propertyName);
	            }
	        }
	        return super.getPropertyValue(propertyName, status, properties);
		}
		
	}
	
}