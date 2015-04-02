/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.text.completion;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.util.PropertyChangeEvent;

public class ContentAssistantExt extends ContentAssistant {
	
	protected final ContentAssistPreferenceHandler caPrefHelper;
	
	public ContentAssistantExt(ContentAssistPreferenceHandler caPrefHelper) {
		this.caPrefHelper = caPrefHelper;
	}
	
	public void configure(IPreferenceStore prefStore) {
		 caPrefHelper.configure(this, prefStore);
	}
	
	public void handlePrefChange(PropertyChangeEvent event, IPreferenceStore prefStore) {
		caPrefHelper.changeConfiguration(this, prefStore, event);
	}
	
}