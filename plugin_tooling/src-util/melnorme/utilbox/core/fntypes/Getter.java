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
package melnorme.utilbox.core.fntypes;

/**
 * An {@link ICallable} for simple callables, typically:
 *  ones not having any side effects, nor performing long-waiting computation.
 */
public interface Getter<RET, EXC extends Exception> extends ICallable<RET, EXC> {
	
	@Override
	default RET call() throws EXC {
		return get();
	}
	
	public RET get() throws EXC;
	
}