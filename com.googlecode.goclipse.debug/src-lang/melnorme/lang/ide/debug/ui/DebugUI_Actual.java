package melnorme.lang.ide.debug.ui;

import org.eclipse.core.runtime.Plugin;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.debug.ui.GoToggleBreakpointAdapter;

public class DebugUI_Actual extends Plugin {
	
	public static String LANG_BREAKPOINT_FACTORY_ID = Activator.PLUGIN_ID + "BreakpointFactory";
	
	public static GoToggleBreakpointAdapter createToggleBreakPointAdapter() {
		return new GoToggleBreakpointAdapter();
	}
	
}