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
 * A {@link Callable2} that is also a {@link Callable}. 
 * The EXC type parameter must an {@link Exception}, not a {@link Throwable} 
 */
@FunctionalInterface
public interface CallableX<RET, EXC extends Exception> extends Callable2<RET, EXC>, Callable<RET> {
	
	@Override
	public RET call() throws EXC;
	
	@Override
	default RET invoke() throws EXC {
		return call();
	}
	
}