package melnorme.lang.ide.core;

import LANG_PROJECT_ID.ide.core.bundle_model.LANGUAGE_BundleModelManager;
import LANG_PROJECT_ID.ide.core.engine.LANGUAGE_EngineClient;
import LANG_PROJECT_ID.ide.core.operations.LANGUAGE_BuildManager;
import LANG_PROJECT_ID.ide.core.operations.LANGUAGE_ToolManager;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.project_model.LangBundleModel;

public class LangCore_Actual {
	
	public static final String PLUGIN_ID = "LANG_PROJECT_ID.ide.core";
	public static final String NATURE_ID = PLUGIN_ID +".nature";
	
	public static final String BUILDER_ID = PLUGIN_ID + ".Builder";
	public static final String BUILD_PROBLEM_ID = PLUGIN_ID + ".build_problem";
	public static final String SOURCE_PROBLEM_ID = PLUGIN_ID + ".source_problem";
	
	public static final String LANGUAGE_NAME = "Lang";
	
	public static LANGUAGE_ToolManager createToolManagerSingleton() {
		return new LANGUAGE_ToolManager();
	}
	
	public static LANGUAGE_EngineClient createEngineClient() {
		return new LANGUAGE_EngineClient();
	}
	
	public static LANGUAGE_BundleModelManager createBundleModelManager() {
		return new LANGUAGE_BundleModelManager();
	}
	public static LANGUAGE_BundleModel getBundleModel() {
		return (LANGUAGE_BundleModel) LangCore.getBundleModel();
	}
	public static BuildManager createBuildManager() {
		return new LANGUAGE_BuildManager(getBundleModel());
	}
	
	
	public static class LANGUAGE_BundleModel extends LangBundleModel {
		
	}
	
}