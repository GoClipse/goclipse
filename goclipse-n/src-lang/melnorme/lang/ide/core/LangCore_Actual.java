package melnorme.lang.ide.core;

import org.eclipse.core.runtime.Plugin;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.builder.GoNature;

public class LangCore_Actual {
	
	public static final String PLUGIN_ID = Activator.PLUGIN_ID;
	public static final String NATURE_ID = GoNature.NATURE_ID;
	
	public static Plugin getInstance() {
		return Activator.getDefault();
	}
	
}