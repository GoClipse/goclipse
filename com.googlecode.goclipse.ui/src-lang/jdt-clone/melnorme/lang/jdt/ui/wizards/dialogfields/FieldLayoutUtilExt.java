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

import org.eclipse.swt.widgets.Composite;

public class FieldLayoutUtilExt extends LayoutUtil {


	/** Does {@link #doDefaultLayout(Composite, DialogField[], boolean)} .*/
	public static void doDefaultLayout2(Composite parent, boolean labelOnTop, DialogField... fields) {
		doDefaultLayout(parent, fields, labelOnTop);
	}

}
