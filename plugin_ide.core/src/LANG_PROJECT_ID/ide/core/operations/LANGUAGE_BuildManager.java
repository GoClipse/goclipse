package LANG_PROJECT_ID.ide.core.operations;

import java.nio.file.Path;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import melnorme.lang.ide.core.operations.OperationInfo;
import melnorme.lang.ide.core.operations.ToolMarkersUtil;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.operations.build.CommonBuildTargetOperation;
import melnorme.lang.ide.core.operations.build.ValidatedBuildTarget;
import melnorme.lang.ide.core.project_model.LangBundleModel;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.tooling.bundle.LaunchArtifact;
import melnorme.lang.tooling.ops.ToolSourceMessage;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public final class LANGUAGE_BuildManager extends BuildManager {
	
	public LANGUAGE_BuildManager(LangBundleModel bundleModel) {
		super(bundleModel);
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
		protected void getDefaultBuildOptions(ValidatedBuildTarget vbt, ArrayList2<String> buildArgs) {
			buildArgs.add(".");
		}
		
		@Override
		public LaunchArtifact getMainLaunchArtifact(ValidatedBuildTarget vbt) throws CommonException {
			return new LaunchArtifact(vbt.getBuildConfigName(), "default_artifact.exe"); // TODO: LANG
		}
		
		@Override
		public CommonBuildTargetOperation getBuildOperation(ValidatedBuildTarget validatedBuildTarget,
				OperationInfo opInfo, Path buildToolPath) throws CommonException, CoreException {
			return new LANGUAGE_BuildTargetOperation(validatedBuildTarget, opInfo, buildToolPath);
		}
	}
	
	/* ----------------- Build ----------------- */
	
	protected class LANGUAGE_BuildTargetOperation extends CommonBuildTargetOperation {
		
		public LANGUAGE_BuildTargetOperation(ValidatedBuildTarget validatedBuildTarget, OperationInfo parentOpInfo, 
				Path buildToolPath) throws CommonException, CoreException {
			super(validatedBuildTarget.buildMgr, validatedBuildTarget, parentOpInfo, buildToolPath);
		}
		
		@Override
		protected void addToolCommand(ArrayList2<String> commands)
				throws CoreException, CommonException, OperationCancellation {
			//super.addToolCommand(commands);
		}
		
		@Override
		protected ProcessBuilder getProcessBuilder(ArrayList2<String> commands)
				throws CommonException, OperationCancellation, CoreException {
			Location projectLocation = ResourceUtils.getProjectLocation(getProject());
			return getToolManager().createToolProcessBuilder(getBuildToolPath(), projectLocation, 
				commands.toArray(String.class));
		}
		
		@Override
		protected void processBuildOutput(ExternalProcessResult processResult, IProgressMonitor pm) 
				throws CoreException {
			ArrayList2<ToolSourceMessage> buildErrors = new ArrayList2<>(); 
			
			// TODO: Lang process build result
			
			new ToolMarkersUtil().addErrorMarkers(buildErrors, ResourceUtils.getProjectLocation(project), pm);
		}
	}
	
}