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
package melnorme.lang.utils.parse;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import melnorme.utilbox.core.CommonException;

public class BasicCharSource_CommonExceptionAdapter implements IBasicCharSource<CommonException> {
	
	protected final IBasicCharSource<? extends Exception> source;
	
	public BasicCharSource_CommonExceptionAdapter(IBasicCharSource<? extends Exception> source) {
		this.source = assertNotNull(source);
	}
	
	@Override
	public int lookahead() throws CommonException {
		try {
			return source.lookahead();
		} catch(Exception e) {
			throw new CommonException(getDefaultMessage(), e);
		}
	}
	
	@Override
	public char consume() throws CommonException {
		try {
			return source.consume();
		} catch(Exception e) {
			throw new CommonException(getDefaultMessage(), e);
		}
	}
	
	protected String getDefaultMessage() {
		return "Error reading from char source.";
	}
	
}