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
package melnorme.lang.utils;

/** 
 * Marker class to indicate to methods that hold an instance of this, 
 * that they are currently running in a worker thread, as such, it is ok to perform long-running tasks. 
 */
public class M_WorkerThread {
	
}