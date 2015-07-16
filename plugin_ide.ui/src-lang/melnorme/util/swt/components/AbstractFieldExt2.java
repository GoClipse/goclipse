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
package melnorme.util.swt.components;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import static melnorme.utilbox.core.CoreUtil.array;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Some extended functionality to {@link AbstractFieldComponent}.
 * 
 */
public abstract class AbstractFieldExt2<VALUE> extends AbstractFieldComponent<VALUE>{
	
	protected final VALUE defaultFieldValue;
	protected String labelText;
	
	public AbstractFieldExt2(String labelText, VALUE defaultFieldValue) {
		super(assertNotNull(defaultFieldValue));
		this.labelText = labelText;
		this.defaultFieldValue = defaultFieldValue;
	}
	
	@Override
	public final VALUE getDefaultFieldValue() {
		return defaultFieldValue;
	}
	
	@Override
	public VALUE getFieldValue() {
		return assertNotNull(super.getFieldValue());
	}
	
	public String getLabelText() {
		return labelText;
	}
	
	@Override
	protected final void createContents(Composite topControl) {
		createContents_do(topControl);
		createContents_layout();
	}
	
	protected abstract void createContents_do(Composite topControl);
	
	protected abstract void createContents_layout();
	
	
	public void layout1Control(Control lastControl) {
		layoutControls(array(lastControl), lastControl, lastControl);
	}
	
	public void layout2Controls_expandLast(Control firstControl, Control lastControl) {
		layoutControls(array(firstControl, lastControl), lastControl, lastControl);
	}
	
	public void layout2Controls_spanLast(Control firstControl, Control lastControl) {
		layoutControls(array(firstControl, lastControl), null, lastControl);
	}
	
	/**
	 * Layout given controls with GridData instances.
	 * Optionally, given grabbingControl will have a GridData grabbing vertical space.
	 * Optionally, given colSpanningControl will have a GridData spanning extra columns in parent layout. 
	 */
	protected void layoutControls(Control[] controls, Control grabbingControl, Control colSpanningControl) {
		assertTrue(controls != null && controls.length > 0);
		GridLayout gridLayout = (GridLayout) controls[0].getParent().getLayout();
		
		int extraColumns = gridLayout.numColumns - controls.length; 
		if(extraColumns < 0) {
			extraColumns = 0;
		}
		
		for (int ix = 0; ix < controls.length; ix++) {
			Control control = controls[ix];
			
			GridData gridData;
			if(control == grabbingControl) {
				gridData = layoutExpandedControl(control);
			} else {
				gridData = gdSwtDefaults().create();
				control.setLayoutData(gridData);
			}
			
			if(control == colSpanningControl) {
				gridData.horizontalSpan = extraColumns + 1;
			}
			
		}
		
	}
	
	protected GridData layoutExpandedControl(Control lastControl) {
		GridDataFactory gdf = GridDataFactory.fillDefaults().grab(true, false);
		GridData gridData = gdf.create();
		lastControl.setLayoutData(gridData);
		return gridData;
	}
	
}