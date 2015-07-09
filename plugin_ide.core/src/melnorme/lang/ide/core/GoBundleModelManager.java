package melnorme.lang.ide.core;

import java.nio.file.Path;

import org.eclipse.core.resources.IProject;

import melnorme.lang.ide.core.GoBundleModelManager.GoBundleModel;
import melnorme.lang.ide.core.project_model.AbstractBundleInfo;
import melnorme.lang.ide.core.project_model.BundleManifestResourceListener;
import melnorme.lang.ide.core.project_model.BundleModelManager;
import melnorme.lang.ide.core.project_model.LangBundleModel;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;

public class GoBundleModelManager extends BundleModelManager<GoBundleModel> {
	
	public static class GoBundleModel extends LangBundleModel<AbstractBundleInfo> {
		
	}
	
	public GoBundleModelManager() {
		super(new GoBundleModel());
	}
	
	@Override
	protected BundleManifestResourceListener init_createResourceListener() {
		return new ManagerResourceListener(null);
	}
	
	@Override
	protected void bundleProjectRemoved(IProject project) {
		model.removeProjectInfo(project);
	}
	
	@Override
	protected void bundleProjectAdded(IProject project) {
		getModel().setProjectInfo(project, new AbstractBundleInfo() {
			
			protected final ArrayList2<BuildConfiguration> DEFAULT_BUILD_CONFIGs = ArrayList2.create(
				new BuildConfiguration("./...", null)
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