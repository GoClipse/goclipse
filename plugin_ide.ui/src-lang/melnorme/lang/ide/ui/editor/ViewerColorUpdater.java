/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.editor;

import static melnorme.utilbox.core.CoreUtil.listFrom;

import java.util.List;

import melnorme.util.swt.SWTUtil;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants;
import org.eclipse.ui.texteditor.AbstractTextEditor;

public class ViewerColorUpdater extends SourceViewerConfigurer {
	
	private Color fForegroundColor;
	private Color fBackgroundColor;
	private Color fSelectionForegroundColor;
	private Color fSelectionBackgroundColor;
	
	public ViewerColorUpdater(IPreferenceStore prefStore, ProjectionViewerExt sourceViewer) {
		super(prefStore, sourceViewer);
	}
	
	@Override
	protected void doConfigureViewer() {
		// ----------- foreground color --------------------
		fForegroundColor = updateColorFromSetting(fForegroundColor,
			AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND_SYSTEM_DEFAULT,
			AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND);
		styledText.setForeground(fForegroundColor);
		
		// ---------- background color ----------------------
		fBackgroundColor = updateColorFromSetting(fBackgroundColor,
			AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND_SYSTEM_DEFAULT,
			AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND);
		styledText.setBackground(fForegroundColor);
		
		// ----------- selection foreground color --------------------
		fSelectionForegroundColor = updateColorFromSetting(fSelectionForegroundColor,
			AbstractDecoratedTextEditorPreferenceConstants.EDITOR_SELECTION_FOREGROUND_DEFAULT_COLOR,
			AbstractDecoratedTextEditorPreferenceConstants.EDITOR_SELECTION_FOREGROUND_COLOR);
		styledText.setSelectionForeground(fSelectionForegroundColor);
		
		// ---------- selection background color ----------------------
		fSelectionBackgroundColor = updateColorFromSetting(fSelectionBackgroundColor,
			AbstractDecoratedTextEditorPreferenceConstants.EDITOR_SELECTION_BACKGROUND_DEFAULT_COLOR,
			AbstractDecoratedTextEditorPreferenceConstants.EDITOR_SELECTION_BACKGROUND_COLOR);
		styledText.setSelectionBackground(fSelectionBackgroundColor);
	}
	
	@Override
	protected void doUnconfigureViewer() {
		// Before we dispose colors, we must make sure styledText will no longer use them.
		styledText.setForeground(null);
		styledText.setBackground(null);
		styledText.setSelectionForeground(null);
		styledText.setSelectionBackground(null);
		
		fForegroundColor = SWTUtil.dispose(fForegroundColor);
		fBackgroundColor = SWTUtil.dispose(fBackgroundColor);
		fSelectionForegroundColor = SWTUtil.dispose(fSelectionForegroundColor);
		fSelectionBackgroundColor = SWTUtil.dispose(fSelectionBackgroundColor);
	}
	
	protected static final List<String> PROP_KEY_LIST = listFrom(
		AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND,
		AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND_SYSTEM_DEFAULT,
		AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND,
		AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND_SYSTEM_DEFAULT,
		AbstractDecoratedTextEditorPreferenceConstants.EDITOR_SELECTION_FOREGROUND_COLOR,
		AbstractDecoratedTextEditorPreferenceConstants.EDITOR_SELECTION_FOREGROUND_DEFAULT_COLOR,
		AbstractDecoratedTextEditorPreferenceConstants.EDITOR_SELECTION_BACKGROUND_COLOR,
		AbstractDecoratedTextEditorPreferenceConstants.EDITOR_SELECTION_BACKGROUND_DEFAULT_COLOR
	);
	
	@Override
	protected void handlePropertyChange(PropertyChangeEvent event) {
		String property = event.getProperty();
		
		if(PROP_KEY_LIST.contains(property)) {
			doConfigureViewer();
		}
	}
	
	protected Color updateColorFromSetting(Color currentOwnedColor, String prefKeyUseDefault, String prefKeyColorRgb) {
		if(currentOwnedColor != null)
			currentOwnedColor.dispose();
		
		Display display = styledText.getDisplay();
		return store.getBoolean(prefKeyUseDefault) ? null : createColor(store, prefKeyColorRgb, display);
	}
	
	protected Color createColor(IPreferenceStore store, String key, Display display) {
		
		RGB rgb = null;
		
		if(store.contains(key)) {
			
			if(store.isDefault(key)) {
				rgb = PreferenceConverter.getDefaultColor(store, key);
			} else {
				rgb = PreferenceConverter.getColor(store, key);
			}
			
			if(rgb != null)
				return new Color(display, rgb);
		}
		
		return null;
	}
	
}