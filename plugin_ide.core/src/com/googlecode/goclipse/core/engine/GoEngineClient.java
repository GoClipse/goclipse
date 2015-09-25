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

import java.io.IOException;
import java.nio.file.Files;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.engine.EngineClient;
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
import com.googlecode.goclipse.tooling.oracle.OraclePackageDescribeParser;

public class GoEngineClient extends EngineClient {
	
	public GoEngineClient() {
	}
	
	@Override
	protected StructureUpdateTask createUpdateTask2(StructureInfo structureInfo, String source, Location fileLocation) {
		if(fileLocation == null) {
			return new StructureUpdateNullTask(structureInfo);
		}
		
		return new GoStructureUpdateTask(structureInfo, source, fileLocation);
	}
	
	protected class GoStructureUpdateTask extends StructureUpdateTask {
		
		protected final String source;
		protected final Location fileLocation;
		
		public GoStructureUpdateTask(StructureInfo structureInfo, String source, Location fileLocation) {
			super(structureInfo);
			this.source = source;
			this.fileLocation = fileLocation;
		}
		
		@Override
		protected SourceFileStructure createSourceFileStructure() {
			
			if(fileLocation == null || isCancelled()) {
				return null;
			}
			
			GoEnvironment goEnv = GoProjectEnvironment.getGoEnvironmentFromLocation(fileLocation);
			Location tempDir = null;
			Location describeTempFile;
			
			ExternalProcessResult describeResult;
			try {
				tempDir = Location.create_fromValid(Files.createTempDirectory("_goclipse"));
				Location tempDir_src = tempDir.resolve_fromValid("src/describe_temp");
				Files.createDirectories(tempDir_src.toPath());
				
				describeTempFile = tempDir_src.resolve_fromValid("describe.go");
				try {
					
					FileUtil.writeStringToFile(describeTempFile.toFile(), source, StringUtil.UTF8);
					
					goEnv = new GoEnvironment(goEnv.getGoRoot(), goEnv.getGoArch(), goEnv.getGoOs(), 
						new GoPath(tempDir.toString()));
					
					describeResult = runGoOracle(goEnv, describeTempFile);
					
				} finally {
					if(describeTempFile != null) {
						try {
							Files.deleteIfExists(describeTempFile.toPath());
							FileUtil.deleteDir(tempDir);
						} catch(IOException e) {
							LangCore.logError("Could not delete temp files", e);
						}
					}
				}
			} catch(IOException e) {
				LangCore.logError("Error creating temporary file for oracle describe: ", e);
				return null;
			} catch(OperationCancellation e) {
				return null;
			} catch(CommonException | CoreException e) {
				LangCore.logError("Error running oracle describe for source structure update", e);
				return null;
			}
			
			try {
				if(describeResult.exitValue != 0) {
					return null; // Don't log this error 
				}
				
				return new OraclePackageDescribeParser(fileLocation) {
					@Override
					protected boolean isSourceElementLocation(Location sourceFileLoc) throws CommonException {
						return describeTempFile.equals(sourceFileLoc);
					};
				}.parse(describeResult, source);
			} catch(CommonException e) {
				LangCore.logWarning("Error parsing oracle describe result, for source structure update. ", e);
				return null;
			}
		}
		
		protected SourceFileStructure createSourceFileStructure(GoEnvironment goEnv, Location opTempFile)
				throws CommonException, CoreException, OperationCancellation {

			ExternalProcessResult describeResult = runGoOracle(goEnv, opTempFile);
			return new OraclePackageDescribeParser(fileLocation).parse(describeResult, source);
		}
		
		protected ExternalProcessResult runGoOracle(GoEnvironment goEnv, Location opTempFile)
				throws CommonException, CoreException, OperationCancellation {
			GoOracleDescribeOperation oracleOp = new GoOracleDescribeOperation(GoToolPreferences.GO_ORACLE_Path.get());
			
			int offset = GoSourceFileUtil.findPackageDeclaration_NameStart(source);
			
			ProcessBuilder pb = oracleOp.createProcessBuilder(goEnv, opTempFile, offset);
			
			ExternalProcessResult describeResult = LangCore.getToolManager().runEngineTool(pb, null, cm);
			return describeResult;
		}
		
	}
	
	@Override
	protected StructureUpdateTask createDisposeTask2(StructureInfo structureInfo, Location fileLocation) {
		return new StructureUpdateNullTask(structureInfo);
	}
	
	public static class StructureUpdateNullTask extends StructureUpdateTask {
		public StructureUpdateNullTask(StructureInfo structureInfo) {
			super(structureInfo);
		}
		
		@Override
		protected SourceFileStructure createSourceFileStructure() {
			return null;
		}
	}
	
}