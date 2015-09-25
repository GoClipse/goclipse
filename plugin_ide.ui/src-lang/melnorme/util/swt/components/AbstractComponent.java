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

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import melnorme.util.swt.SWTLayoutUtil;

/**
 * Common class for UI components, or composite widgets, using SWT.
 * Can be created in two ways: 
 *  the standard way - only one child control is created under parent. 
 *  inlined - many child controls created under parent control. This allows more flexibility for more complex layouts. 
 */
public abstract class AbstractComponent implements IWidgetComponent {
	
	public AbstractComponent() {
	}
	
	@Override
	public Composite createComponent(Composite parent) {
		Composite topControl = createTopLevelControl(parent);
		createContents(topControl);
		updateComponentFromInput();
		return topControl;
	}
	
	@Override
	public void createComponentInlined(Composite parent) {
		createContents(parent);
		updateComponentFromInput();
	}
	
	protected final Composite createTopLevelControl(Composite parent) {
		Composite topControl = doCreateTopLevelControl(parent);
		topControl.setLayout(createTopLevelLayout().create());
		return topControl;
	}
	
	protected Composite doCreateTopLevelControl(Composite parent) {
		return new Composite(parent, SWT.NONE);
	}
	
	protected GridLayoutFactory createTopLevelLayout() {
		return GridLayoutFactory.fillDefaults().numColumns(getPreferredLayoutColumns());
	}
	
	public abstract int getPreferredLayoutColumns();
	
	protected abstract void createContents(Composite topControl);
	
	/* ----------------- util ----------------- */
	
	/** Do {@link #createComponent(Composite)}, and also set the layout data of created Control.  */
 	public final Composite createComponent(Composite parent, Object layoutData) {
 		Composite control = createComponent(parent);
 		return SWTLayoutUtil.setLayoutData(control, layoutData);
 	}
 	
	/* -----------------  ----------------- */
	
	/** 
	 * Update the controls of the components from whatever is considerd the input, or source, of the control.
	 */
	protected abstract void updateComponentFromInput();
	
 	
	/* ----------------- Shortcut utils ----------------- */
	
	protected static GridDataFactory gdSwtDefaults() {
		return GridDataFactory.swtDefaults();
	}
	
	protected static GridDataFactory gdFillDefaults() {
		return GridDataFactory.fillDefaults();
	}
	
	protected static GridLayoutFactory glSwtDefaults() {
		return GridLayoutFactory.swtDefaults();
	}
	
	protected static GridLayoutFactory glFillDefaults() {
		return GridLayoutFactory.fillDefaults();
	}
	
}