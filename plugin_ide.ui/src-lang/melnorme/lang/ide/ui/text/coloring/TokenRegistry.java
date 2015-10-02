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
package melnorme.lang.ide.ui.text.coloring;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

import melnorme.util.swt.jface.text.ColorManager2;
import melnorme.utilbox.collections.HashMap2;

public class TokenRegistry {
	
	protected final HashMap2<String, Token> tokens = new HashMap2<>();
	
	protected final IPreferenceStore prefStore;
	protected final ColorManager2 colorManager;
	
	public TokenRegistry(IPreferenceStore preferenceSore, ColorManager2 colorManager) {
		this.colorManager = assertNotNull(colorManager);
		this.prefStore= assertNotNull(preferenceSore);
	}
	
	public IPreferenceStore getPreferenceStore() {
		return prefStore;
	}
	
	public IToken getToken(ColoringItemPreference colorItem) {
		return doGetToken(colorItem.key);
	}
	
	protected Token doGetToken(String key) {
		assertNotNull(key);
		Token token = tokens.get(key);
		if(token == null) {
			token = new Token(createTextAttribute(key));
			tokens.put(key, token);
		}
		if(token.getData() == null) {
			token.setData(createTextAttribute(key));
		}
		return token;
	}
	
	protected TextAttribute createTextAttribute(String colorKey) {
		RGB rgb = PreferenceConverter.getColor(prefStore, colorKey);
		boolean isBold = prefStore.getBoolean(colorKey + TextColoringConstants.EDITOR_BOLD_SUFFIX);
		boolean isItalic = prefStore.getBoolean(colorKey + TextColoringConstants.EDITOR_ITALIC_SUFFIX);
		boolean isStrikethrough = prefStore.getBoolean(colorKey + TextColoringConstants.EDITOR_STRIKETHROUGH_SUFFIX);
		boolean isUnderline = prefStore.getBoolean(colorKey + TextColoringConstants.EDITOR_UNDERLINE_SUFFIX);
		
		return createTextAttribute(colorKey, rgb, isBold, isItalic, isStrikethrough, isUnderline);
	}
	
	protected TextAttribute createTextAttribute(String colorKey, RGB rgb, boolean isBold, boolean isItalic,
			boolean isStrikethrough, boolean isUnderline) {
		String registryKey = TokenRegistry.class.getSimpleName() + "/" + colorKey;
		
		Color color = colorManager.putAndGetColor(registryKey, rgb);
		
		return createTextAttribute(color, isBold, isItalic, isStrikethrough, isUnderline);
	}
	
	public static TextAttribute createTextAttribute(Color color, boolean isBold, boolean isItalic, 
			boolean isStrikethrough, boolean isUnderline) {
		int style = SWT.NORMAL;
		
		style |= isBold ? SWT.BOLD : 0;
		style |= isItalic ? SWT.ITALIC : 0;
		style |= isStrikethrough ? TextAttribute.STRIKETHROUGH : 0;
		style |= isUnderline ? TextAttribute.UNDERLINE : 0;
		
		return new TextAttribute(color, null, style);
	}
	
	/* -----------------  ----------------- */
	
	protected String getRelatedColorKey(String property) {
		// XXX: possible concurrency issues
		assertNotNull(property);
		
		for(String key : tokens.keySet()) {
			if(property.startsWith(key)) {
				return key;
			}
		}
		
		return null;
	}
	
}