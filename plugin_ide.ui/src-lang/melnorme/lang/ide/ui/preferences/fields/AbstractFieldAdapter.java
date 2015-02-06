/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.preferences.fields;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import melnorme.lang.ide.ui.preferences.IPreferencesComponent;
import melnorme.util.swt.components.AbstractComponent;
import melnorme.util.swt.components.AbstractField;

import org.eclipse.swt.widgets.Composite;

public abstract class AbstractFieldAdapter<VALUE> extends AbstractComponent implements IPreferencesComponent {
	
	protected final String prefKey;
	protected final AbstractField<VALUE> field;
	
	public AbstractFieldAdapter(String prefKey, AbstractField<VALUE> field) {
		this.prefKey = assertNotNull(prefKey);
		this.field = assertNotNull(field);
	}
	
	@Override
	public int getPreferredLayoutColumns() {
		return field.getPreferredLayoutColumns();
	}
	
	@Override
	protected final void createContents(Composite topControl) {
		field.createComponentInlined(topControl);
	}
	
	public AbstractField<VALUE> getField() {
		return field;
	}
	
}