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

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.widgets.Control;

public class LayoutUtils {

	// TODO: generalize this
	public static void layout2Controls(int numColumns, Control control1, Control expandingControl) {
		control1.setLayoutData(GridDataFactory.swtDefaults().create());
		numColumns--;
		if(numColumns == 0) {
			numColumns = 1;
		}
		expandingControl.setLayoutData(GridDataFactory.fillDefaults().span(numColumns, 1).create());
	}
	
}
