/*******************************************************************************
 * Copyright (c) 2008 Symbian Software Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Andrew Ferguson (Symbian) - Initial implementation
 *******************************************************************************/
package _org.eclipse.cdt.ui.text;

/**
 * A means of obtaining ITokenStore objects.
 * <p>
 * Clients may implement this interface.
 * </p>
 * @since 5.0
 */
public interface ITokenStoreFactory {
	/**
	 * @param propertyColorNames
	 * @return a token store object initialized with the specified propertyColorNames
	 */
	public ITokenStore createTokenStore(String[] propertyColorNames);
}
