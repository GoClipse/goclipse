package melnorme.lang.ide.core;

import org.eclipse.core.runtime.Path;

import LANG_PROJECT_ID.ide.core.engine.LANGUAGE_EngineClient;
import LANG_PROJECT_ID.ide.core.operations.LANGUAGE_ToolManager;
import melnorme.lang.ide.core.project_model.BuildManager;
import melnorme.lang.ide.core.project_model.BundleManifestResourceListener;

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
	
	public static BuildManager createBuildManager() {
		return new BuildManager() {
			// TODO: LANG BuildManager
			@Override
			protected BundleManifestResourceListener init_createResourceListener() {
				return new ManagerResourceListener(new Path("lang.bundle"));
			}
		};
	}
	
}