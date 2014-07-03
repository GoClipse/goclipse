/*******************************************************************************
 * Copyright (c) 2007, 2010 The original authors/Contributors listed below.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 		Bruce Eckel - initial code
 *		Bruno Medeiros - modifications
 *******************************************************************************/
package melnorme.utilbox.core;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.io.IOException;


/**
 * Exception adapter to make checked exceptions less annoying. 
 * Based on Bruce Eckel's article:
 * http://www.mindview.net/Etc/Discussions/CheckedExceptions
 */
@SuppressWarnings("serial")
public class ExceptionAdapter extends RuntimeException {
	
	// Number of frames that originalException traveled while checked
	protected int checkedLength; 
	
	protected ExceptionAdapter(Exception e) {
		super(e);
		assertNotNull(e);
		
		// Determine checkedLength based on the difference to this stack trace
		StackTraceElement[] est = e.getStackTrace();
		checkedLength = est.length - getStackTrace().length;
		
		StackTraceElement ste = getStackTrace()[0];
		String firstMethod = ste.getClassName() +"."+ ste.getMethodName();
		// Adjust checkedLength if EA was created in method unchecked
		if(firstMethod.endsWith("ExceptionAdapter.unchecked")) {
			checkedLength++;
		}
	}
	
	
	protected void printStackTraceAppendable(Appendable pr) {
		synchronized(pr) {
			try {
				pr.append(this.toString());
				StackTraceElement[] trace = getCause().getStackTrace();
				for (int i=0; i < trace.length; i++) {
					pr.append("\tat " + trace[i]);
					if(i == checkedLength) {
						pr.append(" [UNCHECKED]");
					}
					pr.append("\n");
				}
			} catch (IOException e) {
				assertFail();
			}
		}
	}
	
	@Override
	public void printStackTrace(java.io.PrintStream ps) {		
		printStackTraceAppendable(ps);
	}
	
	@Override
	public void printStackTrace(java.io.PrintWriter pw) {
		printStackTraceAppendable(pw);
	}
	
	@Override
	public Exception getCause() {
		return (Exception) super.getCause();
	}
	
	public void rethrow() throws Exception {
		throw getCause();
	}
	
	@Override
	public String toString() {
		String className = getClass().getSimpleName();
		return "["+className+"] " + getLocalizedMessage() + "\n";
	}
	
	/** Creates an unchecked Throwable, if not unchecked already. */
	public static final RuntimeException unchecked(Throwable e) {
		if(e instanceof RuntimeException) {
			throw (RuntimeException) e;
		} else if(e instanceof Exception) {
			throw new ExceptionAdapter((Exception) e);
		} else if(e instanceof Error) {
			throw (Error) e;
		} else {
			assertFail("uncheck: Unsupported Throwable: " + e);
			return null;
		}
	}
	
	/** Same as {@link #unchecked(Throwable)} but used during development for temporary code only. 
	 * Uses the Deprecated annotation to cause a warning in IDEs, to warn that code should be reviewed. */
	@Deprecated 
	public static RuntimeException uncheckedTODO(Throwable e) {
		return unchecked(e);
	}
}
