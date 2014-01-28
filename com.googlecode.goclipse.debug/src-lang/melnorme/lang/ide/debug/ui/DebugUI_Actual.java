package melnorme.lang.ide.debug.ui;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.debug.ui.GoToggleBreakpointAdapter;

public class DebugUI_Actual {
	
	public static String LANG_BREAKPOINT_FACTORY_ID = Activator.PLUGIN_ID + "BreakpointFactory";
	
	public static GoToggleBreakpointAdapter createToggleBreakPointAdapter() {
		return new GoToggleBreakpointAdapter();
	}
	
}