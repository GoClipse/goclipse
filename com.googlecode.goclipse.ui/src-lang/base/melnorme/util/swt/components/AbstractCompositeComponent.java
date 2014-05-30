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
import org.eclipse.swt.widgets.Composite;

public abstract class AbstractCompositeComponent extends AbstractComponent {
	
	@Override
	public Composite createComponent(Composite parent) {
		Composite composite = super.doCreateComponent(parent);
		updateComponentFromInput();
		return composite;
	}
	
	protected abstract void updateComponentFromInput();
	
	/* ----------------- util ----------------- */
	
	protected static GridDataFactory gdSwtDefaults() {
		return GridDataFactory.swtDefaults();
	}
	
	protected static GridDataFactory gdFillDefaults() {
		return GridDataFactory.fillDefaults();
	}
	
}