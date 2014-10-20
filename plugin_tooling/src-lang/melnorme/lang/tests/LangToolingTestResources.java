/*******************************************************************************
 * Copyright (c) 2008, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/

package melnorme.lang.tests;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import melnorme.lang.utils.MiscFileUtils;


public class LangToolingTestResources {
	
	protected static final String TEST_RESOURCES_BASE_DIR_PROPERTY = "TestsResourcesDir";
	
	protected static final String TESTDATA = "testdata";
	
	protected static LangToolingTestResources instance;
	
	// lazy loaded
	public static synchronized LangToolingTestResources getInstance() {
		if(instance == null) {
			instance = new LangToolingTestResources();
		}
		return instance;
	}
	
	private String testResourcesDir;
	
	public LangToolingTestResources() {
		testResourcesDir = System.getProperty(TEST_RESOURCES_BASE_DIR_PROPERTY);
		if(testResourcesDir == null) {
			// Assume a default based on process working directory
			// This is so test can be started from typical Eclipse workspace without setting up VM properties
			testResourcesDir = "../plugin_tooling/"+TESTDATA;
		}
		System.out.println("testResourcesDir:" + testResourcesDir);
	}
	
	public File getResourcesDir() {
		File file = new File(testResourcesDir).toPath().toAbsolutePath().normalize().toFile();
		assertTrue(file.exists() && file.isDirectory() && file.isAbsolute());
		return file;
	}
	
	public static File getTestResource(String... segments) {
		return MiscFileUtils.getFile(LangToolingTestResources.getInstance().getResourcesDir(), segments);
	}
	
	public static Path getTestResourcePath(String... segments) {
		try {
			Path path = getTestResource(segments).getCanonicalFile().toPath();
			assertTrue(path.toFile().exists());
			return path;
		} catch (IOException e) {
			throw melnorme.utilbox.core.ExceptionAdapter.unchecked(e);
		}
	}
	
	public static String resourceFileToString(File file) {
		return resourceFileToString(file, TESTDATA);
	}
	
	public static String resourceFileToString(File file, String rootDir) {
		if(file.getName().equals(rootDir)) {
			return "#";
		} else {
			File parentFile = file.getParentFile();
			String parentStr = (parentFile != null) ? resourceFileToString(parentFile, rootDir) : ""; 
			return parentStr + "/" + file.getName();
		}
	}
	
}