package melnorme.lang.ide.core;

import LANG_PROJECT_ID.ide.core.bundle_model.LANGUAGE_BundleModelManager;
import LANG_PROJECT_ID.ide.core.engine.LANGUAGE_LanguageServerHandler;
import LANG_PROJECT_ID.ide.core.engine.LANGUAGE_SourceModelManager;
import LANG_PROJECT_ID.ide.core.operations.LANGUAGE_BuildManager;
import LANG_PROJECT_ID.ide.core.operations.LANGUAGE_ToolManager;
import LANG_PROJECT_ID.tooling.toolchain.LANGUAGE_SDKLocationValidator;
import melnorme.lang.ide.core.engine.LanguageServerHandler;
import melnorme.lang.ide.core.operations.ToolManager;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.project_model.LangBundleModel;
import melnorme.lang.ide.core.utils.operation.EclipseJobExecutor;
import melnorme.utilbox.misc.ILogHandler;

public class LangCore_Actual extends AbstractLangCore {
	
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
	
	public static final String LANGUAGE_SERVER_Name = "lang_language_server";
	
	public LangCore_Actual(ILogHandler logHandler) {
		super(logHandler);
	}
		
	@Override
	protected CoreSettings createCoreSettings() {
		return new CoreSettings() {
			@Override
			public LANGUAGE_SDKLocationValidator getSDKLocationValidator() {
				return new LANGUAGE_SDKLocationValidator();
			}
		};
	}
	
	@Override
	protected ToolManager createToolManager() {
		return new LANGUAGE_ToolManager(coreSettings);
	}
	
	@Override
	public LanguageServerHandler<?> createLanguageServerHandler() {
		return new LANGUAGE_LanguageServerHandler(new EclipseJobExecutor(), getToolManager());
	}
	
	public static LANGUAGE_BundleModelManager createBundleModelManager() {
		return new LANGUAGE_BundleModelManager();
	}
	
	@Override
	protected BuildManager createBuildManager() {
		return new LANGUAGE_BuildManager(this.bundleManager.getModel(), toolManager);
	}
	
	public static LANGUAGE_SourceModelManager createSourceModelManager() {
		return new LANGUAGE_SourceModelManager();
	}
	
	/* -----------------  ----------------- */
	
	public static class LANGUAGE_BundleModel extends LangBundleModel {
		
	}
	
}