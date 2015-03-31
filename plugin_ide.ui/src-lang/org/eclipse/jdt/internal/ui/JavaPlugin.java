package org.eclipse.jdt.internal.ui;

import melnorme.lang.ide.ui.LangUIPlugin;


public abstract class JavaPlugin extends LangUIPlugin {
	
	public static LangUIPlugin getDefault() {
		return LangUIPlugin.getInstance();
	}
	
}
