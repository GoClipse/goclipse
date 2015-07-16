/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation (JDT)
 *     DLTK team ? - DLTK modifications 
 *     Bruno Medeiros - Lang rewrite
 *******************************************************************************/
package melnorme.lang.ide.ui.preferences;

import melnorme.lang.ide.ui.preferences.common.IPreferencesDialogComponent;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.SWTUtil;
import melnorme.util.swt.components.AbstractComponent;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.PixelConverter;
import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;

//originally from DLTK version 5.0.0
public class EditorAppearanceColorsComponent extends AbstractComponent implements IPreferencesDialogComponent {
	
	public static class EditorColorItem {
		public final String label;
		public final String colorKey;
		public RGB color;
		
		public final String useSystemDefaultKey;
		public final int systemColor;
		public boolean useSystemDefault;
		
		public EditorColorItem(String name, String colorKey) {
			this(name, colorKey, null, 0);
		}
		
		public EditorColorItem(String label, String colorKey, String systemDefaultKey, int systemColor) {
			this.label = label;
			this.colorKey = colorKey;
			this.useSystemDefaultKey = systemDefaultKey;
			this.systemColor = systemColor;
		}
		
		public void setValues(String colorPrefValue, boolean useSystemDefaultPrefValue) {
			color = StringConverter.asRGB(colorPrefValue, PreferenceConverter.COLOR_DEFAULT_DEFAULT);
			useSystemDefault = false;
			if(useSystemDefaultKey != null) {
				useSystemDefault = useSystemDefaultPrefValue;
			}
		}
		
		public void loadFromStore(IPreferenceStore store) {
			String colorPrefValue = store.getString(colorKey);
			boolean useSystemDefaultPrefValue = useSystemDefaultKey == null ? false : 
				store.getBoolean(useSystemDefaultKey);
			setValues(colorPrefValue, useSystemDefaultPrefValue);
		}
		
		public void loadStoreDefaults(IPreferenceStore store) {
			String colorPrefValue = store.getDefaultString(colorKey);
			boolean useSystemDefaultPrefValue = useSystemDefaultKey == null ? false :
				store.getDefaultBoolean(useSystemDefaultKey);
			setValues(colorPrefValue, useSystemDefaultPrefValue);
		}
		
		public void saveToStore(IPreferenceStore store) {
			store.setValue(colorKey, StringConverter.asString(color));
			if(useSystemDefaultKey != null) {
				store.setValue(useSystemDefaultKey, useSystemDefault);
			}
		}
		
		public RGB getEffectiveColor(Display display) {
			if(useSystemDefault) {
				return display.getSystemColor(systemColor).getRGB(); 
			} else {
				return color;
			}
		}
		
	}
	
	protected final EditorColorItem[] editorColorItems;
	
	protected List colorList;
	protected ColorSelector colorEditor;
	protected Button useSystemDefaultButton;
	
	public EditorAppearanceColorsComponent(EditorColorItem[] editorColorItems) {
		this.editorColorItems = editorColorItems;
	}
	
	@Override
	protected void updateComponentFromInput() {
		// Does nothing: need a store as input
	}
	
	@Override
	public void loadFromStore(IPreferenceStore store) {
		for (EditorColorItem editorColorItem : editorColorItems) {
			editorColorItem.loadFromStore(store);
		}
		
		handleAppearanceColorListSelectionChanged();
	}
	
	@Override
	public void loadStoreDefaults(IPreferenceStore store) {
		for (EditorColorItem editorColorItem : editorColorItems) {
			editorColorItem.loadStoreDefaults(store);
		}
		handleAppearanceColorListSelectionChanged();
	}
	
	@Override
	public void saveToStore(IPreferenceStore store) {
		for (EditorColorItem editorColorItem : editorColorItems) {
			editorColorItem.saveToStore(store);
		}
	}
	
	@Override
	public int getPreferredLayoutColumns() {
		return 1;
	}
	
