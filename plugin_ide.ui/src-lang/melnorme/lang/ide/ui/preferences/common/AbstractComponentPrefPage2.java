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


import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Composite;

import melnorme.lang.tooling.data.IStatusMessage;
import melnorme.lang.tooling.data.IValidationSource;
import melnorme.lang.tooling.data.StatusLevel;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.fields.IDomainField;

public abstract class AbstractComponentPrefPage2 extends AbstractLangPreferencesPage {
	
	private final ArrayList2<IValidationSource> validators = new ArrayList2<>();
	
	public AbstractComponentPrefPage2() {
		super(null);
	}
	
	public AbstractComponentPrefPage2(IPreferenceStore store) {
		super(store);
	}
	
	@Override
	public final void createControl(Composite parent) {
		super.createControl(parent);
		updateStatusMessage();
	}
	
	public void addValidationSource(IValidationSource validationSource) {
		validators.add(validationSource);
	}
	
	public void addValidationStatusField(IDomainField<IStatusMessage> statusField) {
		IValidationSource validationSource;
		
		if(statusField instanceof IValidationSource) {
			 validationSource = (IValidationSource) statusField;
		} else {
			validationSource = new IValidationSource() {
				@Override
				public IStatusMessage getValidationStatus() {
					return statusField.getFieldValue();
				}
			};
		}
		addValidationSource(validationSource);
		statusField.addListener(() -> updateStatusMessage());
	}
	
	protected void updateStatusMessage() {
		if(!isControlCreated()) {
			return;
		}
		
		IStatusMessage status = IValidationSource.getHighestStatus(validators);
		if(status == null) {
			setMessage(null);
			setValid(true);
		} else {
			setMessage(status.getMessage(), statusLevelToMessageType(status.getStatusLevel()));
			setValid(status.getStatusLevel() != StatusLevel.ERROR);
		}
	}
	
	public static int statusLevelToMessageType(StatusLevel statusLevel) {
		switch (statusLevel) {
		case OK: return IMessageProvider.NONE;
		case INFO: return IMessageProvider.INFORMATION;
		case WARNING: return IMessageProvider.WARNING;
		case ERROR: return IMessageProvider.ERROR;
		}
		throw assertFail();
	}
	
}