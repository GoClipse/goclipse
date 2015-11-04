/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.util.swt.components.fields;

import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import melnorme.util.swt.SWTLayoutUtil;
import melnorme.util.swt.SWTUtil;
import melnorme.util.swt.components.LabelledFieldComponent;

public class ColorField extends LabelledFieldComponent<RGB> {
	
	public static class ColorSelectorExt extends ColorSelector {
		
		public ColorSelectorExt(Composite parent) {
			super(parent);
		}
		
		@Override
		public void setColorValue(RGB newColor) {
			RGB oldValue = getColorValue();
			super.setColorValue(newColor);
			
		    final Object[] finalListeners = getListeners();
		    if (finalListeners.length > 0) {
		        PropertyChangeEvent pEvent = new PropertyChangeEvent(this, PROP_COLORCHANGE, oldValue, newColor);
		        
		        for (Object object : finalListeners) {
		            IPropertyChangeListener listener = (IPropertyChangeListener) object;
		            listener.propertyChange(pEvent);
				}
		    }

		}
	}
	
	protected ColorSelector colorSelector;
	
	public ColorField(String labelText) {
		super(labelText, Option_AllowNull.NO, new RGB(0, 0, 0));
	}
	
	@Override
	public int getPreferredLayoutColumns() {
		return 2;
	}
	
	@Override
	protected void createContents_all(Composite topControl) {
		createContents_Label(topControl);
		createContents_ColorChooser(topControl);
	}
	
	@Override
	protected void createContents_layout() {
		SWTLayoutUtil.layout2Controls_expandLast(label, colorSelector.getButton());
	}
	
	protected void createContents_ColorChooser(Composite parent) {
		colorSelector = new ColorSelectorExt(parent);
		colorSelector.addListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				setFieldValueFromControl(colorSelector.getColorValue());
			}
		});
	}
	
	public ColorSelector getColorSelector() {
		return colorSelector;
	}
	
	@Override
	public Control getFieldControl() {
		return colorSelector == null ? null : colorSelector.getButton();
	}
	
	@Override
	protected void doUpdateComponentFromValue() {
		colorSelector.setColorValue(getFieldValue());
	}
	
	@Override
	protected void doSetEnabled(boolean enabled) {
		SWTUtil.setEnabledIfOk(label, enabled);
		if(colorSelector != null) {
			SWTUtil.setEnabledIfOk(colorSelector.getButton(), enabled);
		}
	}
	
}