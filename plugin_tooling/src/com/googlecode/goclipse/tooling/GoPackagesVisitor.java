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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.googlecode.goclipse.tooling.env.GoPath;

public abstract class GoPackagesVisitor {
	
	protected final Path goPathEntry;
	protected final HashMap<GoPackageName, Path> modules = new HashMap<>();
	protected final HashSet<Path> moduleFiles = new HashSet<>();
	
	public GoPackagesVisitor(Path goPathEntry, List<Path> directoriesToVisit) {
		this.goPathEntry = goPathEntry;
		assertNotNull(goPathEntry);
		
		for (Path directoryToVisit : directoriesToVisit) {
			try {
				if(directoryToVisit.equals(goPathEntry)) {
					directoryToVisit = directoryToVisit.resolve(GoPath.SRC_DIR);
				}
				visitFolder(directoryToVisit);
			} catch (IOException e) {
				throw assertFail("Should not happen, file visit should not throw exception");
			}
		}
	}
	
	protected abstract FileVisitResult handleFileVisitException(Path file, IOException exc);
	
	protected void visitFolder(final Path startingDir) throws IOException {
		if(!startingDir.toFile().exists()) {
			return;
		}
		Files.walkFileTree(startingDir, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				assertTrue(dir.startsWith(startingDir));
				
				String fileName = dir.getFileName().toString();
				if(isIgnoredName(fileName)) {
					return FileVisitResult.SKIP_SUBTREE;
				}
				
				if(isDirectoryAValidGoPackage(dir)) {
					addEntry(GoPath.getGoPackageForPath(goPathEntry, dir), dir);
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
		Files.walkFileTree(goPackageDir, new HashSet<FileVisitOption>(), 1, checkSourceFiles);
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
	
	public HashSet<Path> getModuleFiles() {
		return moduleFiles;
	}
	
	public Set<GoPackageName> getModuleNames() {
		return modules.keySet();
	}
	
}