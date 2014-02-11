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

import org.eclipse.debug.ui.actions.IToggleBreakpointsTarget;
import org.eclipse.debug.ui.actions.IToggleBreakpointsTargetFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;

public class LangToggleBreakpointsTargetFactory implements IToggleBreakpointsTargetFactory {
	
	public LangToggleBreakpointsTargetFactory() {
	}
	
	@Override
	public Set<?> getToggleTargets(IWorkbenchPart part, ISelection selection) {
		return new HashSet<String>(Arrays.asList(array(DebugUI.LANG_BREAKPOINT_FACTORY_ID)));
	}
	
	@Override
	public String getDefaultToggleTarget(IWorkbenchPart part, ISelection selection) {
		return DebugUI.LANG_BREAKPOINT_FACTORY_ID;
	}
	
	@Override
	public IToggleBreakpointsTarget createToggleTarget(String targetID) {
		if(targetID.equals(DebugUI.LANG_BREAKPOINT_FACTORY_ID)) {
			return DebugUI.createToggleBreakPointAdapter();
		}
		return null;
	}
	
	@Override
	public String getToggleTargetName(String targetID) {
		return DebugMessages.LANG_BREAKPOINT_TARGET_NAME;
	}
	
	@Override
	public String getToggleTargetDescription(String targetID) {
		return DebugMessages.LANG_BREAKPOINT_TARGET_DESCRIPTION;
	}
	
}