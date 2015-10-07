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

import melnorme.utilbox.collections.Indexable;

public class LifecycleObject implements IDisposable {
	
	protected final OwnedObjects owned = new OwnedObjects(); 
	
	public LifecycleObject() {
		super();
	}
	
	@Override
	public void dispose() {
		owned.disposeAll();
	}
	
	public Indexable<IDisposable> getOwned() {
		return owned;
	}
	
}