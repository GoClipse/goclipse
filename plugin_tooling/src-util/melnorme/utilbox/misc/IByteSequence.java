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
package melnorme.utilbox.misc;

import java.nio.charset.Charset;

/**
 * A byte sequence, similar to a byte array. 
 * Sometimes used instead of byte array for performance reasons (hiding internal buffers).
 */
public interface IByteSequence {
	
	public int byteAt(int index);
	
	public int getCount();
	
	public String toString(Charset charset);
	
	default String toUtf8String() {
		return toString(StringUtil.UTF8);
	}
	
	public byte[] toByteArray();
	
}