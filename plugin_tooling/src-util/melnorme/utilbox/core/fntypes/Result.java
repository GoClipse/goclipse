/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.utilbox.core.fntypes;

public class Result<DATA, EXC extends Throwable> {
	
	protected volatile DATA resultValue = null;
	/** Note: resultException is either a EXC, or a RuntimeException. */
	protected volatile Throwable resultException;
	
	public Result(DATA resultValue) {
		this.resultValue = resultValue;
		this.resultException = null;
	}
	
	public Result(@SuppressWarnings("unused") Void dummy, EXC resultException) {
		this.resultException = resultException;
	}
	
	public DATA get() throws EXC {
		throwIfExceptionResult();
		return resultValue;
	}
	
	public boolean isSuccessful() {
		return resultException == null;
	}
	
	public boolean isException() {
		return !isSuccessful();
	}
	
	public DATA getOrNull() {
		try {
			return get();
		} catch(RuntimeException e) {
			throw e;
		} catch(Throwable e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void throwIfExceptionResult() throws EXC  {
		if(resultException instanceof RuntimeException) {
			throw (RuntimeException) resultException;
		}
		if(resultException != null) {
			throw (EXC) resultException;
		}
	}
	
}