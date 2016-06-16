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
package com.googlecode.goclipse.ui;

public class GoUIMessages {

	public static final String AUTOMATIC_UNIT_TESTING_WARNING = "Continuous Unit Testing is an experimental feature and depending on its effectiveness, may be removed at some future date.  When enabled, package level unit tests matching the regular expression below will be scheduled to run when a source file is modified within the same package.   By default, only test prefixed with 'TestAuto' will be run.  You should change this regular expression to whatever is appropriate for your project.\n\nA package can only have one scheduled run in the queue at a time and each test has an allotted time slot.  The default time slot size is 5 seconds.  If a test runs over its allotted time, it will be killed and the next test in the queue will run.  \n\nCurrently, there is no notification that a test busted its time; it is assumed long running tests are either an errant test (has an infinite loop) or not suitable to be run with this feature.  Failed tests show up as errors within the project.  ";
	
}