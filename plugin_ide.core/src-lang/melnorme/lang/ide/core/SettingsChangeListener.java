package melnorme.lang.ide.core;

import melnorme.lang.ide.core.utils.prefs.IProjectPreference;
import melnorme.lang.tooling.LocationHandle;

public interface SettingsChangeListener {
	
	void preferenceChanged(IProjectPreference<?> setting, LocationHandle location, Object newValue);
	
}