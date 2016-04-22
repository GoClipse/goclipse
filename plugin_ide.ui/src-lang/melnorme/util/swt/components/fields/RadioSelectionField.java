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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.SWTUtil;
import melnorme.util.swt.components.LabelledFieldWidget;

public class RadioSelectionField<E extends Enum<?>> extends LabelledFieldWidget<E> {
	
	protected final E[] values;
	protected final Button[] radioButtons;
	
	public RadioSelectionField(E[] values) {
		super(null, Option_AllowNull.NO, values[0]);
		assertTrue(values != null && values.length > 0);
		this.values = values;
		
		radioButtons = new Button[values.length];
	}
	
	public int getSelectionIndex() {
		return findIndexForValue(getFieldValue());
	}
	
	protected int findIndexForValue(E value) {
		int ix;
		for (ix = 0; ix < values.length; ix++) {
			if(values[ix] == value) {
				return ix;
			}
		}
		return -1;
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public int getPreferredLayoutColumns() {
		return 1;
	}
	
	@Override
	protected void createContents_all(Composite topControl) {
		createContents_RadioButtons(topControl);
	}
	
	protected void createContents_RadioButtons(Composite topControl) {
		for (int i = 0; i < values.length; i++) {
			final int index = i;
			
			E value = values[i];
			String label = getLabelForValue(value);
			radioButtons[index] = SWTFactoryUtil.createButton(topControl, SWT.RADIO, label);
			radioButtons[index].addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					E newValue = getValueFromControl();
					if(newValue != getFieldValue()) {
						setFieldValueFromControl(newValue);
					}
				}
			});
		}
	}
	
	protected String getLabelForValue(E value) {
		return value.toString();
	}
	
	@Override
	protected void createContents_layout() {
		GridLayout gridLayout = (GridLayout) getFieldControl().getParent().getLayout();
		int numColumns = gridLayout.numColumns;
		
		for(Button button : radioButtons) {
			button.setLayoutData(gdfFillDefaults().span(numColumns, 1).grab(true, false).create());
		}
	}
	
	@Override
	public Button getFieldControl() {
		// Use selection index ?
		return radioButtons[0];
	}
	
	public Button[] getRadioControls() {
		 return radioButtons;
	}
	
	@Override
	protected void doUpdateWidgetFromValue() {
		int indexValue = getSelectionIndex();
		
		setControlButtonSelection(indexValue);
		assertTrue(getValueFromControl() == getFieldValue());
	}
	
	protected void setControlButtonSelection(int indexValue) {
		for (int ix = 0; ix < radioButtons.length; ix++) {
			Button button = radioButtons[ix];
			boolean newSelection = ix == indexValue;
			if(button.getSelection() != newSelection) {
				button.setSelection(newSelection);
			}
		}
	}
	
	protected E getValueFromControl() {
		for(int ix = 0; ix < radioButtons.length; ix++) {
			if(radioButtons[ix].getSelection()) {
				return values[ix];
			}
		}
		return null;
	}
	
	@Override
	protected void doSetEnabled(boolean enabled) {
		for(Button button : radioButtons) {
			SWTUtil.setEnabledIfOk(button, enabled);
		}
	}
	
}