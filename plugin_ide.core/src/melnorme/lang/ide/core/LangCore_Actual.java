package melnorme.lang.ide.core;

import com.googlecode.goclipse.core.engine.GoBundleModelManager;
import com.googlecode.goclipse.core.engine.GoSourceModelManager;
import com.googlecode.goclipse.core.engine.GoBundleModelManager.GoBundleModel;
import com.googlecode.goclipse.core.operations.GoBuildManager;
import com.googlecode.goclipse.core.operations.GoToolManager;

import melnorme.lang.ide.core.operations.build.BuildManager;

public class LangCore_Actual {
	
	public static final String PLUGIN_ID = "com.googlecode.goclipse.core";
	public static final String NATURE_ID = PLUGIN_ID + ".goNature";
	
//	public static final String BUILDER_ID = PLUGIN_ID + ".Builder";
	public static final String BUILDER_ID = "com.googlecode.goclipse.goBuilder";
	public static final String BUILD_PROBLEM_ID = PLUGIN_ID + ".goProblem";
	public static final String SOURCE_PROBLEM_ID = PLUGIN_ID + ".source_problem";
	
	public static final String LANGUAGE_NAME = "Go";
	
	public static GoToolManager createToolManagerSingleton() {
		return new GoToolManager();
	}
	
	public static GoSourceModelManager createSourceModelManager() {
		return new GoSourceModelManager();
	}
	
	public static GoBundleModelManager createBundleModelManager() {
		return new GoBundleModelManager();
	}
	public static GoBundleModel getBundleModel() {
		return (GoBundleModel) LangCore.getBundleModel();
	}
	public static BuildManager createBuildManager() {
		return new GoBuildManager(getBundleModel());
	}
	
}