	@Override
	public void createContents(Composite topControl) {
		PixelConverter pc = new PixelConverter(topControl);
		
		GridData gd;
		gd = GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).span(2, 1).create();
		gd.heightHint = pc.convertHeightInCharsToPixels(1) / 2;
		SWTFactoryUtil.createLabel(topControl, SWT.LEFT, "", gd);
		
		SWTFactoryUtil.createLabel(topControl, SWT.LEFT, PreferencesMessages.EditorPreferencePage_title1, 
			GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).span(2, 1).create());
		
		Composite editorComposite = new Composite(topControl, SWT.NONE);
		editorComposite.setLayout(GridLayoutFactory.swtDefaults().numColumns(2).margins(0, 0).create());
		editorComposite.setLayoutData(GridDataFactory.fillDefaults().grab(false, true).span(2, 1).create());
		
		colorList = new List(editorComposite, SWT.SINGLE | SWT.V_SCROLL | SWT.BORDER);
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL);
		gd.heightHint = pc.convertHeightInCharsToPixels(12);
		colorList.setLayoutData(gd);
		for(int i = 0; i < editorColorItems.length; i++) {
			colorList.add(editorColorItems[i].label);
		}
		colorList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleAppearanceColorListSelectionChanged();
			}
		});
		
		createItemEditWidgets(editorComposite);
		
		colorList.select(0);
	}
	
	protected final EditorColorItem NULL_ELEMENT = new EditorColorItem("", "");
	
	protected EditorColorItem getSelectedItem() {
		int i = colorList.getSelectionIndex();
		if (i == -1)
			return NULL_ELEMENT;
		
		return editorColorItems[i];
	}
	
	protected void handleAppearanceColorListSelectionChanged() {
		if(!SWTUtil.isOkToUse(colorList)) {
			return;
		}
		
		EditorColorItem selectedItem = getSelectedItem();
		if(selectedItem != null && selectedItem != NULL_ELEMENT) {
			colorEditor.setColorValue(selectedItem.getEffectiveColor(colorList.getDisplay()));
			updateItemEditWidgets(selectedItem);
		}
	}
	
	protected void createItemEditWidgets(Composite editorComposite) {
		Composite itemEditorComposite = new Composite(editorComposite, SWT.NONE);
		itemEditorComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
		itemEditorComposite.setLayout(GridLayoutFactory.swtDefaults().numColumns(2).margins(0, 0).create());
		
		SWTFactoryUtil.createLabel(itemEditorComposite, SWT.LEFT, 
			PreferencesMessages.EditorPreferencePage_color, new GridData());
		
		colorEditor = new ColorSelector(itemEditorComposite);
		Button colorEditorButton = colorEditor.getButton();
		colorEditorButton.setLayoutData(GridDataFactory.swtDefaults().grab(true, false).create());
		colorEditorButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getSelectedItem().color = colorEditor.getColorValue();
			}
		});
		
		
		useSystemDefaultButton = new Button(itemEditorComposite, SWT.CHECK);
		useSystemDefaultButton.setText(PreferencesMessages.EditorPreferencePage_systemDefault);
		useSystemDefaultButton.setLayoutData(GridDataFactory.swtDefaults().grab(true, false).span(2, 1).create());
		useSystemDefaultButton.setVisible(false);
		useSystemDefaultButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean systemDefault = useSystemDefaultButton.getSelection();
				getSelectedItem().useSystemDefault = systemDefault;
				colorEditor.getButton().setEnabled(!systemDefault);
			}
		});
	}
	
	protected void updateItemEditWidgets(EditorColorItem appearanceItem) {
		if(appearanceItem.useSystemDefaultKey == null) {
			useSystemDefaultButton.setVisible(false);
			colorEditor.getButton().setEnabled(true);
		} else {
			useSystemDefaultButton.setVisible(true);
			boolean useSystemDefault = appearanceItem.useSystemDefault;
			useSystemDefaultButton.setSelection(useSystemDefault);
			colorEditor.getButton().setEnabled(!useSystemDefault);
		}
	}
	
}