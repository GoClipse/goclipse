/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.util.swt.jface;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;

public interface IStructuredContentProvider0<T> extends IStructuredContentProvider {
	
	@Override
	public Object[] getElements(Object inputElement);
	
	public List<T> getElements0(Object inputElement);
	
}