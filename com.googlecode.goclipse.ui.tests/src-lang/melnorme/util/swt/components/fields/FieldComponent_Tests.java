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
package melnorme.util.swt.components.fields;

import melnorme.lang.ide.ui.preferences.fields.CheckBoxConfigField;
import melnorme.lang.ide.ui.preferences.fields.ComboBoxConfigField;
import melnorme.lang.ide.ui.preferences.fields.TextConfigField;
import melnorme.util.swt.components.AbstractField;
import melnorme.util.swt.components.AbstractFieldTest;
import melnorme.utilbox.tests.CommonTest;

import org.eclipse.swt.SWT;

public abstract class FieldComponent_Tests extends CommonTest {
	
	public static class TextFieldTest extends AbstractFieldTest {
		
		protected TextField field;
		
		@Override
		public TextField createField() {
			return field = new TextField("") {
				@Override
				protected void doUpdateComponentFromValue() {
					controlsUpdateCount++;
					super.doUpdateComponentFromValue();
				};			
			};
		}
		
		@Override
		protected Object getValueFromControl() {
			return field.getFieldControl().getText();
		}
		
		@Override
		public void setFirstFieldValue() {
			field.setFieldValue("blah");
		}
		
		@Override
		public void setSecondFieldValue() {
			field.setFieldValue("foo");
		}
		
		@Override
		public void doChangeFromControl() {
			field.getFieldControl().setText("setText");
		}
		
		@Override
		protected void runTestWithCreatedComponent_extra() {
			field.setFieldValue("blah");
			field.getFieldControl().setTextLimit(5);
			// TODO test non-identical value update
//			field.setFieldValue("1234567");
		}
		
	}
	
	public static class SpinnerNumberFieldTest extends AbstractFieldTest {
		
		protected SpinnerNumberField field;
		
		@Override
		public AbstractField<?> createField() {
			return field = new SpinnerNumberField("blah") {
				@Override
				protected void doUpdateComponentFromValue() {
					controlsUpdateCount++;
					super.doUpdateComponentFromValue();
				};
			};
		}
		
		@Override
		protected Object getValueFromControl() {
			return field.getSpinner().getDigits();
		}
		
		@Override
		public void setFirstFieldValue() {
			field.setFieldValue(10);
		}
		
		@Override
		public void setSecondFieldValue() {
			field.setFieldValue(20);
		}
		
		@Override
		public void doChangeFromControl() {
			field.getSpinner().setDigits(30);
		}
		
		@Override
		protected void runTestWithCreatedComponent_extra() {
			field.setValueMinimum(100);
//			assertTrue(field.getFieldValue() == 100);
		}
		
	}
	
	public static class TextConfigFieldTest extends AbstractFieldTest {
		
		protected TextConfigField field;
		
		@Override
		public AbstractField<?> createField() {
			return field = new TextConfigField("blah", "blah", 20) {
				@Override
				protected void doUpdateComponentFromValue() {
					controlsUpdateCount++;
					super.doUpdateComponentFromValue();
				}
			};
		}
		
		@Override
		protected Object getValueFromControl() {
			return field.getFieldControl().getText();
		}
		
		@Override
		public void setFirstFieldValue() {
			field.setFieldValue("blah");
		}
		
		@Override
		public void setSecondFieldValue() {
			field.setFieldValue("foo");
		}
		
		@Override
		public void doChangeFromControl() {
			field.getFieldControl().setText("setText");
		}
		
	}
	
	public static class CheckboxConfigFieldTest extends AbstractFieldTest {
		
		protected CheckBoxConfigField field;
		
		@Override
		public AbstractField<?> createField() {
			return field = new CheckBoxConfigField("blah", "blah") {
				@Override
				protected void doUpdateComponentFromValue() {
					controlsUpdateCount++;
					super.doUpdateComponentFromValue();
				}
			};
		}
		
		@Override
		protected Object getValueFromControl() {
			return field.getFieldControl().getSelection();
		}
		
		@Override
		public void setFirstFieldValue() {
			field.setFieldValue(true);
		}
		
		@Override
		public void setSecondFieldValue() {
			field.setFieldValue(false);
		}
		
		@Override
		public void doChangeFromControl() {
			field.getFieldControl().setSelection(true);
			field.getFieldControl().notifyListeners(SWT.Selection, null);
		}
		
	}
	
	public static class ComboBoxConfigFieldTest extends AbstractFieldTest {
		
		protected ComboBoxConfigField field;
		
		protected static String[] VALUES = array("1", "2", "3");
		protected static String[] LABELS = array("one", "two", "three");
		
		@Override
		public AbstractField<?> createField() {
			return field = new ComboBoxConfigField("blah", "blah", LABELS, VALUES) {
				@Override
				protected void doUpdateComponentFromValue() {
					controlsUpdateCount++;
					super.doUpdateComponentFromValue();
				}
			};
		}
		
		@Override
		protected Object getValueFromControl() {
			return field.getFieldControl().getSelectionIndex();
		}
		
		@Override
		public void setFirstFieldValue() {
			field.setFieldValue(2);
		}
		
		@Override
		public void setSecondFieldValue() {
			field.setFieldValue(0);
		}
		
		@Override
		public void doChangeFromControl() {
			field.getFieldControl().setText("one");
			field.getFieldControl().notifyListeners(SWT.Selection, null);
		}
		
	}
	
}