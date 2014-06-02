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
import melnorme.utilbox.tests.CommonTest;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Test;

public abstract class AbstractFieldTest extends CommonTest {
	
	public class FieldListener implements IFieldValueListener {
		@Override
		public void fieldValueChanged() {
			valueChangeCount++;
		}
	}
	
	@Test
	public void runTest() throws Exception {
		doRunTest();
	}
	
	protected int controlsUpdateCount;
	protected int controlsUpdateCount_expected;
	protected int valueChangeCount;
	protected int valueChangeCount_expected;
	private AbstractField<?> field;
	
	protected void __checkUpdatesInvariant() {
		assertTrue(valueChangeCount == valueChangeCount_expected);
		assertTrue(controlsUpdateCount == controlsUpdateCount_expected);
	}
	
	public void doRunTest() throws Exception {
		controlsUpdateCount = 0;
		controlsUpdateCount_expected = 0;
		valueChangeCount = 0;
		valueChangeCount_expected = 0;
		
		field = createField();
		assertTrue(field.isCreated() == false);
		assertTrue(field.getFieldValue() != null);
		
		field.addValueChangedListener(new IFieldValueListener() {
			@Override
			public void fieldValueChanged() {
				valueChangeCount++;
			}
		});
		__checkUpdatesInvariant();
		
		setFirstFieldValue(); 
		valueChangeCount_expected++;
		__checkUpdatesInvariant();
		
		Shell shell = new Shell(Display.getDefault());
		try {
			field.createComponent(shell);
			runTestWithCreatedComponent();
		} finally {
			shell.dispose();
		}
		
		assertTrue(field.isCreated() == false);
	}
	
	protected void runTestWithCreatedComponent() {
		assertTrue(field.getFieldControl() != null);
		assertTrue(field.isCreated() == true);
		controlsUpdateCount_expected++;
		__checkUpdatesInvariant();
		
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
		assertEquals(field.getFieldValue(), field.getDefaultFieldValue());
		valueChangeCount_expected++;
		controlsUpdateCount_expected++;
		__checkUpdatesInvariant();
		
		runTestWithCreatedComponent_extra();
	}

	protected abstract void setFirstFieldValue();
	
	protected abstract void setSecondFieldValue();
	
	protected abstract void doChangeFromControl();
	
	protected abstract Object getValueFromControl();
	
	protected abstract AbstractField<?> createField();
	
	protected void runTestWithCreatedComponent_extra() {
	}
	
}