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
 * A {@link Callable} with a stricter API: 
 * allows specifying a more specific Exception that the {@link #call()} method throws.
 */
public interface CallableX<RET, EXC extends Exception> extends Callable<RET> {
	
	@Override
	public RET call() throws EXC;
	
}