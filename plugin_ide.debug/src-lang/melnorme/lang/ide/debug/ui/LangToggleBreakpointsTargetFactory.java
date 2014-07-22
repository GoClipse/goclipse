/*******************************************************************************
 * Copyright (c) 2013, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.debug.ui;

import static melnorme.utilbox.core.CoreUtil.array;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import melnorme.lang.ide.ui.LangUIPlugin;

import org.eclipse.debug.ui.actions.IToggleBreakpointsTarget;
import org.eclipse.debug.ui.actions.IToggleBreakpointsTargetFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;

public class LangToggleBreakpointsTargetFactory implements IToggleBreakpointsTargetFactory {
	
	public static final String DYNAMIC_PRINTF_FACTORY_ID = LangUIPlugin.PLUGIN_ID + "DynamicPrintfBreakpointFactory";
	
	public LangToggleBreakpointsTargetFactory() {
	}
	
	@Override
	public Set<String> getToggleTargets(IWorkbenchPart part, ISelection selection) {
		return new HashSet<>(Arrays.asList(array(
			DebugUI_Actual.LANG_BREAKPOINT_FACTORY_ID,
			DYNAMIC_PRINTF_FACTORY_ID
		)));
	}
	
	@Override
	public String getDefaultToggleTarget(IWorkbenchPart part, ISelection selection) {
		return DebugUI_Actual.LANG_BREAKPOINT_FACTORY_ID;
	}
	
	@Override
	public IToggleBreakpointsTarget createToggleTarget(String targetID) {
		if(targetID.equals(DebugUI_Actual.LANG_BREAKPOINT_FACTORY_ID)) {
			return DebugUI_Actual.createToggleBreakPointAdapter();
		}
		if(targetID.equals(DYNAMIC_PRINTF_FACTORY_ID)) {
			return DebugUI_Actual.createDynamicPrintfBreakpoint();
		}
		return null;
	}
	
	@Override
	public String getToggleTargetName(String targetID) {
		if(targetID.equals(DebugUI_Actual.LANG_BREAKPOINT_FACTORY_ID)) {
			return DebugMessages.LANG_BREAKPOINT_TARGET_NAME;
		}
		if(targetID.equals(DYNAMIC_PRINTF_FACTORY_ID)) {
			return DebugMessages.LANG_DYNAMIC_PRINTF_BREAKPOINT_TARGET_NAME;
		}
		return "";
	}
	
	@Override
	public String getToggleTargetDescription(String targetID) {
		if(targetID.equals(DebugUI_Actual.LANG_BREAKPOINT_FACTORY_ID)) {
			return DebugMessages.LANG_BREAKPOINT_TARGET_DESCRIPTION;
		}
		if(targetID.equals(DYNAMIC_PRINTF_FACTORY_ID)) {
			return DebugMessages.LANG_DYNAMIC_PRINTF_BREAKPOINT_TARGET_DESCRIPTION;
		}
		return "";
	}
	
}