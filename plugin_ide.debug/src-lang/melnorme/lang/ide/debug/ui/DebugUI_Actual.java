package melnorme.lang.ide.debug.ui;

import melnorme.lang.ide.ui.LangUIPlugin;

import MMRNMHRM_ID.debug.ui.ToggleBreakpointAdapter;

public class DebugUI_Actual {
	
	public static final String LANG_BREAKPOINT_FACTORY_ID = LangUIPlugin.PLUGIN_ID + "BreakpointFactory";
	
	public static ToggleBreakpointAdapter createToggleBreakPointAdapter() {
		return new ToggleBreakpointAdapter();
	}
	
}