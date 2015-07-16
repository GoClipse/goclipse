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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;

import melnorme.util.swt.components.AbstractFieldComponentTest;
import melnorme.utilbox.tests.CommonTest;

public abstract class FieldComponent_Tests extends CommonTest {
	
	public static class TextFieldTest extends AbstractFieldComponentTest {
		
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
			valueChangeCount_expected = valueChangeCount;
			controlsUpdateCount_expected = controlsUpdateCount;
			// test non-identical value update
			field.setFieldValue("1234567");
			assertEquals(field.getFieldValue(), "12345");
			
			valueChangeCount_expected++; controlsUpdateCount_expected++;
			__checkUpdatesInvariant();
		}
		
	}
	
	public static class TextField_ExtTest extends TextFieldTest {
		
		@Override
		public TextField createField() {
			return field = new TextField("") {
				
				@Override
				protected void doSetFieldValue(String value) {
					if(value.contains("XXX")) {
						return; // Cancel field update. Controls remain unused
					}
					super.doSetFieldValue(value);
				}
				
				@Override
				protected void doUpdateComponentFromValue() {
					controlsUpdateCount++;
					super.doUpdateComponentFromValue();
				};
			};
			
		}
		
		@Override
		protected void doRunTest(Shell shell) {
			field.setFieldValue("aaa");
			assertEquals(field.getFieldValue(), "aaa");
			field.setFieldValue("aXXXa");
			assertEquals(field.getFieldValue(), "aaa");
			
			field.createComponent(shell);
			
			field.setFieldValue("aXXXa");
			assertEquals(field.getFieldValue(), "aaa");
			assertEquals(field.getFieldControl().getText(), "aaa");
			
			field.getFieldControl().setText("aXXXa");
			assertEquals(field.getFieldValue(), "aaa");
			assertEquals(field.getFieldControl().getText(), "aXXXa");
		}
		
		@Override
		protected void runTestWithCreatedComponent() {
			
		}
		
	}
	
	public static class SpinnerNumberFieldTest extends AbstractFieldComponentTest {
		
		protected SpinnerNumberField field;
		
		@Override
		public SpinnerNumberField createField() {
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
			return field.getSpinner().getSelection();
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
			field.getSpinner().setSelection(30);
		}
		
		@Override
		protected void runTestWithCreatedComponent_extra() {
			field.setValueMinimum(100);
//			assertTrue(field.getFieldValue() == 100);
		}
		
	}
	
	public static class TextField2Test extends AbstractFieldComponentTest {
		
		protected TextField2 field;
		
		@Override
		public TextField2 createField() {
			return field = new TextField2("blah", 20) {
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
	
	public static class CheckBoxFieldTest extends AbstractFieldComponentTest {
		
		protected CheckBoxField field;
		
		@Override
		public CheckBoxField createField() {
			return field = new CheckBoxField("blah") {
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
	
	public static class ComboBoxFieldTest extends AbstractFieldComponentTest {
		
		protected ComboBoxField field;
		
		protected static String[] VALUES = array(":0", ":1", ":2");
		protected static String[] LABELS = array("zero", "one", "two");
		
		@Override
		public ComboBoxField createField() {
			return field = new ComboBoxField("blah", LABELS, VALUES) {
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
		
		@Override
		protected void runTestWithCreatedComponent_extra() {
			field.setFieldValue(0);
			assertEquals(field.getFieldStringValue(), ":0");
			field.setFieldStringValue(":2");
			assertEquals(field.getFieldValue(), 2);
			
			field.setFieldValue(-1);
			assertEquals(field.getFieldValue(), -1);
			assertEquals(field.getFieldStringValue(), "");
		}
		
	}
	
	
	public static class RadioSelectionFieldTest extends AbstractFieldComponentTest {
		
		protected RadioSelectionField<Values> field;
		
		protected static enum Values {
			ZERO("0"),
			ONE("One"),
			TWO("Two"),
			THREE("3");
			
			private String toString;
			
			Values(String toString) {
				this.toString = toString;
			}
			
			@Override
			public String toString() {
				return toString;
			}
			
		}
		
		@Override
		public RadioSelectionField<Values> createField() {
			return field = new RadioSelectionField<Values>(Values.values()) {
				@Override
				protected void doUpdateComponentFromValue() {
					controlsUpdateCount++;
					super.doUpdateComponentFromValue();
				}
			};
		}
		
		@Override
		public void setFirstFieldValue() {
			field.setFieldValue(Values.TWO);
		}
		
		@Override
		public void setSecondFieldValue() {
			field.setFieldValue(Values.ZERO);
		}
		
		@Override
		public void doChangeFromControl() {
			Button[] radioControls = field.getRadioControls();
			
			radioControls[field.getSelectionIndex()].setSelection(false);
			radioControls[1].setSelection(true);
			for (Button button : radioControls) {
				button.notifyListeners(SWT.Selection, null);
			}
		}
		
		@Override
		protected Object getValueFromControl() {
			return field.getValueFromControl();
		}
		
		@Override
		protected void runTestWithCreatedComponent_extra() {
			assertTrue(field.getFieldValue() == Values.ZERO);
			checkSelectionCount(1);
			
			field.setFieldValue(Values.TWO);
			assertTrue(field.getFieldValue() == Values.TWO);
			checkSelectionCount(1);
			
			field.setFieldValue(null);
			assertEquals(field.getFieldValue(), Values.ZERO);
			checkSelectionCount(1);
		}
		
		protected void checkSelectionCount(int selectionCount) {
			int count = 0;
			for (Button button : field.getRadioControls()) {
				if(button.getSelection()) {
					count++;
				}
			}
			assertTrue(count == selectionCount);
		}
		
	}
	
	public static class ColorFieldTest extends AbstractFieldComponentTest {
		
		protected ColorField field;
		
		@Override
		public ColorField createField() {
			return field = new ColorField("blah") {
				@Override
				protected void doUpdateComponentFromValue() {
					controlsUpdateCount++;
					super.doUpdateComponentFromValue();
				}
			};
		}
		
		@Override
		protected Object getValueFromControl() {
			return field.getColorSelector().getColorValue();
		}
		
		@Override
		public void setFirstFieldValue() {
			field.setFieldValue(new RGB(1, 1, 1));
		}
		
		@Override
		public void setSecondFieldValue() {
			field.setFieldValue(new RGB(2, 2, 2));
		}
		
		@Override
		public void doChangeFromControl() {
			field.getColorSelector().setColorValue(new RGB(3, 3, 3));
		}
		
	}
	
}