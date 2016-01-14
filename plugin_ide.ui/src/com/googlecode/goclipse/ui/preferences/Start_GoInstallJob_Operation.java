package com.googlecode.goclipse.ui.preferences;

import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.core.operations.GetAndInstallGoPackageOperation;
import com.googlecode.goclipse.tooling.env.GoEnvironment;

import melnorme.lang.ide.ui.operations.StartToolDownload_FromField;
import melnorme.lang.ide.ui.preferences.pages.DownloadToolTextField;
import melnorme.utilbox.core.CommonException;

public class Start_GoInstallJob_Operation extends StartToolDownload_FromField {
	
	public Start_GoInstallJob_Operation(String operationName, String downloadBundleJobName, 
			DownloadToolTextField toolTextField, String dlSource, String exeName) {
		super(operationName, downloadBundleJobName, toolTextField, dlSource, exeName);
	}
	
	protected GoEnvironment getGoEnvironment() {
		return GoProjectEnvironment.getGoEnvironment(null);
	}
	
	@Override
	protected ProcessBuilder getProcessToStart_andSetToolPath() throws CommonException {
		GoEnvironment goEnv = getGoEnvironment();
		GetAndInstallGoPackageOperation goInstallOp = new GetAndInstallGoPackageOperation(goEnv, dlSource, exeName);
		ProcessBuilder pb = goInstallOp.getProcessToStart();
		
		toolBinPath = goInstallOp.getDownloadedToolLocation().toPathString();
		return pb;
	}
	
}