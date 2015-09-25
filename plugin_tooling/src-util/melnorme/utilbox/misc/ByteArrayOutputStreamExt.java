/*******************************************************************************
 * Copyright (c) 2013 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.utilbox.misc;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

/**
 * Some minor extensions to {@link ByteArrayOutputStream}.
 * Allows accessing the internal buffer and count value
 */
public class ByteArrayOutputStreamExt extends ByteArrayOutputStream implements IByteSequence {
	
	public ByteArrayOutputStreamExt() {
		super();
	}
	
	public ByteArrayOutputStreamExt(int size) {
		super(size);
	}
    
	public synchronized byte[] getInternalBuffer() {
		return buf;
	}
	
	@Override
	public synchronized int byteAt(int index) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException();
        }
		return buf[index];
	}
	
	@Override
	public synchronized int getCount() {
		return count;
	}
	
	@Override
	public synchronized String toString() {
		return toString(StringUtil.UTF8);
	}
	
	@Override
	public synchronized String toString(Charset charset) {
		return new String(buf, 0, count, charset);
	}
	
	@Override
	public synchronized byte[] toByteArray() {
		return super.toByteArray();
	}
	
}