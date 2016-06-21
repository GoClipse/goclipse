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
package melnorme.util.swt.components.fields;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import melnorme.util.swt.components.AbstractFieldComponentTest;
import melnorme.util.swt.components.AbstractWidgetTest;
import melnorme.utilbox.misc.ArrayUtil;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.tests.CommonTest;

public abstract class FieldWidget_Tests extends CommonTest {
	
	public static class TextFieldTest extends AbstractFieldComponentTest {
		
		protected TextFieldWidget field;
		
		@Override
		public TextFieldWidget createField() {
			return field = new TextFieldWidget("") {
				@Override
				protected void doUpdateWidgetFromValue() {
					controlsUpdateCount++;
					super.doUpdateWidgetFromValue();
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
	
	public static class TextField_SelectionTest extends AbstractWidgetTest {
		
		@Override
		protected void doRunTest(Shell shell) {
			TextFieldWidget textFieldWidget = new TextFieldWidget("blah");
			textFieldWidget.createComponent(shell);
			
			textFieldWidget.set("1234");
			Text textControl = textFieldWidget.getFieldControl();
			assertTrue(textControl.getCaretPosition() == 0);
			checkSelection(textControl, 0, 0);
			textControl.setSelection(1, 3);
			checkSelection(textControl, 1, 3);
			textFieldWidget.doUpdateWidgetFromInput();
			checkSelection(textControl, 1, 3);
			
			textFieldWidget.set("123");
			checkSelection(textControl, 0, 0);
		}
		private void checkSelection(Text textControl, int start, int end) {
			assertTrue(textControl.getSelection().equals(new Point(start, end)));
		}
	}
	
	public static class TextField_ExtTest extends TextFieldTest {
		
		@Override
		public TextFieldWidget createField() {
			return field = new TextFieldWidget("") {
				
				@Override
				protected void doSetFieldValue(String value) {
					if(value.contains("XXX")) {
						return; // Cancel field update. Controls remain unused
					}
					super.doSetFieldValue(value);
				}
				
				@Override
				protected void doUpdateWidgetFromValue() {
					controlsUpdateCount++;
					super.doUpdateWidgetFromValue();
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
				protected void doUpdateWidgetFromValue() {
					controlsUpdateCount++;
					super.doUpdateWidgetFromValue();
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
		
		protected TextFieldExt field;
		
		@Override
		public TextFieldExt createField() {
			return field = new TextFieldExt("blah", 20) {
				@Override
				protected void doUpdateWidgetFromValue() {
					controlsUpdateCount++;
					super.doUpdateWidgetFromValue();
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
				protected void doUpdateWidgetFromValue() {
					controlsUpdateCount++;
					super.doUpdateWidgetFromValue();
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
				protected void doUpdateWidgetFromValue() {
					controlsUpdateCount++;
					super.doUpdateWidgetFromValue();
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
	
	
	public static enum Values {
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
	
	public static class RadioSelectionFieldTest extends AbstractFieldComponentTest {
		
		protected RadioSelectionField<Values> field;
		
		@Override
		public RadioSelectionField<Values> createField() {
			return field = new RadioSelectionField<Values>(Values.values()) {
				@Override
				protected void doUpdateWidgetFromValue() {
					controlsUpdateCount++;
					super.doUpdateWidgetFromValue();
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
	
	public static class ComboOptionsFieldComponentTest extends AbstractFieldComponentTest {
		
		protected ComboOptionsField field;
		
		@Override
		public ComboOptionsField createField() {
			field = new ComboOptionsField("label") {
				@Override
				protected void doUpdateWidgetFromValue() {
					controlsUpdateCount++;
					super.doUpdateWidgetFromValue();
				}
			};
			field.setFieldOptions(
				ArrayUtil.map(Values.values(), (Values value) -> value.toString(), String.class));
			return field;
		}
		
		@Override
		protected void checkValueIsNotNull() {
			// 
		}
		
		@Override
		protected void doRunTest(Shell shell) {
			testSetComboOptions(shell, false);
			testSetComboOptions(shell, true);
			
			ComboOptionsField comboField = new ComboOptionsField("blah");
			comboField.setFieldOptions(array("1", "2", "3"));
			comboField.setFieldValue("2");
			comboField.createComponent(shell);
			checkComboValues(comboField, "2");
			
			comboField.setFieldOptions(array("abc", "xxx"));
			assertTrue(comboField.getFieldControl().getSelectionIndex() == 0);
			assertTrue(comboField.getFieldControl().getText().equals("abc"));
			
			comboField.setFieldOptions(array());
			assertTrue(comboField.getFieldControl().getSelectionIndex() == -1);
			assertTrue(comboField.getFieldControl().getText().equals(""));
			
			testSetFieldValue(comboField, "InvalidValue", null);
			testSetFieldValue(comboField, null, null);
			
			// -----------
			comboField.setFieldOptions("One", "Two"); 
			
			testSetFieldValue(comboField, "InvalidValue", "One");
			testSetFieldValue(comboField, "One", "One");
			testSetFieldValue(comboField, null, "One");
			
			testSetValueFromControl(comboField, "One", "One");
			testSetValueFromControl(comboField, "Blah", "One");
			
			super.doRunTest(shell);
		}
		
		protected void testSetComboOptions(Composite parent, boolean createComponent) {
			ComboOptionsField comboField = new ComboOptionsField("label");
			if(createComponent) {
				comboField.createComboControl(parent);
			}
			
			assertAreEqual(comboField.getFieldValue(), null);
			comboField.setFieldOptions(array("1", "2", "3"));
			comboField.setFieldValue("2");
			comboField.setFieldOptions(array("1", "xxx", "2", "b"));
			assertAreEqual(comboField.getFieldValue(), "2");
			comboField.setFieldOptions(array("a", "b", "c"));
			assertAreEqual(comboField.getFieldValue(), "a");
		}
		
		public static void testSetValueFromControl(ComboOptionsField comboField, String newValue,
				String expectedFieldValue) {
			setFromControl(comboField.getFieldControl(), newValue);
			checkComboValues(comboField, expectedFieldValue);
		}
		
		public static void testSetFieldValue(ComboOptionsField comboField, String newValue,
				String expectedFieldValue) {
			comboField.setFieldValue(newValue);
			checkComboValues(comboField, expectedFieldValue);
		}
		
		public static void checkComboValues(ComboOptionsField comboField, String expectedFieldValue) {
			assertEquals(expectedFieldValue == null, comboField.getFieldControl().getSelectionIndex() == -1);
			String expectedControl = StringUtil.nullAsEmpty(expectedFieldValue);
			
			assertAreEqual(comboField.getFieldControl().getText(), expectedControl);
			assertAreEqual(comboField.getFieldValue(), expectedFieldValue);
		}
		
		@Override
		public void setFirstFieldValue() {
			field.setFieldValue(Values.TWO.toString());
		}
		
		@Override
		public void setSecondFieldValue() {
			field.setFieldValue(Values.ZERO.toString());
		}
		
		@Override
		public void doChangeFromControl() {
			setFromControl(field.getFieldControl(), field.getFieldValue());
		}
		
		public static void setFromControl(Combo combo, String newText) {
			combo.setText(newText);
			combo.notifyListeners(SWT.Selection, null);
		}
		
		@Override
		protected Object getValueFromControl() {
			return field.combo.getText();
		}
		
	}
	
	public static class ColorFieldTest extends AbstractFieldComponentTest {
		
		protected ColorField field;
		
		@Override
		public ColorField createField() {
			return field = new ColorField("blah") {
				@Override
				protected void doUpdateWidgetFromValue() {
					controlsUpdateCount++;
					super.doUpdateWidgetFromValue();
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