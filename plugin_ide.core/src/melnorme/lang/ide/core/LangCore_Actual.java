package melnorme.lang.ide.core;

import LANG_PROJECT_ID.ide.core.bundle_model.LANGUAGE_BundleModelManager;
import LANG_PROJECT_ID.ide.core.engine.LANGUAGE_SourceModelManager;
import LANG_PROJECT_ID.ide.core.operations.LANGUAGE_BuildManager;
import LANG_PROJECT_ID.ide.core.operations.LANGUAGE_ToolManager;
import melnorme.lang.ide.core.engine.SourceModelManager;
import melnorme.lang.ide.core.operations.ToolManager;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.project_model.BundleModelManager;
import melnorme.lang.ide.core.project_model.LangBundleModel;
import melnorme.lang.tooling.data.LANGUAGE_SDKLocationValidator;
import melnorme.lang.tooling.ops.SDKLocationValidator;

public class LangCore_Actual {
	
	public static final String PLUGIN_ID = "LANG_PROJECT_ID.ide.core";
	public static final String NATURE_ID = PLUGIN_ID +".nature";
	
	public static final String BUILDER_ID = PLUGIN_ID + ".Builder";
	public static final String BUILD_PROBLEM_ID = PLUGIN_ID + ".build_problem";
	public static final String SOURCE_PROBLEM_ID = PLUGIN_ID + ".source_problem";
	
	// Note: the variable should not be named with a prefix of LANGUAGE, 
	// or it will interfere with MelnormeEclipse templating
	public static final String NAME_OF_LANGUAGE = "LANG_NAME";
	
	public static final String VAR_NAME_SdkToolPath = "SDK_TOOL_PATH";
	public static final String VAR_NAME_SdkToolPath_DESCRIPTION = "The path of the SDK tool";
	
	public static LangCore instance;
	
	/* ----------------- Owned singletons: ----------------- */
	
	protected final CorePreferences corePrefs;
	protected final ToolManager toolManager;
	protected final LANGUAGE_BundleModelManager bundleManager;
	protected final BuildManager buildManager;
	protected final LANGUAGE_SourceModelManager sourceModelManager;
	
	public LangCore_Actual() {
		instance = (LangCore) this;
		
		corePrefs = createCorePreferences();
		toolManager = createToolManagerSingleton();
		bundleManager = createBundleModelManager();
		buildManager = createBuildManager(bundleManager.getModel());
		sourceModelManager = createSourceModelManager();
	}
	
	protected CorePreferences createCorePreferences() {
		return new CorePreferences() {
			@Override
			protected SDKLocationValidator getSDKLocationValidator() {
				return new LANGUAGE_SDKLocationValidator();
			}
		};
	}
	
	public static LANGUAGE_ToolManager createToolManagerSingleton() {
		return new LANGUAGE_ToolManager();
	}
	
	public static LANGUAGE_BundleModelManager createBundleModelManager() {
		return new LANGUAGE_BundleModelManager();
	}
	public static BuildManager createBuildManager(LangBundleModel bundleModel) {
		return new LANGUAGE_BuildManager(bundleModel, LangCore.getToolManager());
	}
	
	public static LANGUAGE_SourceModelManager createSourceModelManager() {
		return new LANGUAGE_SourceModelManager();
	}
	
	
	/* -----------------  ----------------- */
	
	public static CorePreferences preferences() {
		return instance.corePrefs;
	}
	
	public static ToolManager getToolManager() {
		return instance.toolManager;
	}
	public static LANGUAGE_BundleModel getBundleModel() {
		return instance.bundleManager.getModel();
	}
	public static BuildManager getBuildManager() {
		return instance.buildManager;
	}
	public static BundleModelManager<?> getBundleModelManager() {
		return instance.bundleManager;
	}
	public static SourceModelManager getSourceModelManager() {
		return instance.sourceModelManager;
	}
	
	public static class LANGUAGE_BundleModel extends LangBundleModel {
		
	}
	
}