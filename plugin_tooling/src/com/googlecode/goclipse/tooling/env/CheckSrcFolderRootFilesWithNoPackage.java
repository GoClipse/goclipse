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
package com.googlecode.goclipse.tooling.env;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;

import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

public class CheckSrcFolderRootFilesWithNoPackage extends SimpleFileVisitor<Path> {
	
	protected Path startDir = null;
	public boolean containsGoSources = false;
	
	public void checkDir(Location dir) throws CommonException {
		try {
			Files.walkFileTree(dir.path, EnumSet.of(FileVisitOption.FOLLOW_LINKS), 1, this);
		} catch (IOException e) {
			throw new CommonException("Error walking file tree", e);
		}
	}
	
	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		if(startDir == null) {
			this.startDir = dir;
			return super.preVisitDirectory(dir, attrs);
		} else {
			return FileVisitResult.TERMINATE; // Start directory only
		}
	}
	
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		if(file.getFileName().toString().endsWith(".go")) {
			containsGoSources = true;
			return FileVisitResult.TERMINATE;
		}
		return FileVisitResult.CONTINUE;
	}
	
}