package melnorme.lang.ide.core;

import org.eclipse.core.runtime.Plugin;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.core.GoCore;

public class LangCore_Actual {
	
	public static final String PLUGIN_ID = "com.googlecode.goclipse.core";
	public static final String NATURE_ID = Activator.PLUGIN_ID + ".goNature";
	
	public static final String BUILD_PROBLEM_ID = GoCore.PLUGIN_ID + ".goProblem";
	
	public static Plugin getInstance() {
		return Activator.getDefault();
	}
	
}