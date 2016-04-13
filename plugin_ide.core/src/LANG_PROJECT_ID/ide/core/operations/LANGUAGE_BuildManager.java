package LANG_PROJECT_ID.ide.core.operations;

import org.eclipse.core.runtime.IProgressMonitor;

import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.IOperationConsoleHandler;
import melnorme.lang.ide.core.operations.ToolManager;
import melnorme.lang.ide.core.operations.ToolMarkersHelper;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.lang.ide.core.operations.build.CommonBuildTargetOperation;
import melnorme.lang.ide.core.project_model.LangBundleModel;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.tooling.bundle.LaunchArtifact;
import melnorme.lang.tooling.ops.ToolSourceMessage;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public final class LANGUAGE_BuildManager extends BuildManager {
	
	public LANGUAGE_BuildManager(LangBundleModel bundleModel, ToolManager toolManager) {
		super(bundleModel, toolManager);
	}
	
	@Override
	protected Indexable<BuildType> getBuildTypes_do() {
		return ArrayList2.create(
			new LANGUAGE_BuildType("<default>")
		);
	}
	
	protected class LANGUAGE_BuildType extends BuildType {
		public LANGUAGE_BuildType(String name) {
			super(name);
		}
		
		@Override
		public String getDefaultCommandArguments(BuildTarget bt) throws CommonException {
			return ".";
		}
		
		@Override
		public LaunchArtifact getMainLaunchArtifact(BuildTarget bt) throws CommonException {
			return new LaunchArtifact(bt.getBuildConfigName(), "default_artifact.exe"); // TODO: LANG
		}
		
		@Override
		public CommonBuildTargetOperation getBuildOperation(BuildTarget bt,
				IOperationConsoleHandler opHandler, String buildArguments) {
			return new LANGUAGE_BuildTargetOperation(bt, opHandler, buildArguments);
		}
	}
	
	/* ----------------- Build ----------------- */
	
	protected class LANGUAGE_BuildTargetOperation extends CommonBuildTargetOperation {
		
		public LANGUAGE_BuildTargetOperation(BuildTarget buildTarget,
				IOperationConsoleHandler opHandler, String buildArguments) {
			super(LANGUAGE_BuildManager.this.toolManager, buildTarget, opHandler, buildArguments);
		}
		
		@Override
		protected void processBuildOutput(ExternalProcessResult processResult, IProgressMonitor pm) 
				throws CommonException {
			ArrayList2<ToolSourceMessage> buildErrors = new ArrayList2<>(); 
			
			// TODO: Lang process build result
			
			new ToolMarkersHelper().addErrorMarkers(buildErrors, ResourceUtils.getProjectLocation2(project), pm);
		}
	}
	
}