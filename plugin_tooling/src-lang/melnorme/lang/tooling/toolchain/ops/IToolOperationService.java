/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.tooling.toolchain.ops;

import melnorme.utilbox.misc.ILogHandler;

/**
 * Service/helper class to perform certain operation tasks (such as running a process) 
 * under a context that is abstracted away. (Usually it's a UI context that observes the tasks) 
 */
public interface IToolOperationService extends ILogHandler, IProcessRunner {
	
}