package com.googlecode.goclipse.editors;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.editors.text.TextEditor;

import com.googlecode.goclipse.Activator;

public class GoEditor extends TextEditor {

	private ColorManager colorManager;
	private IPropertyChangeListener changeListener;
	
	
	public GoEditor() {
		super();
		setSourceViewerConfiguration(new Configuration());
		setDocumentProvider(new DocumentProvider());
		final GoEditor instance = this;
		
		
		changeListener = new IPropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent event) { 
//				SysUtils.debug("Preference Change Event");
//				if (event.getProperty().equals(PreferenceConstants.FIELD_USE_HIGHLIGHTING)) {
//					setSourceViewerConfiguration(new Configuration(colorManager));
//					setDocumentProvider(new DocumentProvider());
//					initializeEditor();
//				}
			}
		};
		
		Activator.getDefault().getPreferenceStore().addPropertyChangeListener(changeListener);
	}

	
	public void dispose() {
		if (colorManager != null) {
			colorManager.dispose();
		}
		super.dispose();
	}
}
