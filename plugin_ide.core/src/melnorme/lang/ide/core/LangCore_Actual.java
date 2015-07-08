package melnorme.lang.ide.core;

import java.nio.file.Path;

import org.eclipse.core.resources.IProject;

import com.googlecode.goclipse.core.engine.GoEngineClient;
import com.googlecode.goclipse.core.operations.GoBuildManager;
import com.googlecode.goclipse.core.operations.GoToolManager;

import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.project_model.AbstractBundleInfo;
import melnorme.lang.ide.core.project_model.BundleManifestResourceListener;
import melnorme.lang.ide.core.project_model.BundleModelManager;
import melnorme.lang.ide.core.project_model.LangBundleModel;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.misc.SimpleLogger;

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
	
	public static GoEngineClient createEngineClient() {
		return new GoEngineClient();
	}
	
	public static BundleModelManager createBundleModelManager() {
		return new GoBundleModelManager();
	}
	public static GoBundleModel getBundleModel() {
		return (GoBundleModel) LangCore.getBundleModel();
	}
	public static BuildManager createBuildManager() {
		return new GoBuildManager(getBundleModel());
	}
	
	
	public static final class GoBundleModelManager extends BundleModelManager {
		
		public GoBundleModelManager() {
			super(new GoBundleModel());
		}
		
		@Override
		protected BundleManifestResourceListener init_createResourceListener() {
			return new ManagerResourceListener(null);
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public LangBundleModel<AbstractBundleInfo> getModel() {
			return (LangBundleModel<AbstractBundleInfo>) super.getModel();
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
	
	protected static final class GoBundleModel extends LangBundleModel<AbstractBundleInfo> {
		@Override
		protected SimpleLogger getLog() {
			return BundleModelManager.log;
		}
	}
	
}