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
package com.googlecode.goclipse.tooling;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;

import com.googlecode.goclipse.tooling.env.GoWorkspaceLocation;

import melnorme.utilbox.collections.Collection2;
import melnorme.utilbox.collections.HashSet2;
import melnorme.utilbox.collections.LinkedHashMap2;
import melnorme.utilbox.misc.Location;

public abstract class GoPackagesVisitor {
	
	protected final Location sourceRoot;
	protected final LinkedHashMap2<GoPackageName, Path> modules = new LinkedHashMap2<>();
	protected final HashSet2<Path> moduleFiles = new HashSet2<>();
	
	public GoPackagesVisitor(GoWorkspaceLocation goWorkspace, Location directoryToVisit) {
		this.sourceRoot = goWorkspace.getSrcLocation();
		if(!directoryToVisit.startsWith(sourceRoot)) {
			return;
		}
		
		try {
			visitFolder(directoryToVisit);
		} catch (IOException e) {
			throw assertFail("Should not happen, file visit should not throw exception");
		}
	}
	
	protected abstract FileVisitResult handleFileVisitException(Path file, IOException exc);
	
	protected void visitFolder(final Location startingDir) throws IOException {
		if(!startingDir.toFile().exists()) {
			return;
		}
		Files.walkFileTree(startingDir.toPath(),
			EnumSet.of(FileVisitOption.FOLLOW_LINKS),
			Integer.MAX_VALUE,
			new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					assertTrue(dir.startsWith(startingDir.path));
					Location dirLoc = Location.create_fromValid(dir);
					
					String fileName = dir.getFileName().toString();
					if(isIgnoredName(fileName)) {
						return FileVisitResult.SKIP_SUBTREE;
					}
					
					if(isDirectoryAValidGoPackage(dir)) {
						GoPackageName goPackageName = GoPackageName.fromPath(sourceRoot.relativize(dirLoc));
						addEntry(goPackageName, dir);
					}
					
					return FileVisitResult.CONTINUE;
				}
				
				@Override
				public FileVisitResult visitFile(Path filePath, BasicFileAttributes attrs) throws IOException {
					return FileVisitResult.CONTINUE;
				}
				
				@Override
				public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
					return handleFileVisitException(file, exc);
				}
				
			});
	}
	
	protected boolean isIgnoredName(String fileName) {
		return fileName.startsWith("_") || fileName.startsWith(".");
	}
	
	protected boolean isDirectoryAValidGoPackage(final Path goPackageDir) throws IOException {
		CheckDirectoryHasGoSourceFiles checkSourceFiles = new CheckDirectoryHasGoSourceFiles();
		Files.walkFileTree(goPackageDir, EnumSet.of(FileVisitOption.FOLLOW_LINKS), 1, checkSourceFiles);
		return checkSourceFiles.hasGoSourceFiles;
	}
	
	public class CheckDirectoryHasGoSourceFiles extends SimpleFileVisitor<Path> {
		
		public boolean hasGoSourceFiles = false;
		
		@Override
		public FileVisitResult visitFile(Path filePath, BasicFileAttributes attrs) throws IOException {
			String fileName = filePath.getFileName().toString();
			if(!attrs.isDirectory() && fileName.endsWith(".go") && !isIgnoredName(fileName)) {
				this.hasGoSourceFiles = true;
				return FileVisitResult.TERMINATE;
			}
			return FileVisitResult.CONTINUE;
		}
		
		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
			return handleFileVisitException(file, exc);
		}
	}
	
	protected void addEntry(GoPackageName packageName, Path fullPath) {
		assertNotNull(packageName);
		if(packageName.getFullNameAsString().isEmpty()) {
			// ignore
			return;
		}
		modules.put(packageName, fullPath);
		moduleFiles.add(fullPath);
	}
	
	public HashSet2<Path> getModuleFiles() {
		return moduleFiles;
	}
	
	public Collection2<GoPackageName> getModuleNames() {
		return modules.getKeysView();
	}
	
}