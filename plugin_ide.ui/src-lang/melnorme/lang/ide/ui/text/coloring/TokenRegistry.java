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
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import static melnorme.utilbox.core.CoreUtil.list;

import java.util.Map.Entry;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.util.PropertyChangeEvent;

import melnorme.util.swt.SWTUtil;
import melnorme.util.swt.jface.text.ColorManager2;
import melnorme.utilbox.collections.HashMap2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.misc.Pair;

public class TokenRegistry {
	
	protected final HashMap2<String, Pair<ColoringItemPreference, Token>> tokens = new HashMap2<>();
	
	protected final IPreferenceStore prefStore;
	protected final ColorManager2 colorManager;
	
	public TokenRegistry(IPreferenceStore preferenceSore, ColorManager2 colorManager) {
		this.colorManager = assertNotNull(colorManager);
		this.prefStore= assertNotNull(preferenceSore);
	}
	
	public IToken getToken(ColoringItemPreference coloringItem) {
		return doGetToken(coloringItem);
	}
	
	protected Token doGetToken(ColoringItemPreference coloringPref) {
		assertNotNull(coloringPref);
		assertTrue(SWTUtil.isUIThread());
		
		String key = coloringPref.key;
		
		Pair<ColoringItemPreference,Token> pair = tokens.get(key);
		Token token;
		if(pair == null) {
			token = new Token(createTextAttribute(coloringPref));
			tokens.put(key, Pair.create(coloringPref, token));
		} else {
			token = pair.getSecond();
		}
		
		if(token.getData() == null) {
			token.setData(createTextAttribute(coloringPref));
		}
		return token;
	}
	
	protected TextAttribute createTextAttribute(ColoringItemPreference stylingPref) {
		TextStyling textStyle = stylingPref.getFrom(prefStore);
		
		String registryKey = TokenRegistry.class.getSimpleName() + "/" + stylingPref.key;
		
		return textStyle.getTextAttribute(registryKey, colorManager);
	}
	
	/* -----------------  ----------------- */
	
	protected static final Indexable<String> suffixes = list(
		TextColoringConstants.EDITOR_BOLD_SUFFIX,
		TextColoringConstants.EDITOR_ITALIC_SUFFIX,
		TextColoringConstants.EDITOR_STRIKETHROUGH_SUFFIX,
		TextColoringConstants.EDITOR_UNDERLINE_SUFFIX,
		TextColoringConstants.EDITOR_ENABLED_SUFFIX
	);
	
	protected ColoringItemPreference getRelatedColorKey(String property) {
		assertNotNull(property);
		
		if(!SWTUtil.isUIThread()) {
			// This shouldn't even happen, but, let's not assert, some third party plugin could be doing weird stuff.
			return null;
		}
		
		for(Entry<String, Pair<ColoringItemPreference, Token>> entry : tokens.entrySet()) {
			String colorKey = entry.getKey();
			if(property.startsWith(colorKey)) {
				String suffix = property.substring(colorKey.length());
				if(suffixes.contains(suffix)) {
					return entry.getValue().getFirst();
				}
			}
		}
		
		return null;
	}
	
	public boolean handlePreferenceChange(PropertyChangeEvent event) {
		ColoringItemPreference coloringPref = getRelatedColorKey(event.getProperty());
		
		if(coloringPref == null) {
			return false;
		}
		
		Token token = doGetToken(coloringPref);
		token.setData(createTextAttribute(coloringPref));
		
		return true;
	}
	
}