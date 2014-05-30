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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import static melnorme.utilbox.core.CoreUtil.areEqual;
import melnorme.util.swt.SWTUtil;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public abstract class AbstractField<VALUE> extends CommonFieldComponent<VALUE> {
	
	private VALUE value; // private to prevent direct modifications.
	protected boolean runningUpdateControls;
	
	@Override
	public VALUE getFieldValue() {
		return value;
	}
	
	@Override
	public void setFieldValue(VALUE value) {
		setFieldValue(value, true);
	}
	
	/** Update the field value from a control modification. */
	protected void updateFieldValue(VALUE value) {
		if(runningUpdateControls) {
			assertTrue(areEqual(getFieldValue(), value));
			return; // Field value already up to date
		}
		setFieldValue(value, false);
	}
	
	protected void setFieldValue(VALUE value, boolean needsUpdateControls) {
		this.value = value;
		if(needsUpdateControls) {
			updateControls();
		}
		fireFieldValueChanged();
	}
	
	public void createControls(Composite topControl) {
		createContents(topControl);
		doUpdateControls();
	}
	
	@Override
	protected abstract void createContents(Composite topControl);
	
	public abstract Control getFieldControl();
	
	public boolean isCreated() {
		return SWTUtil.isOkToUse(getFieldControl());
	}
	
	public void setEnabled(boolean enabled) {
		// default implementation, classes might need to override
		getFieldControl().setEnabled(enabled);
	}
	
	public boolean isEnabled() {
		return getFieldControl().isEnabled();
	}
	
	public void updateControls() {
		assertTrue(runningUpdateControls == false);
		if(isCreated()) {
			runningUpdateControls = true;
			try {
				doUpdateControls();
			} finally {
				runningUpdateControls = false;
			}
		}
	}
	
	public abstract void doUpdateControls();
	
}