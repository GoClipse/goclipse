/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.core.engine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.engine.SourceModelManager;
import melnorme.lang.tooling.structure.SourceFileStructure;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.FileUtil;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

import org.eclipse.core.runtime.CoreException;
import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.core.GoToolPreferences;
import com.googlecode.goclipse.tooling.GoSourceFileUtil;
import com.googlecode.goclipse.tooling.env.GoEnvironment;
import com.googlecode.goclipse.tooling.env.GoPath;
import com.googlecode.goclipse.tooling.oracle.GoOracleDescribeOperation;
import com.googlecode.goclipse.tooling.oracle.GoOraclePackageDescribeParser;

public class GoSourceModelManager extends SourceModelManager {
	
	public GoSourceModelManager() {
	}
	
	@Override
	protected StructureUpdateTask createUpdateTask(StructureInfo structureInfo, String source) {
		return createUpdateTask(structureInfo, source, false);
	}
	
	@Override
	protected StructureUpdateTask createUpdateTask_forFileSave(StructureInfo structureInfo, String source) {
		return createUpdateTask(structureInfo, source, true);
	}
	
	protected StructureUpdateTask createUpdateTask(StructureInfo structureInfo, String source, boolean isSavedToDisk) {
		Location fileLocation = structureInfo.getLocation();
		if(fileLocation == null) {
			return new StructureUpdateNullTask(structureInfo);
		}
		
		return new GoStructureUpdateTask(structureInfo, source, fileLocation, isSavedToDisk);
	}
	
	protected class GoStructureUpdateTask extends StructureUpdateTask {
		
		protected final String source;
		protected final Location fileLocation;
		protected final boolean isDocumentSavedToDisk;
		
		public GoStructureUpdateTask(StructureInfo structureInfo, String source, Location fileLocation, 
				boolean isSavedToDisk) {
			super(structureInfo);
			this.source = source;
			this.fileLocation = fileLocation;
			this.isDocumentSavedToDisk = isSavedToDisk;
		}
		
		protected Location tempDir;
		protected GoEnvironment goEnv;
		protected Location describeFile;
		
		@Override
		protected SourceFileStructure createNewData() {
			
			if(fileLocation == null || isCancelled()) {
				return null;
			}
			
			goEnv = GoProjectEnvironment.getGoEnvironmentFromLocation(fileLocation);
			describeFile = fileLocation;
			tempDir = null;
			
			try {
				setupDescribeFile();
			} catch(IOException e) {
				LangCore.logError("Error creating temporary file for oracle describe: ", e);
				return null;
			}
			
			ExternalProcessResult describeResult;
			try {
				
				describeResult = runGoOracle(goEnv, describeFile);
				
			} catch(OperationCancellation e) {
				return null;
			} catch(CommonException | CoreException e) {
				LangCore.logError("Error running oracle describe for source structure update", e);
				return null;
			} finally {
				if(tempDir != null) {
					try {
						FileUtil.deleteDir(tempDir);
					} catch(IOException e) {
						LangCore.logError("Could not delete temp files", e);
					}
				}
			}
			
			if(fileLocation == null || isCancelled()) {
				return null;
			}
			
			try {
				
				return new GoOraclePackageDescribeParser(fileLocation, source) {
					@Override
					protected boolean isSourceElementLocation(Location sourceFileLoc) throws CommonException {
						return describeFile.equals(sourceFileLoc);
					};
				}.parse(describeResult);
			} catch(CommonException e) {
				LangCore.logWarning("Error parsing oracle describe result, for source structure update. ", e);
				return null;
			}
		}
		
		protected void setupDescribeFile() throws IOException, FileNotFoundException {
			if(isDocumentSavedToDisk) {
				// No need for temp file, use file on disk.
				return;
			}
			
			tempDir = Location.create_fromValid(Files.createTempDirectory("_goclipse"));
			Location tempDir_src = tempDir.resolve_fromValid("src/describe_temp");
			Files.createDirectories(tempDir_src.toPath());
			describeFile = tempDir_src.resolve_fromValid("describe.go");
			FileUtil.writeStringToFile(describeFile.toFile(), source, StringUtil.UTF8);
			
			// Modify goEnv for tempDir
			goEnv = new GoEnvironment(goEnv.getGoRoot(), new GoPath(tempDir.toString()));
		}
		
		protected ExternalProcessResult runGoOracle(GoEnvironment goEnv, Location opTempFile)
				throws CommonException, CoreException, OperationCancellation {
			GoOracleDescribeOperation oracleOp = new GoOracleDescribeOperation(
				GoToolPreferences.GO_ORACLE_Path.getDerivedValue().toString());
			
			int offset = GoSourceFileUtil.findPackageDeclaration_NameStart(source);
			
			ProcessBuilder pb = oracleOp.createProcessBuilder(goEnv, opTempFile, offset);
			
			return LangCore.getToolManager().runEngineTool(pb, null, cm);
		}
		
	}
	
	public static class StructureUpdateNullTask extends StructureUpdateTask {
		public StructureUpdateNullTask(StructureInfo structureInfo) {
			super(structureInfo);
		}
		
		@Override
		protected SourceFileStructure createNewData() {
			return null;
		}
	}
	
}