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
package melnorme.utilbox.ownership;

public class LifecycleObject implements IDisposable {
	
	protected final OwnedObjects owned = new OwnedObjects(); 
	
	public LifecycleObject() {
		super();
	}
	
	public IOwner asOwner() {
		return owned;
	}
	
	@Override
	public final void dispose() {
		dispose_pre();
		owned.disposeAll();
		dispose_post();
	}
	
	protected void dispose_pre() {
	}
	
	protected void dispose_post() {
	}
	
}