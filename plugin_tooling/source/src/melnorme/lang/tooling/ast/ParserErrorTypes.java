/*******************************************************************************
 * Copyright (c) 2011 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.tooling.ast;

import melnorme.lang.tooling.LANG_SPECIFIC;
import melnorme.lang.tooling.common.ParserError;

@LANG_SPECIFIC
public enum ParserErrorTypes {
	
	GENERIC_ERROR {
		@Override
		public String getUserMessage(ParserError pe) {
			return pe.msgErrorSource;
		}
	},
	
	;
	
	public abstract String getUserMessage(ParserError pe);
	
}