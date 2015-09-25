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

import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public abstract class TypedTableLabelProvider<T> extends BaseLabelProvider 
		implements ITableLabelProvider, ITableLabelProvider0<T> {
	
    @SuppressWarnings("unchecked")
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
    	return getColumnImage0((T) element, columnIndex);
    }
    
    @SuppressWarnings("unchecked")
	@Override
	public String getColumnText(Object element, int columnIndex) {
    	return getColumnText0((T) element, columnIndex);
    }
    
}
