/*******************************************************************************
 * Copyright (c) 2007, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial implementation
 *******************************************************************************/
package melnorme.utilbox.misc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

/**
 * Miscellaneous file utilities. 
 */
public final class FileUtil extends StreamUtil {
	
	/** Read all bytes of the given file. 
	 * @return the bytes that where read in a {@link ByteArrayOutputStream}. */
	public static IByteSequence readBytesFromFile(File file) throws IOException, FileNotFoundException {
		long fileLength = file.length();
		/*
		 * You cannot create an array using a long type. It needs to be an
		 * int type. Before converting to an int type, check to ensure
		 * that file is not larger than Integer.MAX_VALUE.
		 */
		if (fileLength > Integer.MAX_VALUE) 
			throw new IOException("File is too large, size is bigger than " + Integer.MAX_VALUE);
		
		return readAllBytesFromStream(new FileInputStream(file), (int) fileLength);
	}
	
	/** Read all bytes from the given file.
	 * @return a String created from those bytes, with given charsetName. */
	public static String readStringFromFile(File file, String charsetName) throws IOException, FileNotFoundException {
		return readBytesFromFile(file).toString(Charset.forName(charsetName));
	}
	
	/** Read all bytes from the given file.
	 * @return a String created from those bytes, with given charset. */
	public static String readStringFromFile(File file, Charset charset) throws IOException, FileNotFoundException {
		return readBytesFromFile(file).toString(charset);
	}
	
	
	/** Write the given array of bytes to given file */
	public static void writeBytesToFile(byte[] bytes, File file) throws IOException,
			FileNotFoundException {
		FileOutputStream fileOS = new FileOutputStream(file);
		writeBytesToStream(bytes, fileOS);
	}
	
	/** Writes given chars array to given writer. 
	 * Close writer afterwards. */
	public static void writeCharsToFile(char[] chars, File file, String charsetName) 
			throws IOException, FileNotFoundException {
		FileOutputStream fileOS = new FileOutputStream(file);
		OutputStreamWriter osWriter = new OutputStreamWriter(fileOS, charsetName);
		writeCharsToWriter(chars, osWriter);
	}
	
	/** Writes given string to given writer. 
	 * Close writer afterwards. */
	public static void writeStringToFile(String string, File file, String charsetName) 
			throws IOException, FileNotFoundException {
		FileOutputStream fileOS = new FileOutputStream(file);
		OutputStreamWriter osWriter = new OutputStreamWriter(fileOS, charsetName);
		writeStringToWriter(string, osWriter);
	}
	
}
