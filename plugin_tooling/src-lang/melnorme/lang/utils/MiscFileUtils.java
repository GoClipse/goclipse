package melnorme.lang.utils;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import melnorme.lang.tests.CommonToolingTest;
import melnorme.utilbox.core.fntypes.Function;

/**
 * Miscellaneous utils relating to {@link File}'s.
 * The semantics of these util methods may not be robust or precise enough to be used outside of test code.
 */
public class MiscFileUtils {
	
	public static File getFile(File file, String... segments) {
		for (String segment : segments) {
			assertTrue(segment.contains("/") == false && segment.contains("\\") == false);
			file = new File(file, segment);
		}
		return file;
	}
	
	public static File getFile(String rootPath, String... segments) {
		File file = new File(rootPath);
		return getFile(file, segments);
	}
	
	public static class FileTraverser {
		
		protected File rootDir;
		
		public void traverseDirectory(File dir) throws IOException {
			assertTrue(dir.exists() && dir.isDirectory());
			rootDir = dir;
			traverseFileOrDir(dir);
		}
		
		public void traverseFileOrDir(File file) throws IOException {
			assertTrue(file.exists());
			
			if(file.isDirectory()) {
				visitDirectory(file);
			} else {
				Path relativePath = rootDir == null ? null : rootDir.toPath().relativize(file.toPath());
				visitFile(file, relativePath);
			}
		}
		
		protected void visitDirectory(File dir) throws IOException {
			File[] children = dir.listFiles(getDefaultDirFilter());
			assertTrue(children != null);
			
			for (File file : children) {
				traverseFileOrDir(file);
			}
		}
		
		protected FilenameFilter getDefaultDirFilter() {
			return null;
		}
		
		@SuppressWarnings("unused")
		protected void visitFile(File file, Path relativePath) throws IOException {
		}
		
	}
	
	public static void copyDirContentsIntoDirectory(File sourceDir, File destFolder) {
		try {
			new FileCopyTraverser(destFolder).traverseDirectory(sourceDir);
		} catch(IOException e) {
			throw melnorme.utilbox.core.ExceptionAdapter.unchecked(e);
		}
	}
	
	public static void copyDirContentsIntoDirectory(Path sourceDir, Path destFolder) throws IOException {
		File sourcePath = sourceDir.toFile();
		assertTrue(sourcePath.exists());
		new FileCopyTraverser(destFolder.toFile()).traverseDirectory(sourcePath);
	}
	
	public static final class FileCopyTraverser extends FileTraverser {
		protected final File destFolder;
		
		private FileCopyTraverser(File destFolder) {
			this.destFolder = destFolder;
		}
		
		@Override
		protected void visitFile(File file, Path relativePath) throws IOException {
			Path targetPath = destFolder.toPath().resolve(relativePath);
			File targetPathParent = new File(targetPath.toString()).getParentFile();
			targetPathParent.mkdirs();
			assertTrue(targetPathParent.exists());
			Files.copy(file.toPath(), targetPath);
		}
	}
	
	public static void traverseFiles(File folder, boolean recurseDirs, Function<File, Void> fileVisitor) {
		traverseFiles(folder, recurseDirs, fileVisitor, null);
	}
	
	public static void traverseFiles(File folder, boolean recurseDirs, Function<File, Void> fileVisitor,
			FilenameFilter filter) {
		assertTrue(folder.exists() && folder.isDirectory());
		File[] children = folder.listFiles(filter);
		assertNotNull(children);
		
		for (File file : children) {
			if(file.isDirectory() && recurseDirs) {
				fileVisitor.evaluate(file);
				traverseFiles(file, recurseDirs, fileVisitor, filter);
			} else {
				fileVisitor.evaluate(file);
			}
		}
	}
	
	
	public static void unzipFile(File zipFile, File parentDir) throws IOException {
		unzipFile(zipFile, parentDir, CommonToolingTest.testsLogger);
	}
	
	public static void unzipFile(File zipFile, File parentDir, PrintStream logger) throws ZipException, IOException {
		ZipFile zip = new ZipFile(zipFile);
		logger.println("== Unzipping: " + zipFile);
		try {
			Enumeration<? extends ZipEntry> entries = zip.entries();
			
			while(entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				
				File entryTargetFile = new File(parentDir, entry.getName());
				
				if(entry.isDirectory()) {
					entryTargetFile.mkdirs();
					continue;
				}
				
				entryTargetFile.getParentFile().mkdirs();
				Path destPath = entryTargetFile.toPath();
				logger.println("Unzipped: " + entry);
				logger.println("  to: " + destPath);
				Files.copy(zip.getInputStream(entry), destPath);
			}
			
		} finally {
			zip.close();
		}
	}
	
}