/*******************************************************************************
 *  Copyright (c) 2000, 2009 IBM Corporation and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 * 
 *  Contributors:
 *     IBM Corporation - initial API and implementation
 *     QNX Software System
 *******************************************************************************/
package org.eclipse.cdt.ui.text;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;



/**
 * Manages SWT color objects for the given color keys and
 * given <code>RGB</code> objects. Until the <code>dispose</code>
 * method is called, the same color object is returned for
 * equal keys and equal <code>RGB</code> values.
 *
 * <p>
 * This interface may be implemented by clients.
 * </p>
 *
 * @see org.eclipse.cdt.ui.text.ICColorConstants
 * 
 * @since 5.1
 */
public interface IColorManager extends org.eclipse.jdt.ui.text.IColorManagerExtension {
	
	/**
	 * Returns a color object for the given key. The color objects 
	 * are remembered internally; the same color object is returned 
	 * for equal keys.
	 *
	 * @param key the color key
	 * @return the color object for the given key
	 */
	Color getColor(String key);
	
	/**
	 * Returns the color object for the value represented by the given
	 * <code>RGB</code> object.
	 *
	 * @param rgb the rgb color specification
	 * @return the color object for the given rgb value
	 */
	Color getColor(RGB rgb);	
	
	/**
	 * Disposes all color objects remembered by this color manager.
	 */
	void dispose();

	/**
	 * Remembers the given color specification under the given key.
	 *
	 * @param key the color key
	 * @param rgb the color specification
	 * @throws UnsupportedOperationException if there is already a
	 * 	color specification remembered under the given key
	 */
	@Override
	void bindColor(String key, RGB rgb);
	
	
	/**
	 * Forgets the color specification remembered under the given key.
	 * @param key the color key
	 */
	@Override
	void unbindColor(String key);
	
}