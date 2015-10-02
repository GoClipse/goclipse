/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     QNX Software System
 *     Andrew Ferguson (Symbian) - refactored to TokenStore (previously part of AbstractCScanner)
 *******************************************************************************/
package _org.eclipse.cdt.internal.ui.text;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.util.PropertyChangeEvent;

import melnorme.lang.ide.ui.text.coloring.TokenRegistry;
import melnorme.util.swt.jface.text.ColorManager2;


/**
 * Maintains a pool of tokens identified by a String ID.
 * Supports styles per token.
 * Updates token instances on property changes.
 */
public class TokenStore extends TokenRegistry {
	
	public TokenStore(IPreferenceStore preferenceSore, ColorManager2 colorManager) {
		super(preferenceSore, colorManager);
	}
	
	public boolean affectsBehavior(PropertyChangeEvent event) {
		return getRelatedColorKey(event.getProperty()) != null;
	}
	
	public void adaptToPreferenceChange(PropertyChangeEvent event) {
		String property = event.getProperty();
		
		String colorKey = getRelatedColorKey(property);
		
		Token token = doGetToken(colorKey);
		token.setData(createTextAttribute(colorKey));
	}
	
}