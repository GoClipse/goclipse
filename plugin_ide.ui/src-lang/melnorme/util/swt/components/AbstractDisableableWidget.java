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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import melnorme.lang.ide.ui.preferences.common.AbstractWidgetExt;

/**
 * An {@link AbstractWidget} extended with {@link #setEnabled(boolean)} functionality.
 */
public abstract class AbstractDisableableWidget extends AbstractWidgetExt 
	implements IDisableableWidget {
	
	protected AbstractDisableableWidget parent;
	protected boolean enabled = true;
	
	public AbstractDisableableWidget() {
	}
	
	public void setParent(AbstractDisableableWidget parent) {
		assertTrue(this.parent == null);
		this.parent = assertNotNull(parent);
	}
	
	public boolean isEnabled() {
		return enabled && (parent == null || parent.isEnabled());
	}
	
	@Override
	public final void setEnabled(boolean enabled) {
		this.enabled = enabled;
		updateControlEnablement2();
	}
	
	protected void updateControlEnablement2() {
		doSetEnabled(isEnabled());
		updateValidationStatusForEnablement();
	}
	
	protected abstract void doSetEnabled(boolean enabled);
	
	protected void updateValidationStatusForEnablement() {
		if(!isEnabled()) {
			validation.set(null);
		} else {
			validation.updateFieldValue();
		}
	}
	
	@Override
	public void updateWidgetFromInput() {
		updateControlEnablement2();
		doUpdateWidgetFromInput();
	}
	
	protected void doUpdateWidgetFromInput() {
	}
	
}