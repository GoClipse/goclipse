package com.googlecode.goclipse.ui.preferences;

import melnorme.lang.ide.ui.tools.AbstractDeamonToolPrefPage;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * @author steel
 */
public class GocodePreferencePage extends AbstractDeamonToolPrefPage implements
		IWorkbenchPreferencePage {
	
	@Override
	public void init(IWorkbench workbench) {
	}
	
	@Override
	protected String getDaemonToolName() {
		return "Gocode";
	}
	
}