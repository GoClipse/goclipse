/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package melnorme.util.swt;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.PixelConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

public class SWTFactoryUtil {
	
	/**
	 * Creates and returns a new push button with the given
	 * label and/or image.
	 * 
	 * @param parent parent control
	 * @param label button label or <code>null</code>
	 * @param image image of <code>null</code>
	 * 
	 * @return a new push button
	 */
	public static Button createPushButton(Composite parent, String label, Image image) {
		Button button = new Button(parent, SWT.PUSH);
		button.setFont(parent.getFont());
		if (image != null) {
			button.setImage(image);
		}
		if (label != null) {
			button.setText(label);
		}
		GridData gd = new GridData();
		button.setLayoutData(gd);	
		setButtonDimensionHint(button);
		return button;	
	}
	
	/**
	 * Sets width and height hint for the button control.
	 * <b>Note:</b> This is a NOP if the button's layout data is not
	 * an instance of <code>GridData</code>.
	 * 
	 * @param	the button for which to set the dimension hint
	 */		
	public static void setButtonDimensionHint(Button button) {
		Assert.isNotNull(button);
		Object gd= button.getLayoutData();
		if (gd instanceof GridData) {
			((GridData)gd).widthHint= getButtonWidthHint(button);	
			((GridData)gd).horizontalAlignment = GridData.FILL;	 
		}
	}
	
	/**
	 * Returns a width hint for a button control.
	 */
	public static int getButtonWidthHint(Button button) {
		/*button.setFont(JFaceResources.getDialogFont());*/
		PixelConverter converter= new PixelConverter(button);
		int widthHint= converter.convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
		return Math.max(widthHint, button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
	}
	
	public static Composite createGroup(Composite parent, String label, GridData layoutData) {
		Group group = new Group(parent, SWT.SHADOW_NONE);
		group.setText(label);
		group.setLayoutData(layoutData);
		return group;
	}
	
	public static Label createLabel(Composite parent, int style, String labelText, GridData layoutData) {
		Label label = new Label(parent, style);
		label.setText(labelText);
		label.setLayoutData(layoutData);
		return label;
	}
	
}