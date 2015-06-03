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
package melnorme.lang.ide.ui.text.coloring;

import org.eclipse.jface.text.rules.ITokenScanner;

import _org.eclipse.cdt.ui.IPropertyChangeParticipant;

/**
 * Interface for CDT Scanners. Scanners used in CDT must additionally be
 * IPropertyChangeParticipant's.
 * <p>
 * Clients may implement this interface.
 * </p>
 *
 * @since 5.0
 */
public interface ILangTokenScanner extends ITokenScanner, IPropertyChangeParticipant {
}
