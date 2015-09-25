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
package melnorme.util.swt.components.fields;

import static melnorme.utilbox.core.CoreUtil.array;

import org.eclipse.jface.layout.PixelConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import melnorme.util.swt.SWTLayoutUtil;

public class TextField2 extends TextFieldComponent {
	
	protected final int textLimit;
	
	public TextField2(String labelText, int textLimit) {
		super(labelText, SWT.BORDER | SWT.SINGLE);
		this.textLimit = textLimit;
	}
	
	@Override
	protected Text createText_2(Composite parent) {
		Text text = super.createText_2(parent);
		text.setTextLimit(textLimit);
		return text;
	}
	
	@Override
	protected void createContents_layout() {
		SWTLayoutUtil.layoutControls(array(label, text), null, text);
		
		GridData textGD = (GridData) text.getLayoutData();
		PixelConverter pixelConverter = new PixelConverter(text.getParent());
		textGD.widthHint = pixelConverter.convertWidthInCharsToPixels(textLimit + 1);
	}
	
}