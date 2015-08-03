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
package melnorme.utilbox.collections;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.util.List;

import melnorme.utilbox.core.CoreUtil;

public class ListView<K> extends CollectionView<K> implements Indexable<K> {
	
	protected final List<K> list;
	
	public ListView(List<K> list) {
		super(list);
		this.list = assertNotNull(list);
	}
	
	@Override
	public <T> ListView<T> upcastTypeParameter() {
		return CoreUtil.blindCast(this);
	}

	@Override
	public K get(int index) {
		return list.get(index);
	}
	
}