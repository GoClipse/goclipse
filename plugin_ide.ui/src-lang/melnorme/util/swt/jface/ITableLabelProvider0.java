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

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public interface ITableLabelProvider0<T> extends IBaseLabelProvider {
	
	/** See {@link ITableLabelProvider#getColumnImage(Object, int)} */
    public Image getColumnImage0(T element, int columnIndex);
    
    /** See {@link ITableLabelProvider#getColumnText(Object, int)} */
    public String getColumnText0(T element, int columnIndex);
    
}