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
package melnorme.util.swt.components;

import org.eclipse.swt.widgets.Composite;

import melnorme.util.swt.SWTLayoutUtil;

public interface IWidgetComponent {
	
	/**
	 * Create the component controls under a single top-level Control. 
	 */
	public Composite createComponent(Composite parent);
	
	/** Do {@link #createComponent(Composite)}, and also set the layout data of created Control.  */
 	default Composite createComponent(Composite parent, Object layoutData) {
 		Composite control = createComponent(parent);
 		return SWTLayoutUtil.setLayoutData(control, layoutData);
 	}
 	
	/**
	 * Create the component controls directly on given parent.
	 * This places restriction on parent: it must have a GridLayout, 
	 * with a minimum number of columns that can accomodate the children. 
	 */
	public void createComponentInlined(Composite parent);
	
}