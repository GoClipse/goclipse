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
 *     Andrew Ferguson (Symbian)
 *******************************************************************************/
package melnorme.lang.ide.ui.text.coloring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.util.PropertyChangeEvent;

import _org.eclipse.cdt.ui.IPropertyChangeParticipant;
import _org.eclipse.cdt.ui.text.ITokenStore;

/**
 * Convenience implementation for {@link ILangTokenScanner}.
 * Subclasses need to initialize scanner rules by calling {@link #setRules(IRule[])} or {@link #setRules(List)}.
 * <p>
 * Clients may instantiate and extend this class.
 * </p>
 * 
 * @since 5.1
 */
public abstract class AbstractLangScanner extends BufferedRuleBasedScanner implements ILangTokenScanner {
	private List<IPropertyChangeParticipant> fParticipants;
	final protected ITokenStore fTokenStore;
	
	/**
	 * Create a new scanner for the given token store with default buffer size.
	 * 
	 * @param tokenStore
	 */
	public AbstractLangScanner(ITokenStore tokenStore) {
		fTokenStore= tokenStore;
		fParticipants= new ArrayList<IPropertyChangeParticipant>();
	}
	
	/**
	 * Create a new scanner for the given token store and buffer size.
	 * 
	 * @param tokenStore
	 * @param size
	 */
	public AbstractLangScanner(ITokenStore tokenStore, int size) {
		this(tokenStore);
		setBufferSize(size);
	}
	
	protected void addPropertyChangeParticipant(IPropertyChangeParticipant participant) {
		fParticipants.add(participant);
	}
	
	/**
	 * Convenience method for setting the scanner rules with a list rather than an array.
	 */
	public final void setRules(List<IRule> rules) {
		if(rules==null) {
			setRules((IRule[])null);
		} else {
			IRule[] result= new IRule[rules.size()];
			rules.toArray(result);
			setRules(result);		
		}
	}
	
	@Override
	public IToken nextToken() {
		fTokenStore.ensureTokensInitialised();
		return super.nextToken();
	}
	
	public IToken getToken(String key) {
		return fTokenStore.getToken(key);
	}
	
	public IPreferenceStore getPreferenceStore() {
		return fTokenStore.getPreferenceStore();
	}
	
	@Override
	public void adaptToPreferenceChange(PropertyChangeEvent event) {
		if(fTokenStore.affectsBehavior(event)) {
			fTokenStore.adaptToPreferenceChange(event);
		}
		for (IPropertyChangeParticipant propertyChangeParticipant : fParticipants) {
			propertyChangeParticipant.adaptToPreferenceChange(event);
		}
	}
	
	@Override
	public boolean affectsBehavior(PropertyChangeEvent event) {
		boolean result= fTokenStore.affectsBehavior(event);
		for(Iterator<IPropertyChangeParticipant> i= fParticipants.iterator(); !result && i.hasNext(); ) {
			result |= (i.next()).affectsBehavior(event);
		}
		return result;
	}
	
	/* ----------------- Helpers ----------------- */
	
	public static class LangWhitespaceDetector implements IWhitespaceDetector {
		@Override
		public boolean isWhitespace(char character) {
			return Character.isWhitespace(character);
		}
	}
	
	public static class JavaWordDetector implements IWordDetector {
		
		@Override
		public boolean isWordPart(char character) {
			return Character.isJavaIdentifierPart(character);
		}
		
		@Override
		public boolean isWordStart(char character) {
			return Character.isJavaIdentifierPart(character);
		}
	}
	
}