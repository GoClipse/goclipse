package melnorme.lang.ide.core;

import com.googlecode.goclipse.core.engine.GoBundleModelManager;
import com.googlecode.goclipse.core.engine.GoSourceModelManager;
import com.googlecode.goclipse.core.operations.GoBuildManager;
import com.googlecode.goclipse.core.operations.GoToolManager;
import com.googlecode.goclipse.tooling.GoSDKLocationValidator;

import melnorme.utilbox.misc.ILogHandler;

public class LangCore_Actual extends AbstractLangCore {
	
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
	
	public LangCore_Actual(ILogHandler logHandler) {
		super(logHandler);
	}
		
	@Override
	protected CoreSettings createCoreSettings() {
		return new CoreSettings() {
			@Override
			public GoSDKLocationValidator getSDKLocationValidator() {
				return new GoSDKLocationValidator();
			}
		};
	}
	
	@Override
	public GoToolManager createToolManager() {
		return new GoToolManager(coreSettings);
	}
	
	public static GoSourceModelManager createSourceModelManager() {
		return new GoSourceModelManager();
	}
	
	public static GoBundleModelManager createBundleModelManager() {
		return new GoBundleModelManager();
	}
	
	@Override
	public GoBuildManager createBuildManager() {
		return new GoBuildManager(bundleManager.getModel(), getToolManager());
	}
		
	/* -----------------  ----------------- */
	
}