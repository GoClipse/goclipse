package melnorme.lang.ide.core;

import melnorme.lang.ide.core.operations.build.BuildManager.BuildConfiguration;
import melnorme.lang.ide.core.project_model.AbstractBundleInfo;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;

public class BundleInfo extends AbstractBundleInfo {
	
	@Override
	public Indexable<BuildConfiguration> getBuildConfigurations() {
		return ArrayList2.create(
			new BuildConfiguration("./...", null)
		);
	}
	
}