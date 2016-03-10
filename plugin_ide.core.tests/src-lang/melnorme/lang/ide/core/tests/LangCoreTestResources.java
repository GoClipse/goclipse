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
package melnorme.lang.ide.core.tests;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.io.File;
import java.io.IOException;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.LangCorePlugin;
import melnorme.lang.ide.core.tests.utils.BundleResourcesUtil;
import melnorme.lang.utils.MiscFileUtils;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;

public class LangCoreTestResources {
	
	public static void createFolderFromCoreTestsResource(String resourcePath, IContainer destFolder)
			throws CoreException {
		File destFolder_File = destFolder.getLocation().toFile();
		copyFolderContentsFromCoreTestsResource(resourcePath, destFolder_File);
		
		destFolder.refreshLocal(IResource.DEPTH_INFINITE, null);
		assertTrue(destFolder.exists());
	}
	
	protected static final String TESTDATA_BUNDLE_PATH = "testdata/";
	
	/** Copies the contents of a test bundle resource folder into given destFolder destination */
	public static void copyFolderContentsFromCoreTestsResource(String resourcePath, File destFolder) 
			throws CoreException {
		String pluginId = LangCorePlugin.TESTS_PLUGIN_ID;
		String bundleResourcePath = new Path(TESTDATA_BUNDLE_PATH).append(resourcePath).toString();
		try {
			BundleResourcesUtil.copyDirContents(pluginId, bundleResourcePath, destFolder);
		} catch(IOException e) {
			throw LangCore.createCoreException("Error copying resource contents", e);
		}
	}
	
	public static IFolder createFolderFromDirectory(File directory, IProject project, String destFolderName)
			throws CoreException {
		IFolder destFolder = project.getFolder(destFolderName);
		try {
			MiscFileUtils.copyDirContentsIntoDirectory(directory, destFolder.getLocation().toFile());
		} catch (IOException e) {
			throw LangCore.createCoreException("Error in copyDirContentsIntoDirectory", e);
		}
		destFolder.refreshLocal(IResource.DEPTH_INFINITE, null);
		return destFolder;
	}
	
}