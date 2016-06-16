package melnorme.lang.ide.debug.ui;

import org.eclipse.cdt.debug.internal.ui.actions.breakpoints.ToggleDynamicPrintfAdapter;

import com.googlecode.goclipse.debug.ui.GoToggleBreakpointAdapter;

import melnorme.lang.ide.core.LangCore;

public class DebugUI_Actual {
	
	public static final String LANG_BREAKPOINT_FACTORY_ID = LangCore.PLUGIN_ID + "BreakpointFactory";
	
	public static GoToggleBreakpointAdapter createToggleBreakPointAdapter() {
		return new GoToggleBreakpointAdapter();
	}
	
	public static ToggleDynamicPrintfAdapter createDynamicPrintfBreakpoint() {
		return new ToggleDynamicPrintfAdapter();
	}
	
}