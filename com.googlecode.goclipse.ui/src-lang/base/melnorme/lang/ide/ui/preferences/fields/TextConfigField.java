/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.preferences.fields;

import melnorme.util.swt.SWTFactoryUtil;

import org.eclipse.jface.layout.PixelConverter;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class TextConfigField extends AbstractConfigField<String> {
	
	protected final int textLimit;
	protected Label labelControl;
	protected Text textControl;
	
	public TextConfigField(String label, String prefKey, int textLimit) {
		super(prefKey, label);
		this.textLimit = textLimit;
	}
	
	@Override
	protected void createContents(Composite topControl) {
		PixelConverter pixelConverter = new PixelConverter(topControl);
		
		labelControl = SWTFactoryUtil.createLabel(topControl, SWT.NONE, label, new GridData()); 
		
		textControl = new Text(topControl, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData();
		gd.widthHint = pixelConverter.convertWidthInCharsToPixels(textLimit + 1);
		textControl.setLayoutData(gd);
		textControl.setTextLimit(textLimit);
		textControl.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				updateFieldValue(textControl.getText());
			}
		});
	}
	
	@Override
	public Control getFieldControl() {
		return textControl;
	}
	
	@Override
	public Control getLeftMostControl() {
		return labelControl;
	}
	
	@Override
	public void doUpdateControls() {
		if(getFieldValue() != null) {
			textControl.setText(getFieldValue());
		}
	}
	
	@Override
	public void loadFromStore(IPreferenceStore store) {
		setFieldValue(store.getString(prefKey));
	}
	
	@Override
	public void saveToStore(IPreferenceStore store) {
		store.setValue(prefKey, getFieldValue());
	}
	
}