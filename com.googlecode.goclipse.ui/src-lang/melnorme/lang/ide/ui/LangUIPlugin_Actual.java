package melnorme.lang.ide.ui;

import com.googlecode.goclipse.ui.GoUIPlugin;

/**
 * Alias for the actual running plugin, used by Lang code. 
 */
public final class LangUIPlugin_Actual extends GoUIPlugin {
	
	protected static GoUIPlugin __getInstance() {
		return GoUIPlugin.getInstance();
	}
	
}