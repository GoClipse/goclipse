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
package melnorme.lang.ide.ui.preferences.common;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import melnorme.lang.ide.ui.utils.DialogPageUtils;
import melnorme.lang.tooling.data.IStatusMessage;
import melnorme.util.swt.components.IWidgetComponent;

/**
 * Abstract preference page wrapping {@link IWidgetComponent}.
 */
public abstract class AbstractPreferencesBlockPrefPage extends AbstractLangPreferencesPage {
	
	protected final IPreferencesWidget preferencesBlock;
	
	public AbstractPreferencesBlockPrefPage() {
		super(null);
		
		preferencesBlock = init_createPreferencesBlock();
		
		preferencesBlock.getStatusField().registerListener(true, () -> updateStatusMessage());
	}
	
	protected abstract AbstractPreferencesBlock init_createPreferencesBlock();
	
	@Override
	protected Control createContents(Composite parent) {
		return preferencesBlock.createComponent(parent);
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public final void createControl(Composite parent) {
		super.createControl(parent);
		updateStatusMessage();
	}
	
	protected void updateStatusMessage() {
		DialogPageUtils.setPrefPageStatus(this, getPageStatus());
	}
	
	/** @return page status message. Can be null */
	protected IStatusMessage getPageStatus() {
		return preferencesBlock.getStatusField().getValue();
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public void performDefaults() {
		preferencesBlock.loadDefaults();
	}
	
	@Override
	public boolean performOk() {
		return preferencesBlock.saveSettings();
	}
	
}