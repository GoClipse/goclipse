/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
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

import melnorme.utilbox.collections.Indexable;

public abstract class AbstractCompositeWidget extends AbstractDisableableWidget implements IDisableableWidget {
	
	public AbstractCompositeWidget() {
	}
	
	@Override
	protected final void createContents(Composite topControl) {
		getSubWidgets().forEach(subComponent -> {
			subComponent.createComponentInlined(topControl);
		});
	}
	
	@Override
	protected final void doSetEnabled(boolean enabled) {
		getSubWidgets().forEach(subComponent -> {
			subComponent.setEnabled(enabled);
		});
	}
	
	protected abstract Indexable<IDisableableWidget> getSubWidgets();
	
	@Override
	protected void _verifyContract_IDisableableComponent() {
		// No need to check, only possible children are AbstractComponentExt
	}
	
}