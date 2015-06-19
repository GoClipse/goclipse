/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.operations;

import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper;

import org.eclipse.core.resources.IProject;

public class ProcessStartInfo {
	
	public ProcessBuilder pb;
	public IProject project;
	public String prefixText;
	public ExternalProcessNotifyingHelper processHelper;
	public CommonException ce;
	
	public boolean clearConsole;
	
	public ProcessStartInfo(boolean clearConsole) {
		this.clearConsole = clearConsole;
	}
	
	public ProcessStartInfo(ProcessBuilder pb, IProject project, String prefixText, 
			boolean clearConsole, ExternalProcessNotifyingHelper processHelper, CommonException ce) {
		this.pb = pb;
		this.project = project;
		this.prefixText = prefixText;
		this.processHelper = processHelper;
		this.ce = ce;
		
		this.clearConsole = clearConsole;
	}
	
}