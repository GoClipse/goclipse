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
package melnorme.lang.tooling.bundle;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.CoreUtil.areEqual;

import java.nio.file.Path;
import java.text.MessageFormat;

import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.HashcodeUtil;
import melnorme.utilbox.misc.PathUtil;
import melnorme.utilbox.misc.StringUtil;

public class FileRef {
	
	protected final String binaryPath;
	protected final String sourcePath;
	
	public FileRef(String binaryPath, String sourcePath) {
		this.binaryPath = assertNotNull(binaryPath);
		this.sourcePath = sourcePath;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof FileRef)) return false;
		
		FileRef other = (FileRef) obj;
		
		return 
			areEqual(binaryPath, other.binaryPath) &&
			areEqual(sourcePath, other.sourcePath)
		;
	}
	
	@Override
	public int hashCode() {
		return HashcodeUtil.combinedHashCode(binaryPath, sourcePath);
	}
	
	@Override
	public String toString() {
		return MessageFormat.format("{0}@`{1}`", binaryPath, StringUtil.nullAsEmpty(sourcePath));
	}
	
	/* -----------------  ----------------- */
	
	public String getBinaryPathString() {
		return binaryPath;
	}
	
	public String getSourcePathString() {
		return sourcePath;
	}
	
	public Path getBinaryPath() throws CommonException {
		return asPath(binaryPath);
	}
	
	public Path getSourcePath() throws CommonException {
		return asPath(sourcePath);
	}
	
	public static Path asPath(String pathString) throws CommonException {
		return pathString == null ? null: PathUtil.createPath(pathString);
	}
	
}