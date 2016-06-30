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
package melnorme.lang.ide.ui.templates;

import static melnorme.utilbox.core.CoreUtil.array;

import java.io.IOException;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.EditorSettings_Actual;
import melnorme.lang.ide.ui.LangUIPlugin;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.ui.editors.text.templates.ContributionContextTypeRegistry;
import org.eclipse.ui.editors.text.templates.ContributionTemplateStore;

public class TemplateRegistry {
	
	private static final String TEMPLATES_PREF_KEY = "LangTemplatesStore";
	
	protected ContextTypeRegistry contextTypeRegistry;
	protected TemplateStore store;
	
	public IPreferenceStore getPreferenceStore() {
		return LangUIPlugin.getInstance().getPreferenceStore();
	}
	
	protected String[] getRegisteredContextTypeIds() {
		return array(EditorSettings_Actual.TEMPLATE_CONTEXT_TYPE_ID);
	}
	
	public TemplateStore getTemplateStore() {
		if(store == null) {
			store = new ContributionTemplateStore(getContextTypeRegistry(), getPreferenceStore(), TEMPLATES_PREF_KEY);
			try {
				store.load();
			} catch (IOException e) {
				LangCore.logError("Could not load template store.", e);
			}
		}
		return store;
	}
	
	public ContextTypeRegistry getContextTypeRegistry() {
		if(contextTypeRegistry == null) {
			contextTypeRegistry = createContributionContextTypeRegistry();
		}
		return contextTypeRegistry;
	}
	
	protected ContextTypeRegistry createContributionContextTypeRegistry() {
		final ContributionContextTypeRegistry registry = new ContributionContextTypeRegistry();
		for(String id : getRegisteredContextTypeIds()) {
			registry.addContextType(id);
		}
		return registry;
	}
	
	public TemplateContextType getContextType(String contextTypeId) {
		return getContextTypeRegistry().getContextType(contextTypeId);
	}
	
}