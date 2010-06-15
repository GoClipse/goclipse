package com.googlecode.goclipse.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.SysUtils;


/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		SysUtils.debug("Loading Go Plug-in Preferences...");
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.P_BOOLEAN, true);
		store.setDefault(PreferenceConstants.FIELD_USE_HIGHLIGHTING, PreferenceConstants.VALUE_HIGHLIGHTING_TRUE);
		store.setDefault(PreferenceConstants.P_STRING,	"Default value");	
	}

}
