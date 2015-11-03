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

/**
 * An {@link AbstractComponent} extended with {@link #setEnabled(boolean)} functionality.
 */
public abstract class AbstractComponentExt extends AbstractComponent implements IDisableableComponent {
	
	public AbstractComponentExt() {
	}
	
	@Override
	public void _verifyContract() {
		_verifyContract_setEnabled();
	}
	
	protected void _verifyContract_setEnabled() {
		// Verify the contract of IDisableableComponent
		IDisableableComponent.super._verifyContract();
	}
	
}