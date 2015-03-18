/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.utilbox.tests;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import melnorme.utilbox.misc.FileUtil;
import melnorme.utilbox.misc.Location;

public class TestsWorkingDir {
	
	protected static final String TEST_RESOURCES_WORKING_DIR_PROPERTY = "Melnorme.TestsWorkingDir";
	
	protected static String testsWorkingDir;
	
	public static void initWorkingDir(String workingDir) {
		assertTrue(workingDir != null);
		assertTrue(testsWorkingDir == null);
		testsWorkingDir = workingDir;
		
		System.out.println("====>> WORKING DIR: " + testsWorkingDir);
		
		File file = new File(testsWorkingDir);
		if(!file.exists()) {
			file.mkdir();
		}
		assertTrue(file.toPath().isAbsolute());
	}
	
	protected static void defaultWorkingDirInit() {
		if(testsWorkingDir != null) 
			return;
		
		// default init:
		String property = System.getProperty(TEST_RESOURCES_WORKING_DIR_PROPERTY);
		if(property != null) {
			initWorkingDir(property);
		} else {
			initWorkingDir(System.getProperty("java.io.tmpdir") + "/_tests");
		}
	}
	
	public static Location getWorkingDir() {
		defaultWorkingDirInit(); // attempt default init
		
		assertNotNull(testsWorkingDir);
		File file = new File(testsWorkingDir);
		assertTrue(file.exists() && file.isDirectory() && file.isAbsolute());
		return Location.create_fromValid(file.toPath()); // Tests code, so assume valid
	}
	
	public static File getWorkingDirFile() {
		return getWorkingDir().toFile();
	}
	
	public static Path getWorkingDirPath() {
		return getWorkingDir().getPath();
	}
	
	public static Location getWorkingDir(String relativePath) {
		return getWorkingDir().resolve_fromValid(relativePath);
	}
	
	/** Delete given dir, but for safety, check that it is within TestsWorkingDir. */
	public static void deleteDir(Location dir) throws IOException {
		assertTrue(dir.startsWith(getWorkingDir()));
		FileUtil.deleteDir(dir);
	}
	
}