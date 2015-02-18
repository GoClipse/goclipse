package LANG_PROJECT_ID.ide.ui;

import melnorme.lang.ide.ui.LangUIPlugin;

import org.osgi.framework.BundleContext;

public class LANGUAGE_UIPlugin extends LangUIPlugin {
	
	@Override
	protected LANGUAGE_OperationsConsoleListener createOperationsConsoleListener() {
		return new LANGUAGE_OperationsConsoleListener();
	}
	
	@Override
	protected void doCustomStop(BundleContext context) {
	}
	
}