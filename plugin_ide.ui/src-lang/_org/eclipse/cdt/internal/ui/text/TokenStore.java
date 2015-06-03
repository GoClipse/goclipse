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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import _org.eclipse.cdt.ui.PreferenceConstants;
import _org.eclipse.cdt.ui.text.IColorManager;
import _org.eclipse.cdt.ui.text.ITokenStore;


/**
 * Maintains a pool of tokens identified by a String ID.
 * Supports styles per token.
 * Updates token instances on property changes.
 */

public class TokenStore implements ITokenStore {
	
	private final IColorManager fColorManager;
	private final IPreferenceStore fPreferenceStore;

	private Map<String, IToken> fTokenMap= new HashMap<String, IToken>();
	private String[] fPropertyNamesColor;

	private boolean fNeedsLazyColorLoading;
	
	/**
	 * Maintains tokens for a specified set of property keys. Automatically also
	 * copes with bold, italic, strike-through and underlined versions of the key.
	 * @param manager the IColorManager to retrieve token colors from
	 * @param store the {@link IPreferenceStore} to fetch property preferences from
	 */
	public TokenStore(IColorManager manager, IPreferenceStore store, String[] propertyNamesColor) {
		fColorManager= assertNotNull(manager);
		fPreferenceStore= assertNotNull(store);
	
		fPropertyNamesColor= propertyNamesColor;

		fNeedsLazyColorLoading= Display.getCurrent() == null;
		for (int i= 0; i < fPropertyNamesColor.length; i++) {			
			if(fPropertyNamesColor[i].endsWith(PreferenceConstants.EDITOR_BOLD_SUFFIX)
					|| fPropertyNamesColor[i].endsWith(PreferenceConstants.EDITOR_ITALIC_SUFFIX)
					|| fPropertyNamesColor[i].endsWith(PreferenceConstants.EDITOR_STRIKETHROUGH_SUFFIX)
					|| fPropertyNamesColor[i].endsWith(PreferenceConstants.EDITOR_UNDERLINE_SUFFIX)) {
				throw new IllegalArgumentException(); // XXX
			}
			if (fNeedsLazyColorLoading)
				addTokenWithProxyAttribute(fPropertyNamesColor[i]);
			else
				addToken(fPropertyNamesColor[i]);
		}
	}
	
	/**
	 * In the case where at the time of IToken construction, the Display was not
	 * ready to construct colors. 
	 */
	@Override
	public void ensureTokensInitialised() {
		if (fNeedsLazyColorLoading && Display.getCurrent() != null) {
			for (int i= 0; i < fPropertyNamesColor.length; i++) {
				addToken(fPropertyNamesColor[i]);
			}
			fNeedsLazyColorLoading= false;
		}
	}

	private void addTokenWithProxyAttribute(String colorKey) {
		fTokenMap.put(colorKey, new Token(createTextAttribute(colorKey, true)));
	}

	private void addToken(String colorKey) {
		assertNotNull(colorKey);
		//if (fColorManager.getColor(colorKey) == null) {
		// Make sure color manager is updated
		if (true) {
			RGB rgb= PreferenceConverter.getColor(fPreferenceStore, colorKey);
			fColorManager.unbindColor(colorKey);
			fColorManager.bindColor(colorKey, rgb);
		}
		
		if (!fNeedsLazyColorLoading)
			fTokenMap.put(colorKey, new Token(createTextAttribute(colorKey, false)));
		else {
			Token token= ((Token)fTokenMap.get(colorKey));
			if (token != null)
				token.setData(createTextAttribute(colorKey, false));
		}
	}

