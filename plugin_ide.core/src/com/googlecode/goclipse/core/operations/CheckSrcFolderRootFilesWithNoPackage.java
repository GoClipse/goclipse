package com.googlecode.goclipse.core.operations;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.eclipse.core.runtime.CoreException;

import melnorme.lang.ide.core.LangCore;
import melnorme.utilbox.misc.Location;

public class CheckSrcFolderRootFilesWithNoPackage extends SimpleFileVisitor<Path> {
	
	protected Path startDir = null;
	protected boolean containsGoSources = false;
	
	public void checkDir(Location dir) throws CoreException {
		try {
			Files.walkFileTree(dir.path, this);
		} catch (IOException e) {
			throw LangCore.createCoreException("Error walking file tree", e);
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