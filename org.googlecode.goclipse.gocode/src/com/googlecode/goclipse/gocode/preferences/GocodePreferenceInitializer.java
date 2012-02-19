package com.googlecode.goclipse.gocode.preferences;

import com.googlecode.goclipse.gocode.GocodePlugin;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

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
