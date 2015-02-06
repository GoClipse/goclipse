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
package melnorme.util.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;

public class SWTFactory {
	
	public static Group createGroup(Composite parent, String label, GridData gridData) {
		Group group = createGroup(parent, label);
		group.setLayoutData(gridData);
		return group;
	}
	
	public static Group createGroup(Composite parent, String label) {
		return createGroup(parent, label, SWT.SHADOW_NONE);
	}
	
	public static Group createGroup(Composite parent, String label, int style) {
		Group group = new Group(parent, style);
		group.setText(label);
		return group;
	}
	
	public static Label createLabel(Composite parent, int style, String labelText, GridData gridData) {
		Label label = createLabel(parent, style, labelText);
		label.setLayoutData(gridData);
		return label;
	}

	public static Label createLabel(Composite parent, int style, String labelText) {
		Label label = new Label(parent, style);
		label.setText(labelText);
		return label;
	}
	
	public static Link createLink(Composite parent, int style, String labelText, GridData gridData) {
		Link link = createLink(parent, style, labelText);
		link.setLayoutData(gridData);
		return link;
	}
	
	public static Link createLink(Composite parent, int style, String labelText) {
		Link link = new Link(parent, style);
		link.setText(labelText);
		return link;
	}
	
	public static Button createButton(Composite parent, int style, String label, GridData gridData) {
		return createButton(parent, style, label, null, gridData);
	}
	public static Button createButton(Composite parent, int style, String label, Image image, GridData gridData) {
		Button button = createButton(parent, style, label, image);
		button.setLayoutData(gridData);
		return button;
	}
	
	public static Button createButton(Composite parent, int style, String label) {
		return createButton(parent, style, label, (Image) null);
	}
	public static Button createButton(Composite parent, int style, String label, Image image) {
		Button button = new Button(parent, style);
		if(image != null) {
			button.setImage(image);
		}
		if(label != null) {
			button.setText(label);
		}
		return button;
	}
	
}