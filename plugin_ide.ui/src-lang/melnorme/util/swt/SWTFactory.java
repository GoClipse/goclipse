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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

public class SWTFactory {
	
	public static Group createGroup(Composite parent, String label, GridData layoutData) {
		Group group = createGroup(parent, label);
		group.setLayoutData(layoutData);
		return group;
	}
	
	public static Group createGroup(Composite parent, String label) {
		Group group = new Group(parent, SWT.SHADOW_NONE);
		group.setText(label);
		return group;
	}
	
	public static Label createLabel(Composite parent, int style, String labelText, GridData layoutData) {
		Label label = createLabel(parent, style, labelText);
		label.setLayoutData(layoutData);
		return label;
	}

	public static Label createLabel(Composite parent, int style, String labelText) {
		Label label = new Label(parent, style);
		label.setText(labelText);
		return label;
	}
	
}