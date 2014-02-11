/*******************************************************************************
 * Copyright (c) 2007 DSource.org and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial implementation
 *******************************************************************************/
package melnorme.utilbox.misc;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

/**
 * Miscellaneous stream utilities. 
 */
public class StreamUtil {
	
	protected static final int EOF = -1;
	
	private static IOException createFailedToReadExpected(int length, int totalRead) {
		return new IOException("Failed to read requested amount of characters. " +
				"Read: " + totalRead + " of total requested: " + length);
	}
	
	
	/** Reads and returns all bytes from given inputStream until an EOF is read. 
	 * Closes inputStream afterwards. */
	public static byte[] readAllBytesFromStream(InputStream inputStream) throws IOException {
		try {
			final int BUFFER_SIZE = 1024;
			byte[] buffer = new byte[BUFFER_SIZE];
			byte[] bytes = new byte[0];
//			int totalRead = 0;
			
			int read;
			while((read = inputStream.read(buffer)) != EOF) {
				bytes = ArrayUtil.concat(bytes, buffer, read);
//				totalRead += read;
			}
			return bytes;
		} finally {
			inputStream.close();
		}
	}
	
	/** Reads and returns all chars from given reader until an EOF is read.
	 * Closes reader afterwards. */
	public static char[] readAllCharsFromReader(Reader reader) throws IOException {
		try {
			final int BUFFER_SIZE = 1024;
			char[] buffer = new char[BUFFER_SIZE];
			char[] chars = new char[0];
//			int totalRead = 0;
			
			int read;
			while((read = reader.read(buffer)) != EOF) {
				chars = ArrayUtil.concat(chars, buffer, read);
//				totalRead += read;
			}
			return chars;
		} finally {
			reader.close();
		}
	}
	
	/** Reads all chars from given reader until an EOF is read, and returns them as a String.
	 * Closes reader afterwards. */
	public static String readStringFromReader(Reader reader) throws IOException {
		return new String(readAllCharsFromReader(reader));
	}
	
	/** Reads given length amount of bytes from given inputStream, and returns them. 
	 *  Closes inputStream afterwards. 
	 *  @throws IOException if it fails to read given requestLength amount of elements. */
	public static byte[] readBytesFromStream(InputStream inputStream, int length) throws IOException {
		try {
			byte[] bytes = new byte[length];
			int totalRead = 0;
			do {
				int read = inputStream.read(bytes, totalRead, length - totalRead);
				if (read == -1) {
					throw createFailedToReadExpected(length, totalRead);
				}
				totalRead += read;
			} while (totalRead != length);
			return bytes;
		} finally {
			inputStream.close(); 
		}
	}
	
	/** Reads given length amount of chars from given reader, and returns them in a char[]. 
	 *  Throws IOException if it fails to read given length amount of elements.
	 *  Closes reader afterwards. 
	 */
	public static char[] readRequiredAmountFromReader(Reader reader, int length) throws IOException {
		char[] chars = readCharAmountFromReader(reader, length);
		if(chars.length != length) {
			throw createFailedToReadExpected(length, chars.length);
		}
		return chars;
	}
	
	/** Attempts to read given length amount of chars from given reader, and returns them in a char[]. 
	 *  Closes reader afterwards. */
	public static char[] readCharAmountFromReader(Reader reader, int length) throws IOException 
	{
		try {
			char[] chars = new char[length];
			int totalRead = 0;
			do {
				int read = reader.read(chars, totalRead, length - totalRead);
				if (read == -1) {
					break;
				}
				totalRead += read;
			} while (totalRead != length);
			return chars;
		} finally {
			reader.close(); 
		}
	}
	
	/** Reads and returns all available bytes from the given inputStream, as specified by 
	 * {@link InputStream#available()}. 
	 * Closes inputStream afterwards. */
	public static byte[] readAvailableBytesFromStream(InputStream inputStream) throws IOException {
		int availableToRead = inputStream.available();
		return readBytesFromStream(inputStream, availableToRead);
	}
	
	/** Writes given bytes array to given outputStream. 
	 * Closes outputStream afterwards. */
	public static void writeBytesToStream(byte[] bytes, OutputStream outputStream) throws IOException {
		// A BufferedOutputStream is likely not necessary since this is a one-time array write
		BufferedOutputStream bos = new BufferedOutputStream(outputStream);
		try {
			bos.write(bytes);
		} finally {
			bos.close();
		}
	}
	
	/** Writes given chars array to given writer. 
	 * Close writer afterwards. */
	public static void writeCharsToWriter(char[] chars, Writer writer) throws IOException {
		BufferedWriter bw = new BufferedWriter(writer);
		try {
			bw.write(chars);
		} finally {
			bw.close();
		}
	}
	
	/** Writes given string to given writer. 
	 * Close writer afterwards. */
	public static void writeStringToWriter(String string, Writer writer) throws IOException {
		BufferedWriter bw = new BufferedWriter(writer);
		try {
			bw.write(string);
		} finally {
			bw.close();
		}
	}
	
	
	/** Copies given length amount of bytes from given inputStream to given outputStream. 
	 * Alternatively, if length == -1, copy all bytes in inputStream until EOF.
	 * @throws IOException if it fails to read given length amount of elements. */
	public static void copyBytesToStream(InputStream inputStream, OutputStream outputStream, int length)
			throws IOException {
		final int BUFFER_SIZE = 1024;
		byte[] buffer = new byte[BUFFER_SIZE];
		
		int totalRead = 0;
		do {
			int readReqLen = (length == -1) ? BUFFER_SIZE : Math.min(BUFFER_SIZE, length - totalRead);
			int read = inputStream.read(buffer, 0, readReqLen);
			if (read == -1) {
				if (length != -1) {
					throw createFailedToReadExpected(length, totalRead);
				} else {
					return; // Nothing more to copy.
				}
			}
			totalRead += read;
			outputStream.write(buffer, 0, read);
		} while (totalRead != length);
	}

	/** Copies all bytes in given inputStream (until EOF) to given outputStream. 
	 * Closes inputStream and outputStream. */
	public static void copyStream(InputStream inputStream, OutputStream outputStream) throws IOException {
		copyStream(inputStream, outputStream, true);
	}
	
	/** Copies all bytes in given inputStream (until EOF) to given outputStream. 
	 * Closes inputStream and also outputStream if given closeOut is true. */
	public static void copyStream(InputStream inputStream, OutputStream outputStream, boolean closeOut) 
			throws IOException {
		try {
			copyBytesToStream(inputStream, outputStream, -1);
		} finally {
			try {
				inputStream.close();
			} finally {
				if(closeOut) {
					outputStream.close();
				}
			}
		}
	}
	
	
	/** Closes given inputStream, either ignoring IOExceptions, 
	 * or rethrowing them unchecked, according to given rethrowAsUnchecked. */
	public static void uncheckedClose(InputStream inStream, boolean rethrowAsUnchecked) {
		try {
			inStream.close();
		} catch (IOException e) {
			if(rethrowAsUnchecked)
				throw melnorme.utilbox.core.ExceptionAdapter.unchecked(e);
		}
	}
	
	/** Closes given reader, either ignoring IOExceptions, 
	 * or rethrowing them unchecked, according to given rethrowAsUnchecked. */
	public static void uncheckedClose(Reader reader, boolean rethrowAsUnchecked) {
		try {
			reader.close();
		} catch (IOException e) {
			if(rethrowAsUnchecked)
				throw melnorme.utilbox.core.ExceptionAdapter.unchecked(e);
		}
	}
	
}
