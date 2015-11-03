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

public abstract class AbstractCompositeComponent extends AbstractComponentExt {
	
	public AbstractCompositeComponent() {
	}
	
	@Override
	protected final void createContents(Composite topControl) {
		for(AbstractComponentExt subComponent : getSubComponents()) {
			subComponent.createComponentInlined(topControl);
		}
	}
	
	@Override
	public final void setEnabled(boolean enabled) {
		for(AbstractComponentExt subComponent : getSubComponents()) {
			subComponent.setEnabled(enabled);
		}
	}
	
	protected abstract Indexable<AbstractComponentExt> getSubComponents();
	
	@Override
	protected void _verifyContract_setEnabled() {
		// No need to check, only possible children are AbstractComponentExt
	}
	
}