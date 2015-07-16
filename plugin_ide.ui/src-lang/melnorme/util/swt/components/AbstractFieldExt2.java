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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.swt.widgets.Composite;

/**
 * Some extended functionality to {@link AbstractFieldComponent}.
 * 
 */
public abstract class AbstractFieldExt2<VALUE> extends AbstractFieldComponent<VALUE>{
	
	protected final VALUE defaultFieldValue;
	protected String labelText;
	
	public AbstractFieldExt2(String labelText, VALUE defaultFieldValue) {
		super(assertNotNull(defaultFieldValue));
		this.labelText = labelText;
		this.defaultFieldValue = defaultFieldValue;
	}
	
	@Override
	public final VALUE getDefaultFieldValue() {
		return defaultFieldValue;
	}
	
	@Override
	public VALUE getFieldValue() {
		return assertNotNull(super.getFieldValue());
	}
	
	public String getLabelText() {
		return labelText;
	}
	
	@Override
	protected final void createContents(Composite topControl) {
		createContents_do(topControl);
		createContents_layout();
	}
	
	protected abstract void createContents_do(Composite topControl);
	
	protected abstract void createContents_layout();
	
}