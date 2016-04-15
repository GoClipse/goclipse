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

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;

public abstract class AbstractCompositeWidget extends AbstractDisableableWidget {
	
	private final ArrayList2<AbstractDisableableWidget> subComponents = new ArrayList2<>();
	
	protected boolean createInlined = true;
	protected GridDataFactory defaultRowLayout;
	
	public AbstractCompositeWidget(boolean createInlined) {
		this.createInlined = createInlined;
		this.defaultRowLayout = gdFillDefaults().grab(true, false).hint(200, SWT.DEFAULT);
	}
	
	protected void addSubComponents(AbstractDisableableWidget... subComponents) {
		for (AbstractDisableableWidget subComponent : subComponents) {
			addSubComponent(subComponent);
		}
	}
	
	protected <T extends AbstractDisableableWidget> T addSubComponent(T subComponent) {
		assertNotNull(subComponent);
		validation.addStatusField(true, subComponent.getStatusField());
		subComponent.setParent(this);
		subComponents.add(subComponent);
		return subComponent;
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
				createSubComponent(topControl, subComponent);
			});
		}
		
	}
	
	protected void createSubComponent(Composite topControl, IDisableableWidget subComponent) {
		subComponent.createComponent(topControl, getLayoutData(subComponent));
	}
	
	protected GridData getLayoutData(@SuppressWarnings("unused") IDisableableWidget subComponent) {
		return createSubComponentDefaultGridData();
	}
	
	protected GridData createSubComponentDefaultGridData() {
		return defaultRowLayout.create();
	}
	
	@Override
	protected void _verifyContract_IDisableableComponent() {
		// No need to check, only possible children are AbstractComponentExt
	}
	
	@Override
	protected final void doSetEnabled(boolean enabled) {
		subComponents.forEach(subComponent -> {
			subComponent.updateControlEnablement2();
		});
	}
	
	@Override
	protected void doUpdateWidgetFromInput() {
		subComponents.forEach(subComponent -> {
			subComponent.updateWidgetFromInput();
		});
	}
	
}