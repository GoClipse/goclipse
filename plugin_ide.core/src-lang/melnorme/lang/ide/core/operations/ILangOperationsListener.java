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
package melnorme.lang.ide.core.operations;

import melnorme.utilbox.process.ExternalProcessNotifyingHelper;

import org.eclipse.core.runtime.CoreException;

public interface ILangOperationsListener {
	
	void engineDaemonStarted(ProcessBuilder pb, ExternalProcessNotifyingHelper processHelper);
	
	void engineDaemonFailedToStart(CoreException ce);
	
}