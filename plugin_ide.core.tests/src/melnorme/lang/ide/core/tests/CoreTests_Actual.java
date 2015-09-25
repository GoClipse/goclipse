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
package melnorme.lang.ide.core.tests;

import java.io.PrintStream;

import melnorme.utilbox.misc.SimpleLogger;

public class CoreTests_Actual {
	
	public final static PrintStream testsLogger = System.out;
	
	public final static SimpleLogger testsLogVerbose = new SimpleLogger("verbose");
	
}