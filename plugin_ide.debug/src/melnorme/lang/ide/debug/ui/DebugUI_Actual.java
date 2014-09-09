package melnorme.lang.ide.debug.ui;

import org.eclipse.cdt.debug.internal.ui.actions.breakpoints.ToggleDynamicPrintfAdapter;

import melnorme.lang.ide.ui.LangUIPlugin;
import LANG_PROJECT_ID.ide.debug.ui.ToggleBreakpointAdapter;

public class DebugUI_Actual {
	
	public static final String LANG_BREAKPOINT_FACTORY_ID = LangUIPlugin.PLUGIN_ID + "BreakpointFactory";
	
	public static ToggleBreakpointAdapter createToggleBreakPointAdapter() {
		return new ToggleBreakpointAdapter();
	}
	
	public static ToggleDynamicPrintfAdapter createDynamicPrintfBreakpoint() {
		return new ToggleDynamicPrintfAdapter();
	}
	
}