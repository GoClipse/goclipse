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
package melnorme.utilbox.core.fntypes;

import java.util.concurrent.Callable;


/**
 * A variant of {@link Callable} with a stricter API: 
 * allows specifying a more specific Throwable that the {@link Callable#call()} method throws.
 * 
 * NOTE: this functional method  must have a different name than {@link Callable#call()}, 
 * because of a javac compiler bugs that will prevent CallableX to be a {@link FunctionalInterface}.
 * 
 */
@FunctionalInterface
public interface Callable2<RET, EXC extends Throwable> {
	
	// This needs to have a different name than call
	public RET invoke() throws EXC;
	
	/* -----------------  ----------------- */
	
	default Result<RET, EXC> callToResult() {
		return Result.callToResult(this);
	}
	
}