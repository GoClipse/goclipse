package melnorme.lang.ide.core.engine;
/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.util.concurrent.CountDownLatch;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.filebuffers.LocationKind;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.junit.Test;

import melnorme.lang.ide.core.engine.SourceModelManager.StructureModelRegistration;
import melnorme.lang.ide.core.engine.SourceModelManager.StructureUpdateTask;
import melnorme.lang.ide.core.engine.StructureModelTest.FixtureSourceModelManager;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.operations.build.ProjectBuildInfo;
import melnorme.lang.ide.core.tests.CommonCoreTest;
import melnorme.lang.ide.core.tests.SampleProject;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.tooling.LocationKey;
import melnorme.lang.tooling.common.ParserError;
import melnorme.lang.tooling.structure.SourceFileStructure;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

class AnnotationsModelManager {
	
	/**
	 * @param project  
	 */
	public Indexable<?> getAnnotations(IProject project) {
		return Indexable.EMPTY_INDEXABLE;
	}
	
}

public class ModelReconcilationTest extends CommonCoreTest {
	
	protected ITextFileBufferManager fbm = FileBuffers.getTextFileBufferManager();
	protected AnnotationsModelManager annotationsMgr = new AnnotationsModelManager();
	
	protected SampleProject fixtureProject;
	
	@Override
	public void dispose() throws Exception {
		if(fixtureProject != null) {
			fixtureProject.cleanUp();
		}

		super.dispose();
	}
	
	public void initProject() throws CoreException, CommonException {
		fixtureProject = new SampleProject(ModelReconcilationTest.class.getSimpleName()) {
			@Override
			protected void fillProject() throws CoreException {
				IFolder folder = getProject().getFolder("folder");
				createFolder(folder);
				
				ResourceUtils.writeStringToFile(folder.getFile("fileA"), "default contents A", null);
				ResourceUtils.writeStringToFile(folder.getFile("fileB"), "default contents B", null);
			}
		};
	}
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() throws Exception {
		initProject();
		
		FixtureSourceModelManager sourceModelMgr = new FixtureSourceModelManager();
		
		BuildManager buildMgr = sourceModelMgr.reconcileMgr.projectReconciler.buildMgr;
		disableAutoBuilds(buildMgr.getBuildInfo(fixtureProject.project));
		
		IFile fileA = fixtureProject.getFile("folder/fileA");
		try(BufferFixture helper = new BufferFixture(fbm, fileA.getFullPath(), LocationKind.IFILE, sourceModelMgr)) {
			
			assertTrue(annotationsMgr.getAnnotations(fixtureProject.project).size() == 0);
			
			LocationKey locationKey = new LocationKey(location(fileA));
			StructureModelRegistration reg = sourceModelMgr.connectStructureUpdates(locationKey, helper.getDocument(), 
				IStructureModelListener.NIL_LISTENER);
			
			// A structure working copy is now connected
			
			// Do a doc change
			helper.instrumentedChangeDocument("change1 xxx");
			
			helper.createStructureTask_EntryLatch.countDown();
			
			reg.structureInfo.awaitUpdatedData();
			
			// TODO
			//assertTrue(annotationsMgr.getAnnotations(fixtureProject.project).size() == 1);
			
			helper.sourceModelMgr.updateTaskProvider = helper.sourceModelMgr.DEFAULT_UPDATE_TASK;
			
			// test revert - isDirty
			assertTrue(helper.buffer.isDirty());
			helper.buffer.revert(new NullProgressMonitor());
			
			// test revert - isDirty == false (regression)
			assertTrue(helper.buffer.isDirty() == false);
			helper.buffer.revert(new NullProgressMonitor());
			
			reg.dispose();
			
		};
		
	}
	
	protected void disableAutoBuilds(ProjectBuildInfo buildInfo) {
		buildInfo.getBuildTargets().forEach((bt) -> {
			try {
				// Disable the auto/check build
				buildInfo.getUpdatedInfo().changeEnable(bt.getBuildTargetName(), bt.isNormalBuildEnabled(), false);
			} catch(Exception e) {
				throw melnorme.utilbox.core.ExceptionAdapter.unchecked(e);
			}
		});
	}
	
	/* -----------------  ----------------- */
	
	public static class BufferFixture implements AutoCloseable {
		
		protected final ITextFileBufferManager fbm;
		protected final FixtureSourceModelManager sourceModelMgr;
		
		public final ITextFileBuffer buffer;
		public final IPath path;
		public final LocationKind locationKind;
		
		protected final IProgressMonitor pm = null;
		
		public BufferFixture(ITextFileBufferManager fbm, IPath path, LocationKind locationKind, 
				FixtureSourceModelManager sourceModelMgr) throws CoreException {
			this.path = path;
			this.locationKind = locationKind;
			this.sourceModelMgr = sourceModelMgr;
			this.fbm = assertNotNull(fbm);
			fbm.connect(path, locationKind, pm);
			buffer = fbm.getTextFileBuffer(path, locationKind);
		}
		
		public IDocument getDocument() {
			return buffer.getDocument();
		}
		
		@Override
		public void close() throws Exception {
			fbm.disconnect(path, locationKind, pm);
		}
		
		/* -----------------  ----------------- */
		
		protected CountDownLatch createStructureTask_EntryLatch = new CountDownLatch(0);
		
		public void instrumentedChangeDocument(String contents) {
			assertTrue(createStructureTask_EntryLatch.getCount() == 0);
			
			createStructureTask_EntryLatch = new CountDownLatch(1);
			
			sourceModelMgr.updateTaskProvider = (structureInfo) -> {
				sourceModelMgr.updateTaskProvider = null;
				
//				String source = structureInfo.document.get();
				
				return new StructureUpdateTask(structureInfo) {
					@Override
					protected SourceFileStructure doCreateNewData() throws OperationCancellation {
						try {
							createStructureTask_EntryLatch.await();
						} catch(InterruptedException e) {
							throw new OperationCancellation();
						}

						return new SourceFileStructure(null, null, (Indexable<ParserError>) null);
					}
				};
			};
			
			getDocument().set(contents);
		}
		
	}
	
}