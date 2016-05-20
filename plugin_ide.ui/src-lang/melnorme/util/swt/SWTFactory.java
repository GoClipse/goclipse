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
package melnorme.util.swt;

import static melnorme.util.swt.SWTLayoutUtil.setLayoutData;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.PixelConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;

public class SWTFactory {
	
	/** A version of {@link SelectionAdapter} that works better for Java 8 lambdas */
	public static interface WidgetSelectedListener extends SelectionListener {
		@Override
		default void widgetDefaultSelected(SelectionEvent e) {
		}
	}
	
	/** A version of {@link SelectionAdapter} that works better for Java 8 lambdas */
	public static interface WidgetDefaultSelectedListener extends SelectionListener {
		@Override
		default void widgetSelected(SelectionEvent e) {
		}
	}
	
	/* -----------------  ----------------- */
	
	public static Composite createComposite(Composite parent, GridData gridData) {
		Composite composite = new Composite(parent, SWT.NONE);
		return setLayoutData(composite, gridData);
	}
	
	public static Composite createComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		return composite;
	}
	
	public static Composite createGridComposite(Composite parent, int columns, GridData gridData) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(GridLayoutFactory.fillDefaults().numColumns(columns).create());
		if(gridData != null) {
			composite.setLayoutData(gridData);
		}
		return composite;
	}
	
	/* -----------------  ----------------- */
	
	public static Group createGroup(Composite parent, String label, GridData gridData) {
		Group group = createGroup(parent, label);
		return setLayoutData(group, gridData);
	}
	
	public static Group createGroup(Composite parent, String label) {
		return createGroup(parent, label, SWT.SHADOW_NONE);
	}
	
	public static Group createGroup(Composite parent, String label, int style) {
		Group group = new Group(parent, style);
		group.setText(label);
		return group;
	}
	
	/* -----------------  ----------------- */
	
	public static Label createIconLabel(Composite parent, Image image, GridData gridData) {
		Label label = createLabel(parent, SWT.LEFT, "");
		label.setImage(image);
		return setLayoutData(label, gridData);
	}
	
	public static Label createLabel(Composite parent, int style, String labelText, GridData gridData) {
		Label label = createLabel(parent, style, labelText);
		return setLayoutData(label, gridData);
	}

	public static Label createLabel(Composite parent, int style, String labelText) {
		Label label = new Label(parent, style);
		label.setText(labelText);
		return label;
	}
	
	public static Link createLink(Composite parent, int style, String labelText, GridData gridData) {
		Link link = createLink(parent, style, labelText);
		return setLayoutData(link, gridData);
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
		return setLayoutData(button, gridData);
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
	
	public static Button createPushButton(Composite parent, String label, Image image) {
		return createPushButton(parent, label, image, new GridData());
	}
	
	public static Button createPushButton(Composite parent, String label, Image image, GridData gridData) {
		Button button = createButton(parent, SWT.PUSH, label, image, gridData);
		if(gridData != null) {
			gridData.minimumWidth = getButtonWidthHint(button);
			setLayoutData(button, gridData);
		}
		return button;
	}
	
	public static int getButtonWidthHint(Button button) {
		PixelConverter converter = new PixelConverter(button);
		int widthHint = converter.convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
		return Math.max(widthHint, button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
	}
	
	public static Button createPushButton(Composite parent, String label, Image image, GridData gridData, 
			WidgetSelectedListener listener) {
		Button button = createPushButton(parent, label, image);
		button.addSelectionListener(listener);
		return setLayoutData(button, gridData);
	}
	
	/* -----------------  ----------------- */
	
	public static Text createText(Composite parent, int style) {
		return new Text(parent, style);
	}
	
	public static Text createText(Composite parent, int style, GridData gridData) {
		Text text = createText(parent, style);
		return setLayoutData(text, gridData);
	}
	
	public static Text createReadonlyText(Composite dialogArea, String initialText) {
		int style = SWT.BORDER | SWT.READ_ONLY;
		if(true) {
			style |= SWT.MULTI | SWT.WRAP;
		}
		Text text = createText(dialogArea, style);
		text.setText(initialText);
		return text;
	}
	
	public static Text createReadonlyText(Composite dialogArea, String initialText, GridData gridData) {
		Text text = createReadonlyText(dialogArea, initialText);
		return setLayoutData(text, gridData);
	}
	
	/* -----------------  ----------------- */
	
	public static Combo createCombo(Composite parent, int style) {
		Combo combo = new Combo(parent, style);
		return combo;
	}
	
	public static Combo createCombo(Composite parent, int style, GridData gridData) {
		Combo combo = createCombo(parent, style);
		return setLayoutData(combo, gridData);
	}
	
}