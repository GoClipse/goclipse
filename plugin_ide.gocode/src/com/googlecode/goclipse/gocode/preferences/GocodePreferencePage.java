package com.googlecode.goclipse.gocode.preferences;

import melnorme.util.swt.SWTFactoryUtil;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.googlecode.goclipse.gocode.GocodePlugin;

/**
 * @author steel
 */
public class GocodePreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {
	
	public GocodePreferencePage() {
		super(GRID);
		
		setPreferenceStore(GocodePlugin.getPlugin().getPreferenceStore());
	}
	
	@Override
	public void init(IWorkbench workbench) {
		
	}
	
	@Override
	protected void createFieldEditors() {
		Group group = SWTFactoryUtil.createGroup(getFieldEditorParent(),
			"Gocode",
			GridDataFactory.fillDefaults().grab(true, false).minSize(300, SWT.DEFAULT).create());
		
		addField(new BooleanFieldEditor(
			GocodePreferences.GOCODE_CONSOLE.key, "Enable Gocode diagnostics console", group));
		
		addField(new BooleanFieldEditor(
			GocodePreferences.AUTO_START_SERVER.key, "Start Gocode server automatically", group));
		
		SWTFactoryUtil.createLabel(group, SWT.NONE,
			"(to start manually: gocode -s" + (GocodePreferences.USE_TCP ? " -sock=tcp" : "") + ")",
			GridDataFactory.swtDefaults().span(3, 1).indent(18, 0).create());
				
		addField(new FileFieldEditor(
			GocodePreferences.GOCODE_PATH.key, "Gocode path:", group));
		
		GridLayoutFactory.fillDefaults().margins(6, 4).numColumns(3).applyTo(group);
	}
	
}