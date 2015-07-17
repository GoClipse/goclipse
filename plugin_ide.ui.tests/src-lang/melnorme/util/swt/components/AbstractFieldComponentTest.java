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
import melnorme.utilbox.fields.IFieldValueListener;
import melnorme.utilbox.tests.CommonTest;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Test;

public abstract class AbstractFieldComponentTest extends CommonTest {
	
	public class FieldListener implements IFieldValueListener {
		@Override
		public void fieldValueChanged() {
			valueChangeCount++;
		}
	}
	
	@Test
	public void runTest() throws Exception {
		runTest_______();
	}
	
	protected int controlsUpdateCount;
	protected int controlsUpdateCount_expected;
	protected int valueChangeCount;
	protected int valueChangeCount_expected;
	private FieldComponent<?> field;
	
	protected void __checkUpdatesInvariant() {
		assertTrue(valueChangeCount == valueChangeCount_expected);
		assertTrue(controlsUpdateCount == controlsUpdateCount_expected);
	}
	
	public void runTest_______() throws Exception {
		controlsUpdateCount_expected = controlsUpdateCount = 0;
		valueChangeCount_expected = valueChangeCount = 0;
		
		field = createField();
		assertTrue(field.isCreated() == false);
		checkValueIsNotNull();
		
		field.addValueChangedListener(new IFieldValueListener() {
			@Override
			public void fieldValueChanged() {
				valueChangeCount++;
			}
		});
		__checkUpdatesInvariant();
		
		Shell shell = new Shell(Display.getDefault());
		try {

		doRunTest(shell);
		
		} finally {
			shell.dispose();
			assertTrue(field.isCreated() == false);
		}	
	}
	
	protected void checkValueIsNotNull() {
		assertTrue(field.getFieldValue() != null);
	}
	
	protected void doRunTest(Shell shell) {
		setFirstFieldValue(); 
		valueChangeCount_expected++;
		__checkUpdatesInvariant();
		
		field.createComponent(shell);
		assertTrue(field.getFieldControl() != null);
		assertTrue(field.isCreated() == true);
		assertTrue(controlsUpdateCount <= controlsUpdateCount_expected + 1);
		assertTrue(valueChangeCount <= valueChangeCount_expected + 1);
		controlsUpdateCount_expected = controlsUpdateCount;
		valueChangeCount_expected = valueChangeCount; 
//		valueChangeCount_expected++; // This could change in the future
		__checkUpdatesInvariant();
		runTestWithCreatedComponent();
	}
	
	protected void runTestWithCreatedComponent() {
		
		setSecondFieldValue();
		assertEquals(field.getFieldValue(), getValueFromControl());
		valueChangeCount_expected++;
		controlsUpdateCount_expected++;
		__checkUpdatesInvariant();
		
		doChangeFromControl();
		assertEquals(field.getFieldValue(), getValueFromControl());
		valueChangeCount_expected++;
		__checkUpdatesInvariant();
		
		field.setFieldValue(null);
//		assertEquals(field.getFieldValue(), field.getDefaultFieldValue());
		valueChangeCount_expected++;
		controlsUpdateCount_expected++;
		__checkUpdatesInvariant();
		
		runTestWithCreatedComponent_extra();
	}

	protected abstract void setFirstFieldValue();
	
	protected abstract void setSecondFieldValue();
	
	protected abstract void doChangeFromControl();
	
	protected abstract Object getValueFromControl();
	
	protected abstract FieldComponent<?> createField();
	
	protected void runTestWithCreatedComponent_extra() {
	}
	
}