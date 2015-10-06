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

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

import melnorme.util.swt.SWTUtil;
import melnorme.util.swt.jface.text.ColorManager2;
import melnorme.utilbox.collections.HashMap2;
import melnorme.utilbox.ownership.IDisposable;
import melnorme.utilbox.ownership.OwnedArraylist;

public class TokenRegistry implements IDisposable {
	
	protected final HashMap2<String, Token> tokens = new HashMap2<>();
	
	protected final ColorManager2 colorManager;
	protected final OwnedArraylist owned = new OwnedArraylist();
	
	public TokenRegistry(ColorManager2 colorManager) {
		this.colorManager = assertNotNull(colorManager);
	}
	
	@Override
	public void dispose() {
		owned.disposeAll();
	}
	
	/* -----------------  ----------------- */
	
	public IToken getToken(ITextStylingPref stylingPref) {
		return doGetToken(stylingPref);
	}
	
	protected Token doGetToken(ITextStylingPref stylingPref) {
		assertNotNull(stylingPref);
		assertTrue(SWTUtil.isUIThread());
		
		String key = stylingPref.getKey();
		
		Token token = tokens.get(key);
		if(token == null) {
			Token newToken = new Token(createTextAttribute(stylingPref));
			
			stylingPref.addOwnedListener(owned, () -> updateToken(newToken, stylingPref));
			
			tokens.put(key, newToken);
			token = newToken;
		}
		
		return token;
	}
	
	protected TextAttribute createTextAttribute(ITextStylingPref stylingPref) {
		TextStyling textStyle = stylingPref.getFieldValue();
		
		String registryKey = TokenRegistry.class.getSimpleName() + "/" + stylingPref.getKey();
		
		return textStyle.getTextAttribute(registryKey, colorManager);
	}
	
	
	protected void updateToken(Token token, ITextStylingPref stylingPref) {
		token.setData(createTextAttribute(stylingPref));
		
		handleTokenModified(token);
	}
	
	@SuppressWarnings("unused")
	protected void handleTokenModified(Token token) {
	}
	
}