	/**
	 * Create a text attribute based on the given color, bold, italic, strikethrough and underline preference keys.
	 *
	 * @param colorKey the color preference key
	 * @param boldKey the bold preference key
	 * @param italicKey the italic preference key
	 * @param strikethroughKey the strikethrough preference key
	 * @param underlineKey the italic preference key
	 * @return the created text attribute
	 * @since 3.0
	 */
	private TextAttribute createTextAttribute(String colorKey, boolean isNull) {
		Color color= null;
		if (!isNull)
			color= fColorManager.getColor(colorKey);

		String boldKey= colorKey + PreferenceConstants.EDITOR_BOLD_SUFFIX;
		String italicKey= colorKey + PreferenceConstants.EDITOR_ITALIC_SUFFIX;
		String strikethroughKey= colorKey + PreferenceConstants.EDITOR_STRIKETHROUGH_SUFFIX;
		String underlineKey= colorKey + PreferenceConstants.EDITOR_UNDERLINE_SUFFIX;
		
		int style= fPreferenceStore.getBoolean(boldKey) ? SWT.BOLD : SWT.NORMAL;
		if (fPreferenceStore.getBoolean(italicKey))
			style |= SWT.ITALIC;

		if (fPreferenceStore.getBoolean(strikethroughKey))
			style |= TextAttribute.STRIKETHROUGH;

		if (fPreferenceStore.getBoolean(underlineKey))
			style |= TextAttribute.UNDERLINE;

		return new TextAttribute(color, null, style);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.cdt.ui.text.ITokenStore#getToken(java.lang.String)
	 */
	@Override
	public IToken getToken(String key) {
		return getTokenInner(key);
	}
	
	private Token getTokenInner(String key) {
		ensureTokensInitialised();
		return (Token) fTokenMap.get(key);
	}

	private int indexOf(String property) {
		if (property != null) {
			int length= fPropertyNamesColor.length;
			for (int i= 0; i < length; i++) {
				if (property.startsWith(fPropertyNamesColor[i])) {
					int pLength= property.length();
					if(fPropertyNamesColor[i].length() < pLength) {
						String end= property.substring(fPropertyNamesColor[i].length());
						if (end.equals(PreferenceConstants.EDITOR_BOLD_SUFFIX)
							|| end.equals(PreferenceConstants.EDITOR_ITALIC_SUFFIX)
							|| end.equals(PreferenceConstants.EDITOR_STRIKETHROUGH_SUFFIX)
							|| end.equals(PreferenceConstants.EDITOR_UNDERLINE_SUFFIX)) {
							return i;						
						}
					} else if (fPropertyNamesColor[i].equals(property)) {
						return i;
					}
				}
			}
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.cdt.ui.IPropertyChangeParticipant#affectsBehavior(org.eclipse.jface.util.PropertyChangeEvent)
	 */
	@Override
	public boolean affectsBehavior(PropertyChangeEvent event) {
		return indexOf(event.getProperty()) >= 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.cdt.ui.IPropertyChangeParticipant#adaptToPreferenceChange(org.eclipse.jface.util.PropertyChangeEvent)
	 */
	@Override
	public void adaptToPreferenceChange(PropertyChangeEvent event) {
		String property= event.getProperty();
		int i= indexOf(property);
		if (property.startsWith(fPropertyNamesColor[i])) {
			Token token= getTokenInner(fPropertyNamesColor[i]);
			if(property.length() == fPropertyNamesColor[i].length()) {
				adaptToColorChange(token, event);
			} else {
				String end= property.substring(fPropertyNamesColor[i].length());				
				if (end.equals(PreferenceConstants.EDITOR_BOLD_SUFFIX)) {
					adaptToStyleChange(token, event, SWT.BOLD);
				} else if (end.equals(PreferenceConstants.EDITOR_ITALIC_SUFFIX)) {
					adaptToStyleChange(token, event, SWT.ITALIC);
				} else if (end.equals(PreferenceConstants.EDITOR_STRIKETHROUGH_SUFFIX)) {
					adaptToStyleChange(token, event, TextAttribute.STRIKETHROUGH);
				} else if (end.equals(PreferenceConstants.EDITOR_UNDERLINE_SUFFIX)) {
					adaptToStyleChange(token, event, TextAttribute.UNDERLINE);
				}
			}
		}
	}

	private void adaptToColorChange(Token token, PropertyChangeEvent event) {
		RGB rgb= null;

		Object value= event.getNewValue();
		if (value instanceof RGB)
			rgb= (RGB) value;
		else if (value instanceof String)
			rgb= StringConverter.asRGB((String) value);

		if (rgb != null) {

			String property= event.getProperty();
			Color color= fColorManager.getColor(property);

			if ((color == null || !rgb.equals(color.getRGB()))) {
				fColorManager.unbindColor(property);
				fColorManager.bindColor(property, rgb);

				color= fColorManager.getColor(property);
			}

			Object data= token.getData();
			if (data instanceof TextAttribute) {
				TextAttribute oldAttr= (TextAttribute) data;
				token.setData(new TextAttribute(color, oldAttr.getBackground(), oldAttr.getStyle()));
			}
		}
	}

	protected void adaptToStyleChange(Token token, PropertyChangeEvent event, int styleAttribute) {
		boolean eventValue= false;
		Object value= event.getNewValue();
		if (value instanceof Boolean)
			eventValue= ((Boolean) value).booleanValue();
		else if (IPreferenceStore.TRUE.equals(value))
			eventValue= true;

		Object data= token.getData();
		if (data instanceof TextAttribute) {
			TextAttribute oldAttr= (TextAttribute) data;
			boolean activeValue= (oldAttr.getStyle() & styleAttribute) == styleAttribute;
			if (activeValue != eventValue)
				token.setData(new TextAttribute(oldAttr.getForeground(), oldAttr.getBackground(), eventValue ? oldAttr.getStyle() | styleAttribute : oldAttr.getStyle() & ~styleAttribute));
		}
	}
	
	/**
	 * Returns the preference store.
	 *
	 * @return the preference store.
	 *
	 * @since 3.0
	 */
	@Override
	public IPreferenceStore getPreferenceStore() {
		return fPreferenceStore;
	}
}
