/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.preferences.common;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import melnorme.lang.ide.core.utils.prefs.DerivedValuePreference;
import melnorme.lang.ide.core.utils.prefs.IGlobalPreference;
import melnorme.lang.ide.core.utils.prefs.IntPreference;
import melnorme.lang.ide.core.utils.prefs.StringPreference;
import melnorme.lang.tooling.data.CompositeValidatableField;
import melnorme.lang.tooling.data.IStatusMessage;
import melnorme.util.swt.SWTFactory;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.components.AbstractWidget;
import melnorme.util.swt.components.FieldComponent;
import melnorme.util.swt.components.IValidatableWidget;
import melnorme.util.swt.components.fields.ComboBoxField;
import melnorme.util.swt.components.fields.NumberField;
import melnorme.utilbox.fields.IFieldView;
import melnorme.utilbox.fields.IModelField;

public abstract class AbstractPreferencesBlock2 extends AbstractWidget implements IValidatableWidget {
	
	protected final CompositeValidatableField validation = new CompositeValidatableField();
	protected final PreferencesPageContext prefContext;
	
	public AbstractPreferencesBlock2(PreferencesPageContext prefContext) {
		this.prefContext = assertNotNull(prefContext);
	}
	
	@Override
	public IFieldView<IStatusMessage> getStatusField() {
		return validation;
	}
	
	public final void bindToDerivedPreference(IModelField<String> field, DerivedValuePreference<?> pref) {
		prefContext.bindToValidatedPreference(field, pref, validation);
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public void updateComponentFromInput() {
	}
	
	/* -----------------  Control Helpers  ----------------- */
	
	public <T> void createAndBindComponent(Composite parent, IGlobalPreference<T> pref, FieldComponent<T> field) {
		field.createComponentInlined(parent);
		prefContext.bindToPreference(field, pref);
	}
	
	protected void createIntField(Composite parent, IntPreference pref, NumberField field) {
		field.createComponentInlined(parent);
		prefContext.bindToPreference(field.asIntProperty(), pref);
	}
	
	protected void createCheckboxField(Composite parent, StringPreference pref, ComboBoxField field) {
		field.createComponentInlined(parent);
		prefContext.bindToPreference(field.asStringProperty(), pref);
	}
	
	/* -----------------  ----------------- */
	
	public static Composite createSubsection(Composite parent, String label) {
		return SWTFactoryUtil.createGroup(parent, label, 
			createDefaultSubSectionGridData());
	}
	
	public static Composite createSubsection(Composite parent, String label, int numColumns) {
		return createOptionsSection(parent, label, numColumns,
			createDefaultSubSectionGridData());
	}
	
	public static GridData createDefaultSubSectionGridData() {
		return new GridData(SWT.FILL, SWT.CENTER, true, false);
	}
	
	public static Group createOptionsSection(Composite parent, String label, int numColumns, GridData gridData) {
		Group group = SWTFactory.createGroup(parent, label, gridData);
		group.setLayout(createDefaultOptionsSectionLayout(numColumns));
		return group;
	}
	
	public static GridLayout createDefaultOptionsSectionLayout(int numColumns) {
		return GridLayoutFactory.swtDefaults().numColumns(numColumns).create();
	}
	
	protected NumberField createNumberField(String label, int textLimit) {
		NumberField numberField = new NumberField(label, textLimit);
		validation.addValidatableField(false, numberField);
		return numberField;
	}
	
}