package melnorme.lang.ide.core;

import java.nio.file.Path;

import org.eclipse.core.resources.IProject;

import LANG_PROJECT_ID.ide.core.engine.LANGUAGE_EngineClient;
import LANG_PROJECT_ID.ide.core.operations.LANGUAGE_BuildManager;
import LANG_PROJECT_ID.ide.core.operations.LANGUAGE_ToolManager;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.project_model.AbstractBundleInfo;
import melnorme.lang.ide.core.project_model.BundleManifestResourceListener;
import melnorme.lang.ide.core.project_model.BundleModelManager;
import melnorme.lang.ide.core.project_model.LangBundleModel;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.misc.SimpleLogger;

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
	
	public static BundleModelManager createBundleModelManager() {
		return new LANGUAGE_BundleModelManager();
	}
	public static LANGUAGE_BundleModel getBundleModel() {
		return (LANGUAGE_BundleModel) LangCore.getBundleModel();
	}
	public static BuildManager createBuildManager() {
		return new LANGUAGE_BuildManager(getBundleModel());
	}
	
	
	public static final class LANGUAGE_BundleModelManager extends BundleModelManager {
		
		public LANGUAGE_BundleModelManager() {
			super(new LANGUAGE_BundleModel());
		}
		
		@Override
		protected BundleManifestResourceListener init_createResourceListener() {
			return new ManagerResourceListener(new org.eclipse.core.runtime.Path("lang.bundle"));
		}
		
		@Override
		public LANGUAGE_BundleModel getModel() {
			return (LANGUAGE_BundleModel) super.getModel();
		}
		
		@Override
		protected Object getProjectInfo(IProject project) {
			return model.getProjectInfo(project);
		}
		
		@Override
		protected void bundleProjectRemoved(IProject project) {
			model.removeProjectInfo(project);
		}
		
		@Override
		protected void bundleProjectAdded(IProject project) {
			getModel().setProjectInfo(project, new AbstractBundleInfo() {
				
				protected final ArrayList2<BuildConfiguration> DEFAULT_BUILD_CONFIGs = ArrayList2.create(
					new BuildConfiguration(null, null)
				);
				
				@Override
				public Path getEffectiveTargetFullPath() {
					
					return null;
				}
				
				@Override
				public Indexable<BuildConfiguration> getBuildConfigurations() {
					return DEFAULT_BUILD_CONFIGs;
				}
				
			});
		}
		
		@Override
		protected void bundleManifestFileChanged(IProject project) {
			bundleProjectAdded(project);
		}
	}
	
	protected static final class LANGUAGE_BundleModel extends LangBundleModel<AbstractBundleInfo> {
		@Override
		protected SimpleLogger getLog() {
			return BundleModelManager.log;
		}
	}
	
}