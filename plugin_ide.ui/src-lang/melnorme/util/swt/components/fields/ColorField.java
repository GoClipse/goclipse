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
package melnorme.util.swt.components.fields;

import melnorme.util.swt.SWTFactory;
import melnorme.util.swt.SWTLayoutUtil;
import melnorme.util.swt.components.AbstractFieldExt2;

import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class ColorField extends AbstractFieldExt2<RGB> {
	
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
	
	protected Label label;
	protected ColorSelector colorSelector;
	
	public ColorField(String labelText) {
		super(labelText, new RGB(0, 0, 0));
	}
	
	@Override
	public int getPreferredLayoutColumns() {
		return 2;
	}
	
	@Override
	protected void createContents_do(Composite topControl) {
		createLabel(topControl);
		createColorChooser(topControl);
	}
	
	protected void createLabel(Composite parent) {
		label = SWTFactory.createLabel(parent, SWT.LEFT, labelText);
	}
	
	protected void createColorChooser(Composite parent) {
		colorSelector = new ColorSelectorExt(parent);
		colorSelector.addListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				setFieldValueFromControl(colorSelector.getColorValue());
			}
		});
	}
	
	@Override
	protected void createContents_layout() {
		SWTLayoutUtil.layout2Controls_expandLast(label, colorSelector.getButton());
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
	
	public void setEnabled(boolean enabled) {
		label.setEnabled(enabled);
		colorSelector.setEnabled(enabled);
	}
	
}