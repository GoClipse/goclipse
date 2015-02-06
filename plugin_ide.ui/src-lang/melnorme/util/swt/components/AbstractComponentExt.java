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

import melnorme.util.swt.SWTFactoryUtil;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.PixelConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.dialogs.PreferencesUtil;

/**
 * Same as AbstractComponent, but with a few more helpers and functionality.
 */
public abstract class AbstractComponentExt extends AbstractComponent {
	
	@Override
	public final Composite createComponent(Composite parent) {
		Composite topControl = createTopLevelControl(parent);
		createContents(topControl);
		updateComponentFromInput();
		return topControl;
	}
	
	@Override
	public void createComponentInlined(Composite parent) {
		super.createComponentInlined(parent);
		updateComponentFromInput();
	}
	
	/** 
	 * Update the components controls from some intrinsic notion of a configured input.
	 * Can do nothing if there is not configured input. 
	 */
	public abstract void updateComponentFromInput();
	
	/* ----------------- util ----------------- */
	
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
	
	/* ----------------- helpers ----------------- */
	
	protected void createHorizontalSpacer(final Composite topControl, int charHeight, PixelConverter pc) {
		SWTFactoryUtil.createLabel(topControl, SWT.LEFT, "", 
			gdFillDefaults().hint(pc.convertHeightInCharsToPixels(charHeight), SWT.DEFAULT).create());
	}
	
	public static Composite createComposite(final Composite topControl) {
		Composite editorComposite = new Composite(topControl, SWT.NONE);
		editorComposite.setLayoutData(gdFillDefaults().grab(false, true).create());
		return editorComposite;
	}
	
	public static Link createOpenPreferencesDialogLink(final Composite topControl, String linkText) {
		Link link = new Link(topControl, SWT.NONE);
		link.setText(linkText);
		link.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				PreferencesUtil.createPreferenceDialogOn(topControl.getShell(), e.text, null, null);
			}
		});
		return link;
	}
	
}