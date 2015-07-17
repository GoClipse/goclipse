/*******************************************************************************
 * Copyright (c) 2014, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.util.swt;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.CoreUtil.array;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Control;

import melnorme.utilbox.misc.ArrayUtil;

public class SWTLayoutUtil {
	
	public static <CONTROL extends Control, DATA> CONTROL setLayoutData(CONTROL control, Object layoutData) {
		control.setLayoutData(layoutData);
		return control;
	}

	public static void layout1Control(Control lastControl) {
		layoutControls(array(lastControl), lastControl, lastControl);
	}
	
	public static void layout2Controls(Control firstControl, Control lastControl) {
		layoutControls(array(firstControl, lastControl), null, null);
	}
	
	public static void layout2Controls_expandLast(Control firstControl, Control lastControl) {
		layoutControls(array(firstControl, lastControl), lastControl, lastControl);
	}
	
	public static void layout2Controls_spanLast(Control firstControl, Control lastControl) {
		layoutControls(array(firstControl, lastControl), null, lastControl);
	}
	
	public static GridData layoutExpandedControl(Control lastControl) {
		GridDataFactory gdf = GridDataFactory.fillDefaults().grab(true, false);
		GridData gridData = gdf.create();
		lastControl.setLayoutData(gridData);
		return gridData;
	}
	
	/**
	 * Layout given controls with GridData instances.
	 * Optionally, given grabbingControl will have a GridData grabbing vertical space.
	 * Optionally, given colSpanningControl will have a GridData spanning extra columns in parent layout. 
	 */
	public static void layoutControls(Control[] controls, Control grabbingControl, Control colSpanningControl) {
		assertNotNull(controls);
		
		controls = ArrayUtil.removeAll(controls, null);
		
		if(controls.length == 0) {
			return;
		}
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
				gridData = GridDataFactory.swtDefaults().create();
				control.setLayoutData(gridData);
			}
			
			if(control == colSpanningControl) {
				gridData.horizontalSpan = extraColumns + 1;
			}
			
		}
		
	}
	
}