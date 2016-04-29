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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

/**
 * Common class for UI components, or composite widgets, using SWT.
 * Can be created in two ways: 
 *  the standard way - only one child control is created under parent. 
 *  inlined - many child controls created under parent control. This allows more flexibility for more complex layouts. 
 */
public abstract class AbstractWidget implements IWidgetComponent {
	
	public AbstractWidget() {
		_verifyContract();
	}
	
	protected void _verifyContract() {
		_verifyContract_IDisableableComponent();
	}
	
	@Override
	public Composite createComponent(Composite parent) {
		Composite topControl = createTopLevelControl(parent);
		createContents(topControl);
		updateWidgetFromInput();
		return topControl;
	}
	
	@Override
	public void createComponentInlined(Composite parent) {
		createContents(parent);
		updateWidgetFromInput();
	}
	
	protected Composite createTopLevelControl(Composite parent) {
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
	
	/* ----------------- optional setEnabled ----------------- */
	
	protected void _verifyContract_IDisableableComponent() {
		if(this instanceof IDisableableWidget) {
			IDisableableWidget disableableComponent = (IDisableableWidget) this;
			disableableComponent._IDisableableComponent$verifyContract(); // Eagerly verify contract 
		}
	}
	
	/* -----------------  ----------------- */
	
	/** 
	 * Update the widget controls from whatever is considerd the input, or source, of the control.
	 */
	protected abstract void updateWidgetFromInput();
	
 	
	/* ----------------- Shortcut utils ----------------- */
	
	public static GridDataFactory gdfSwtDefaults() {
		return GridDataFactory.swtDefaults();
	}
	
	public static GridDataFactory gdfFillDefaults() {
		return GridDataFactory.fillDefaults();
	}
	
	public static GridDataFactory gdfGrabHorizontal() {
		return gdfGrabHorizontal(SWT.DEFAULT);
	}
	
	public static GridDataFactory gdfGrabHorizontal(int hHint) {
		return gdfFillDefaults().grab(true, false).hint(hHint, SWT.DEFAULT);
	}
	
	public static GridData gdGrabHorizontal() {
		return gdGrabHorizontal(SWT.DEFAULT);
	}
	
	public static GridData gdGrabHorizontal(int hHint) {
		return gdfGrabHorizontal(hHint).create();
	}
	
	public static GridData gdGrabAll(int xHint, int yHint) {
		return gdfFillDefaults().grab(true, true).hint(xHint, yHint).create();
	}
	
}