/*******************************************************************************
 * Copyright (c) 2014, 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.utilbox.ownership;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import melnorme.utilbox.collections.ArrayList2;

@SuppressWarnings("serial")
public class OwnedObjects extends ArrayList2<IDisposable> implements IOwner, IDisposable {
	
	protected boolean disposed = false;
	
	public OwnedObjects() {
	}
	
	protected void checkDisposed() {
		assertTrue(disposed == false);
	}
	
	@Override
	public void bind(IDisposable disposable) {
		add(disposable);
	}
	
	@Override
	public boolean unbind(IDisposable disposable) {
		return remove(disposable);
	}
	
	@Override
	public boolean add(IDisposable e) {
		checkDisposed();
		return super.add(e);
	}
	
	@Override
	public IDisposable remove(int index) {
		checkDisposed();
		return super.remove(index);
	}
	
	@Override
	public boolean remove(Object o) {
		checkDisposed();
		return super.remove(o);
	}
	
	@Override
	public void dispose() {
		disposeAll();
	}
	
	@Override
	public void disposeAll() {
		for(IDisposable disposable : this) {
			disposable.dispose();
		}
		this.clear();
		disposed = true;
	}
	
}