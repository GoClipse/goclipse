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
package melnorme.util.swt.components;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import melnorme.lang.tooling.data.CompositeValidatableField;
import melnorme.lang.tooling.data.IStatusMessage;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.fields.IFieldView;

public abstract class AbstractCompositeWidget extends AbstractDisableableWidget {
	
	private final ArrayList2<IDisableableWidget> subComponents = new ArrayList2<>();
	protected final CompositeValidatableField validation = new CompositeValidatableField();
	
	protected boolean createInlined = true;
	
	public AbstractCompositeWidget() {
	}
	
	@Override
	public IFieldView<IStatusMessage> getStatusField() {
		return validation;
	}
	
	protected void addSubComponent(AbstractDisableableWidget subComponent) {
		validation.addStatusField(true, subComponent.getStatusField());
		subComponents.add(subComponent);
	}
	
	protected final Indexable<IDisableableWidget> getSubWidgets() {
		return subComponents;
	}
	
	@Override
	protected void createContents(Composite topControl) {
		createSubComponents(topControl, createInlined);
	}
	
	protected void createSubComponents(Composite topControl, boolean createInlined) {
		if(createInlined) {
			getSubWidgets().forEach(subComponent -> {
				subComponent.createComponentInlined(topControl);
			});
		} else {
			getSubWidgets().forEach(subComponent -> {
				subComponent.createComponent(topControl, createSubComponentDefaultGridData());
			});
		}
		
	}
	
	protected GridData createSubComponentDefaultGridData() {
		return gdFillDefaults().grab(true, false).create();
	}
	
	@Override
	protected final void doSetEnabled(boolean enabled) {
		getSubWidgets().forEach(subComponent -> {
			subComponent.setEnabled(enabled);
		});
	}
	
	@Override
	protected void _verifyContract_IDisableableComponent() {
		// No need to check, only possible children are AbstractComponentExt
	}
	
	@Override
	protected void updateControlEnablement() {
		super.updateControlEnablement();
		
		updateValidationStatusForEnablement();
	}
	
	protected void updateValidationStatusForEnablement() {
		if(!isEnabled()) {
			validation.setValue(null);
		} else {
			validation.updateFieldValue();
		}
	}
	
}