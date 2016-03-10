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
package melnorme.lang.ide.core.tests;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.osgi.service.datalocation.Location;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import melnorme.lang.ide.core.LangCorePlugin;
import melnorme.lang.ide.core.LangNature;
import melnorme.lang.ide.core.tests.utils.ErrorLogListener;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.utilbox.misc.MiscUtil;
import melnorme.utilbox.misc.StreamUtil;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.tests.CommonTest;
import melnorme.utilbox.tests.TestsWorkingDir;

/** 
 * Base core test class that adds an exception listener to the platform log. 
 * The ErrorLogListener was the only way I found to detect UI exceptions in SafeRunnable's 
 * when running as plugin test. 
 */
public abstract class CommonCoreTest extends CommonTest {
	
	static {
		initializeWorkingDirToEclipseInstanceLocation();
		
		disableAutoBuild();
		
		LangCorePlugin.getInstance().initializeAfterUIStart();
	}
	
	protected static void disableAutoBuild() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceDescription desc= workspace.getDescription();
		desc.setAutoBuilding(false);
		try {
			workspace.setDescription(desc);
		} catch (CoreException e) {
			throw melnorme.utilbox.core.ExceptionAdapter.unchecked(e);
		}
	}
	
	protected static ErrorLogListener logErrorListener;
	
	@BeforeClass
	public static void setUpExceptionListenerStatic() throws Exception {
		logErrorListener = new ErrorLogListener(true) {
			@Override
			public void handleErrorStatus(IStatus status, String plugin) {
				String msg = status.getMessage();
				
				// Ignore error about early start of debug plugin
				if(msg != null && msg.startsWith(EclipseUtils.MSG__ERROR_TRYING_TO_START_PLUGIN)) {
					return;
				}
				
				super.handleErrorStatus(status, plugin);
			}
		};
	}
	
	@AfterClass
	public static void checkLogErrorListenerStatic() throws Throwable {
		logErrorListener.checkErrorsAndUninstall();
	}
	
	public static void checkLogErrors_() throws Throwable {
		logErrorListener.checkErrors();
	}
	
	// the @Before and @After are not applied to the same method, so that better stack information can
	// be obtained the if the method fails its check.
	@Before
	public void checkLogErrors_before() throws Throwable {
		doCheckLogErrors();
	}
	@After
	public void checkLogErrors_after() throws Throwable {
		doCheckLogErrors();
	}
	
	protected void doCheckLogErrors() throws Throwable {
		checkLogErrors_();
	}
	
	private static void initializeWorkingDirToEclipseInstanceLocation() {
		Location instanceLocation = Platform.getInstanceLocation();
		try {
			URI uri = instanceLocation.getURL().toURI();
			Path workingDirPath = new File(uri).toPath().toAbsolutePath().resolve("TestsWD");
			TestsWorkingDir.initWorkingDir(workingDirPath.toString());
		} catch (URISyntaxException e) {
			throw assertFail();
		}
	}
	
	/* ----------------- utilities ----------------- */
	
	public static org.eclipse.core.runtime.Path epath(melnorme.utilbox.misc.Location loc) {
		return ResourceUtils.epath(loc);
	}
	public static org.eclipse.core.runtime.Path epath(Path path) {
		return ResourceUtils.epath(path);
	}
	
	public static Path path(IPath path) {
		return MiscUtil.createValidPath(path.toOSString());
	}
	
	public static IProject createAndOpenProject(String name, boolean overwrite) throws CoreException {
		return ResourceUtils.createAndOpenProject(name, overwrite);
	}
	
	public static void deleteProject(String projectName) throws CoreException {
		ResourceUtils.tryDeleteProject(projectName);
	}
	
	public static IFolder createFolder(IFolder folder) throws CoreException {
		folder.create(true, true, null);
		return folder;
	}
	
	public static void deleteResource(IResource resource) throws CoreException {
		resource.delete(false, new NullProgressMonitor());
	}
	
	/* -----------------  ----------------- */
	
	public static IProject createLangProject(String projectName, boolean overwrite) throws CoreException {
		IProject project = ResourceUtils.createAndOpenProject(projectName, overwrite);
		setupLangProject(project, false);
		return project;
	}
	
	public static void setupLangProject(IProject project) throws CoreException {
		setupLangProject(project, true);
	}
	
	public static void setupLangProject(IProject project, boolean requireWsLock) throws CoreException {
		assertTrue(project.exists());
		if(requireWsLock) {
			ISchedulingRule currentRule = Job.getJobManager().currentRule();
			assertTrue(currentRule != null && currentRule.contains(project));
		}
		EclipseUtils.addNature(project, LangNature.NATURE_ID);
	}
	
	public static String readFileContents(IFile file) throws CoreException, IOException {
		return StreamUtil.readStringFromReader(new InputStreamReader(file.getContents(), StringUtil.UTF8));
	}
	
	public static void writeStringToFile(IProject project, String filePath, String contents) 
			throws CoreException {
		IFile file = project.getFile(filePath);
		ResourceUtils.writeStringToFile(file, contents, null);
	}
	
}