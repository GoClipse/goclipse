/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.utilbox.misc;

import org.junit.Test;

import melnorme.utilbox.misc.ReflectionUtils.IllegalFieldValue;
import melnorme.utilbox.tests.CommonTest;

class AbstractFoo {
	
	private String string = "abc";
	
	public String getString() {
		return string;
	}
}

class Foo extends AbstractFoo {
	private Number number = 2;
	
	public Number getNumber() {
		return number;
	}
}

public class ReflectionUtils_Test extends CommonTest {
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() throws Exception {
		
		assertAreEqual(ReflectionUtils.readField(new Foo(), "number"), 2);
		assertAreEqual(ReflectionUtils.readField(new Foo(), "string"), "abc");
		
		Foo foo = new Foo();
		ReflectionUtils.writeField(foo, "number", 10);
		assertAreEqual(foo.getNumber(), 10);
		
		ReflectionUtils.writeField(foo, "string", "xxx");
		assertAreEqual(foo.getString(), "xxx");
		
		verifyThrows(() -> ReflectionUtils.writeField(foo, "number", "xxx"), IllegalFieldValue.class, 
			"Can not set java.lang.Number field");
	}
	
}