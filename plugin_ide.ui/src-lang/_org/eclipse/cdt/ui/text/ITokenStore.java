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

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.IToken;

import _org.eclipse.cdt.ui.IPropertyChangeParticipant;
import _org.eclipse.cdt.ui.PreferenceConstants;

/**
 * An ITokenStore manages a set of tokens for a specified set of color property identifiers. Responsibilities include
 * <ul>
 * <li> Reacting to changes to preferences in a specified {@link IPreferenceStore}
 * <li> Managing whether further styles (bold, italic, strikethrough, underline) should be applied
 * <li> Coping with 
 * </ul>
 * 
 * ITokenStore assumes style preferences are stored under the following names
 * <p>
 * Preference color key + {@link PreferenceConstants#EDITOR_BOLD_SUFFIX} are used
 * to retrieve whether the token is rendered in bold.
 * </p>
 * <p>
 * Preference color key + {@link PreferenceConstants#EDITOR_ITALIC_SUFFIX} are used
 * to retrieve whether the token is rendered in italic.
 * </p>
 * <p>
 * Preference color key + {@link PreferenceConstants#EDITOR_STRIKETHROUGH_SUFFIX} are used
 * to retrieve whether the token is rendered in strikethrough.
 * </p>
 * <p>
 * Preference color key + {@link PreferenceConstants#EDITOR_UNDERLINE_SUFFIX} are used
 * to retrieve whether the token is rendered in underline.
 * </p>
 * <p>
 * Clients may implement this interface.
 * </p>
 *
 * @see ITokenStoreFactory
 * @since 5.0
 */
public interface ITokenStore extends IPropertyChangeParticipant {
	/**
	 * Ensures any IToken objects that will be <em>or have been</em> returned are
     * initialized for display.
	 */
	void ensureTokensInitialised();
	
	/**
	 * @param property
	 * @return a token for the specified property. The Token may not be suitable for use if the
	 * current Display is null. Clients should call ITokenStoreFactory#ensureTokensInitialised() at the
	 * point of token use in case this token store was originally initialized before a display was available.
	 */
	IToken getToken(String property);
	
	/**
	 * @return The preference store used to read token styling preferences from.
	 */
	IPreferenceStore getPreferenceStore();
}
