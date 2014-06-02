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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.junit.Test;

public class FieldComponent_Test extends CommonTest {
	
	
	@Test
	public void testBasic() throws Exception { testBasic$(); }
	public void testBasic$() throws Exception {
		
		TestsFieldComponent tfc = new TestsFieldComponent();
		FieldListener listener = new FieldListener();
		tfc.addValueChangedListener(listener);
		
		assertTrue(listener.count == 0);
		tfc.setFieldValue("blah");
		assertTrue(listener.count == 1);
		assertTrue(tfc.doUpdateControls_count == 0);
		
		assertTrue(tfc.isCreated() == false);
		
		Shell shell = new Shell(Display.getDefault());
		try {
			tfc.createComponent(shell);
			
			assertTrue(tfc.isCreated() == true);
			assertTrue(tfc.isEnabled() == true);
			
			assertTrue(listener.count == 1);
			assertTrue(tfc.doUpdateControls_count == 1);
			
			tfc.setFieldValue("foo");
			assertTrue(listener.count == 2);
			assertTrue(tfc.doUpdateControls_count == 2);
			
			tfc.text.setText("setText");
			assertEquals(tfc.getFieldValue(), "setText");
			assertTrue(listener.count == 3);
			assertTrue(tfc.doUpdateControls_count == 2);
			
		} finally {
			shell.dispose();
		}
		
		assertTrue(tfc.isCreated() == false);
	}
	
	private final class FieldListener implements IFieldValueListener {
		public int count = 0;
		
		@Override
		public void fieldValueChanged() {
			count++;
		}
	}

	public static class TestsFieldComponent extends AbstractField<String> {
		
		protected Text text;
		protected int doUpdateControls_count = 0;

		@Override
		protected void createContents(Composite topControl) {
			int style = SWT.NONE;
			text = createFieldTextControl(this, topControl, style);
		}
		
		@Override
		protected void doUpdateComponentFromValue() {
			doUpdateControls_count++;
			text.setText(getFieldValue());
		}
		
		@Override
		protected Control getFieldControl() {
			return text;
		}
		
	}
}