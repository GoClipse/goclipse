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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import melnorme.lang.tooling.data.CompositeValidatableField;
import melnorme.lang.tooling.data.IStatusMessage;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.fields.IFieldView;

public abstract class AbstractCompositeWidget extends AbstractDisableableWidget {
	
	private final ArrayList2<AbstractDisableableWidget> subComponents = new ArrayList2<>();
	protected final CompositeValidatableField validation = new CompositeValidatableField();
	
	protected boolean createInlined = true;
	
	public AbstractCompositeWidget() {
	}
	
	public AbstractCompositeWidget(boolean createInlined) {
		this.createInlined = createInlined;
	}
	
	@Override
	public IFieldView<IStatusMessage> getStatusField() {
		return validation;
	}
	
	protected void addSubComponent(AbstractDisableableWidget subComponent) {
		assertNotNull(subComponent);
		validation.addStatusField(true, subComponent.getStatusField());
		subComponent.setParent(this);
		subComponents.add(subComponent);
	}
	
	protected final Indexable<IDisableableWidget> getSubWidgets() {
		return subComponents.<IDisableableWidget>upcastTypeParameter();
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
	protected void _verifyContract_IDisableableComponent() {
		// No need to check, only possible children are AbstractComponentExt
	}
	
	@Override
	protected final void doSetEnabled(boolean enabled) {
		subComponents.forEach(subComponent -> {
			subComponent.updateControlEnablement();
		});
		
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