/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package melnorme.util.swt;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

public class MetricsUtil {

	/** Gets FontMetrics for the given control */
	protected static FontMetrics getFontMetrics(Control testControl) {
		FontMetrics fontMetrics;
		// Compute and store a font metric
		GC gc = new GC(testControl);
		gc.setFont(JFaceResources.getDialogFont());
		fontMetrics = gc.getFontMetrics();
		gc.dispose();
		return fontMetrics;
	}

	/** Initializes a layout with dialog unit settings. 
	 * @param testControl the control used to obtain FontMetrics. */
	public static void initGridLayoutWithDLUs(GridLayout layout, Control testControl) {
	
		FontMetrics fontMetrics = getFontMetrics(testControl);
	
		layout.horizontalSpacing = Dialog.convertHorizontalDLUsToPixels(
				fontMetrics, IDialogConstants.HORIZONTAL_SPACING);
		layout.verticalSpacing = Dialog.convertHorizontalDLUsToPixels(
				fontMetrics, IDialogConstants.VERTICAL_SPACING);
		layout.marginWidth = Dialog.convertHorizontalDLUsToPixels(fontMetrics,
				IDialogConstants.HORIZONTAL_MARGIN);
		layout.marginHeight = Dialog.convertHorizontalDLUsToPixels(fontMetrics,
				IDialogConstants.VERTICAL_MARGIN);
	}

	/** Initializes a GridLayout with some default settings.
	 * @param margins if the layout should have margins
	 * @param testControl the control used to obtain FontMetrics. If null then 
	 * SWT defaults will be used
	 */
	public static void initGridLayout(GridLayout gl, boolean margins, Control testControl) {
		if(testControl != null)
			initGridLayoutWithDLUs(gl, testControl);
	
		if(!margins) {
			gl.marginWidth = 0;
			gl.marginHeight = 0;
		}
	}

	/** Creates and initializes a new GridLayout with numColumns and margins. */
	public static Layout createGridLayout(int numColumns, Control testControl) {
		GridLayout gd = new GridLayout(numColumns, false);
		initGridLayout(gd, true, testControl);
		return gd;
	}

}
