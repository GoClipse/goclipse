package melnorme.lang.ide.core;

import org.eclipse.core.runtime.Plugin;

import com.googlecode.goclipse.Activator;

public class LangCore_Actual {
	
	public static final String PLUGIN_ID = Activator.PLUGIN_ID;
	public static final String NATURE_ID = "com.googlecode.goclipse.core.goNature";
	
	public static Plugin getInstance() {
		return Activator.getDefault();
	}
	
}