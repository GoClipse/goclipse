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
import java.nio.file.Path;

import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.PathUtil;


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
	
	private Location testResourcesDir;
	
	public LangToolingTestResources() {
		String testResourcesDir = System.getProperty(TEST_RESOURCES_BASE_DIR_PROPERTY);
		if(testResourcesDir == null) {
			// Assume a default based on process working directory
			// This is so test can be started from typical Eclipse workspace without setting up VM properties
			testResourcesDir = "../plugin_tooling/"+TESTDATA;
		}
		Path path = PathUtil.createValidPath(testResourcesDir);
		this.testResourcesDir = Location.create_fromValid(path.toAbsolutePath());
		System.out.println("[==] testResourcesDir: " + testResourcesDir);
	}
	
	public Location getResourcesDir() {
		assertTrue(testResourcesDir != null);
		File file = testResourcesDir.toFile();
		assertTrue(file.exists() && file.isDirectory());
		return testResourcesDir;
	}
	
	public static File getTestResourceFile(String... segments) {
		return getTestResourceLoc(segments).toFile();
	}
	public static Path getTestResourcePath(String... segments) {
		return getTestResourceLoc(segments).toPath();
	}
	
	public static Location getTestResourceLoc(String... segments) {
		Location loc = getInstance().getResourcesDir();
		for (String pathSegment : segments) {
			loc = loc.resolve_fromValid(pathSegment);
		}
		assertTrue(loc.toFile().exists());
		return loc;
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