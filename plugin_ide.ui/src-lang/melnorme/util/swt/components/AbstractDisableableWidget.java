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

import melnorme.lang.ide.ui.preferences.common.AbstractWidgetExt;

/**
 * An {@link AbstractWidgetExt} extended with {@link #setEnabled(boolean)} functionality.
 */
public abstract class AbstractDisableableWidget extends AbstractWidgetExt implements IDisableableWidget {
	
	public AbstractDisableableWidget() {
	}
	
	protected boolean enabled = true;
	
	@Override
	public final void setEnabled(boolean enabled) {
		this.enabled = enabled;
		updateControlEnablement();
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	protected void updateControlEnablement() {
		doSetEnabled(isEnabled());
	}
	
	protected abstract void doSetEnabled(boolean enabled);
	
	
	@Override
	protected final void updateComponentFromInput() {
		doUpdateComponentFromInput();
		updateControlEnablement();
	}
	
	protected void doUpdateComponentFromInput() {
	}
	
}