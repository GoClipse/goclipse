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
package melnorme.util.swt.components;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import org.eclipse.swt.widgets.Control;

public interface IDisableableWidget extends IWidgetComponent {
	
	/**
	 * Enable or disable the entire widget. Can be set even before the widget is created.
	 */
	void setEnabled(boolean enabled);
	
	default void _IDisableableComponent$verifyContract() {
		DisableableWidgetContractCheckHelper.verify_setEnabled_contract(getClass());
	}
	
}


class DisableableWidgetContractCheckHelper {
	
	public static void verify_setEnabled_contract(Class<?> klass) {
		if(klass == AbstractDisableableWidget.class) {
			return;
		}
		
		boolean needs_setEnabled_MethodOverride = 
				!Modifier.isAbstract(klass.getModifiers()) && hasUIControlField(klass);
		
		if(needs_setEnabled_MethodOverride) {
			
			// if the subclass has declared new UI control fields, ensure it has override setEnabled too
			
			for(Method method : klass.getDeclaredMethods()) {
				if(method.getName().equals("setEnabled")) {
					return;
				}
				if(method.getName().equals("doSetEnabled")) {
					return;
				}
			}
			assertFail();
			
		} else {
			
			verify_setEnabled_contract(klass.getSuperclass());
			return;
		}
		
	}
	
	public static boolean hasUIControlField(Class<?> klass) {
		Field[] declaredFields = klass.getDeclaredFields();
		for(Field field : declaredFields) {
			if(field.getName().startsWith("this$")) {
				continue;
			}
			if(Modifier.isStatic(field.getModifiers()) || field.getType().isPrimitive()) {
				continue;
			}
			if(isUIControlField(field)) {
				return true;
			}
			
		}
		return false;
	}
	
	public static boolean isUIControlField(Field field) {
		Type fieldType = field.getAnnotatedType().getType();
		if(fieldType instanceof Class) {
			Class<?> fieldKlass = (Class<?>) fieldType;
			if(Control.class.isAssignableFrom(fieldKlass)) {
				return true;
			}
		}
		return false;
	}
	
}