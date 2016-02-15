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
package melnorme.lang.ide.ui.preferences;


import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import melnorme.lang.ide.ui.preferences.common.PreferencesPageContext;
import melnorme.util.swt.components.AbstractDisableableWidget;
import melnorme.util.swt.components.IDisableableWidget;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;

/**
 * Replicates some functionality from {@link AbstractDisableableWidget} 
 */
public abstract class AbstractCompositePreferencesBlock extends AbstractPreferencesBlockExt {
	
	protected final ArrayList2<IDisableableWidget> subComponents = new ArrayList2<>();
	
	public AbstractCompositePreferencesBlock(PreferencesPageContext prefContext) {
		super(prefContext);
	}
	
	protected Indexable<IDisableableWidget> getSubWidgets() {
		return subComponents;
	}
	
	protected void addSubComponent(AbstractDisableableWidget subComponent) {
		validation.addValidatableField(true, subComponent.getStatusField());
		subComponents.add(subComponent);
	}
	
	/* -----------------  ----------------- */
	
	@Override
	protected final void createContents(Composite topControl) {
		getSubWidgets().forEach(subComponent -> {
			subComponent.createComponent(topControl, createSubComponentDefaultGridData());
		});
	}
	
	protected GridData createSubComponentDefaultGridData() {
		return gdFillDefaults().grab(true, false).create();
	}
	
	protected boolean enabled = true;
	
	@Override
	public final void setEnabled(boolean enabled) {
		this.enabled = enabled;
		updateControlEnablement();
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	@Override
	protected void _verifyContract_IDisableableComponent() {
		// No need to check, only possible children are AbstractComponentExt
	}
	
	@Override
	public final void updateComponentFromInput() {
		doUpdateComponentFromInput();
		updateControlEnablement();
	}
	
	protected void doUpdateComponentFromInput() {
	}
	
	protected void updateControlEnablement() {
		doSetEnabled(isEnabled());
	}
	
	protected final void doSetEnabled(boolean enabled) {
		getSubWidgets().forEach(subComponent -> {
			subComponent.setEnabled(enabled);
		});
	}
	
}