package com.googlecode.goclipse.gocode.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.googlecode.goclipse.gocode.GocodePlugin;

/**
 * Set the "auto-run gocode server" preference to true.
 */
public class GocodePreferenceInitializer extends AbstractPreferenceInitializer {

  public GocodePreferenceInitializer() {
    
  }

  @Override
  public void initializeDefaultPreferences() {
    IPreferenceStore store = GocodePlugin.getPlugin().getPreferenceStore();
    
    store.setDefault(GocodePlugin.RUN_SERVER_PREF, true);
  }

}
