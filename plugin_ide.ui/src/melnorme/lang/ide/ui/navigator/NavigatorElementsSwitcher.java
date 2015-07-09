/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.navigator;

import org.eclipse.core.filesystem.IFileStore;

import com.googlecode.goclipse.ui.navigator.elements.GoPathElement;

import melnorme.lang.tooling.LANG_SPECIFIC;

@LANG_SPECIFIC
public interface NavigatorElementsSwitcher<RET> extends NavigatorElementsSwitcher_Default<RET> {
	
	@Override
	default RET switchElement(Object element) {
		if(element instanceof GoPathElement) {
			return visitGoPathElement((GoPathElement) element);
		}
		if(element instanceof IFileStore) {
			return visitFileStoreElement((IFileStore) element);
		}
		return NavigatorElementsSwitcher_Default.super.switchElement(element);
	}
	
	public abstract RET visitGoPathElement(GoPathElement goPathElement);
	
	public abstract RET visitFileStoreElement(IFileStore fileStore);
	
}