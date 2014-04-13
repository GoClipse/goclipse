/*******************************************************************************
 * Copyright (c) 2007 DSource.org and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial implementation
 *******************************************************************************/
package melnorme.util.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * A Composite which creates setups a default GridData if parent has a GridLayout. 
 */
public class BaseComposite extends Composite {

	/** Creates a composite with no margins and SWT default spacing. */
	public BaseComposite(Composite parent) {
		super(parent, SWT.NONE);
		
		if(parent.getLayout() instanceof GridLayout) {
			setLayoutData(SWTLayoutUtil.newRowGridData());
		}
	}

	/** Sets the enable state of this control and of all children. */
	public void recursiveSetEnabled(boolean enabled) {
		super.setEnabled(enabled);
		SWTUtil.recursiveSetEnabled(this, enabled);
	}
}