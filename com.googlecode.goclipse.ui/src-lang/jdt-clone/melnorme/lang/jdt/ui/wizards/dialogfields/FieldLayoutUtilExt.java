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
package melnorme.lang.jdt.ui.wizards.dialogfields;

import melnorme.util.swt.GridComposite;

import org.eclipse.swt.widgets.Composite;

public class FieldLayoutUtilExt extends LayoutUtil {


	/** Does {@link #doDefaultLayout(Composite, DialogField[], boolean)} .*/
	public static void doDefaultLayout2(Composite parent, boolean labelOnTop, DialogField... fields) {
		doDefaultLayout(parent, fields, labelOnTop);
	}

	/** Does {@link #doDefaultLayout(Composite, DialogField[], boolean, int, int)} . */
	public static void doDefaultLayout2(Composite parent, boolean labelOnTop,
			int marginWidth, int marginHeight, DialogField... fields) {
		doDefaultLayout(parent, fields, labelOnTop, marginWidth, marginHeight);
	}

	/** Creates a Composite and fills in the given fields. */
	public static Composite createCompose(Composite parent, boolean labelOnTop, DialogField field) {
		Composite content = new GridComposite(parent, field.getNumberOfControls());
		FieldLayoutUtilExt.doDefaultLayout2(content, labelOnTop, field);
		return content;
	}
}
