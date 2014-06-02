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
package melnorme.util.swt.components;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * A limited for of a field component.
 * Setting and getting the value only work *after* the field has been created.
 * BM: consider deprecating and replacing with {@link AbstractField} which does not have that limitation
 */
public abstract class WidgetFieldComponent<VALUE> extends CommonFieldComponent<VALUE> {
	
	@Override
	public void updateComponentFromInput() {
		// Do nothing, by default, no notion of input
	}
	
	/* ----------------- helper methods ----------------- */
	
	protected Text createFieldText(Composite parent, int style) {
		Text fieldText = new Text(parent, style);
		fieldText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent evt) {
				fireFieldValueChanged();
			}
		});
		return fieldText;
	}
	
}