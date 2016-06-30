package melnorme.lang.ide.core;

import com.googlecode.goclipse.core.engine.GoBundleModelManager;
import com.googlecode.goclipse.core.engine.GoSourceModelManager;
import com.googlecode.goclipse.core.operations.GoBuildManager;
import com.googlecode.goclipse.core.operations.GoToolManager;
import com.googlecode.goclipse.tooling.GoSDKLocationValidator;

import melnorme.lang.ide.core.engine.SourceModelManager;
import melnorme.lang.ide.core.operations.ToolManager;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.project_model.BundleModelManager;
import melnorme.lang.ide.core.project_model.LangBundleModel;

public class LangCore_Actual {
	
	public static final String PLUGIN_ID = "com.googlecode.goclipse.core";
	public static final String NATURE_ID = PLUGIN_ID + ".goNature";
	
//	public static final String BUILDER_ID = PLUGIN_ID + ".Builder";
	public static final String BUILDER_ID = "com.googlecode.goclipse.goBuilder";
	public static final String BUILD_PROBLEM_ID = PLUGIN_ID + ".goProblem";
	public static final String SOURCE_PROBLEM_ID = PLUGIN_ID + ".source_problem";
	
	// Note: the variable should not be named with a prefix of LANGUAGE, 
	// or it will interfere with MelnormeEclipse templating
	public static final String NAME_OF_LANGUAGE = "Go";
	
	public static final String VAR_NAME_SdkToolPath = "GO_TOOL_PATH";
	public static final String VAR_NAME_SdkToolPath_DESCRIPTION = "The path of the Go tool";
	
	public static LangCore instance;
	
	/* ----------------- Owned singletons: ----------------- */
	
	protected final CoreSettings coreSettings;
	protected final ToolManager toolManager;
	protected final BundleModelManager<? extends LangBundleModel> bundleManager;
	protected final BuildManager buildManager;
	protected final SourceModelManager sourceModelManager;
	
	public LangCore_Actual() {
		instance = (LangCore) this;
		
		coreSettings = createCoreSettings();
		toolManager = createToolManager();
		bundleManager = createBundleModelManager();
		buildManager = createBuildManager(bundleManager.getModel());
		sourceModelManager = createSourceModelManager();
	}
	
	protected CoreSettings createCoreSettings() {
		return new CoreSettings() {
			@Override
			public GoSDKLocationValidator getSDKLocationValidator() {
				return new GoSDKLocationValidator();
			}
		};
	}
	
	public static GoToolManager createToolManager() {
		return new GoToolManager();
	}
	
	public static GoSourceModelManager createSourceModelManager() {
		return new GoSourceModelManager();
	}
	
	public static GoBundleModelManager createBundleModelManager() {
		return new GoBundleModelManager();
	}
	public static GoBuildManager createBuildManager(LangBundleModel bundleModel) {
		return new GoBuildManager(bundleModel, getToolManager());
	}
	
		
	/* -----------------  ----------------- */
	
	public static CoreSettings settings() {
		return instance.coreSettings;
	}
	
	public static ToolManager getToolManager() {
		return instance.toolManager;
	}
	public static LangBundleModel getBundleModel() {
		return instance.bundleManager.getModel();
	}
	public static BuildManager getBuildManager() {
		return instance.buildManager;
	}
	public static BundleModelManager<? extends LangBundleModel> getBundleModelManager() {
		return instance.bundleManager;
	}
	public static SourceModelManager getSourceModelManager() {
		return instance.sourceModelManager;
	}
	
}