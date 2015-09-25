/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.launching;

import java.util.Map;

import melnorme.lang.ide.core.LangCore;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.RuntimeProcess;

/**
 * Extension to {@link RuntimeProcess} that allows updating the process label as used by the console.
 */
public class RuntimeProcessExtension extends RuntimeProcess {

	public RuntimeProcessExtension(ILaunch launch, Process process, String name, Map<String, String> attributes) {
		super(launch, process, name, attributes);
	}
	
	@Override
	protected void fireTerminateEvent() {
		updateProcessLabelForConsole();
		super.fireTerminateEvent();
	}
	
	protected void updateProcessLabelForConsole() {
		setAttribute(IProcess.ATTR_PROCESS_LABEL, calcExtendedProcessLabel(this));
	}

	public static String calcExtendedProcessLabel(IProcess process) {
		ILaunch launch = process.getLaunch();
		
		StringBuffer buffer = new StringBuffer();
		
		if (process.isTerminated()) {
			try {
				int exitValue = process.getExitValue();
				buffer.append("<exit code: " + exitValue + "> ");
			} catch (DebugException e) {
				// Should not happen
			}
		}
		
		ILaunchConfiguration launchConfiguration = launch.getLaunchConfiguration();
		if (launchConfiguration != null) {
			buffer.append(launchConfiguration.getName());
	
			try {
				ILaunchConfigurationType launchConfigType = launchConfiguration.getType();
				if (launchConfigType != null) {
					String type = launchConfigType.getName();
	
					buffer.append(" [");
					buffer.append(type);
					buffer.append("] ");
				}
			} catch (CoreException ce) {
				LangCore.logStatus(ce);
			}
		}
		buffer.append(process.getLabel());
		return buffer.toString();
	}
	
}