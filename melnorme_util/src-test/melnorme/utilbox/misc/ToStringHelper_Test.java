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

import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.misc.ToStringHelper.ToString;
import melnorme.utilbox.tests.CommonTest;

public class ToStringHelper_Test extends CommonTest {
	
	public static class Foo implements ToString {
		public String name;
		public Foo foo;
		public Indexable<?> children;
		
		public Foo(String name, Foo foo, Indexable<?> children) {
			super();
			this.name = name;
			this.foo = foo;
			this.children = children;
		}
		
		public Foo(String name, Foo foo) {
			this(name, foo, null);
		}
		
		@Override
		public String toString() {
			return defaultToString();
		}
		
		@Override
		public void toString(ToStringHelper sh) {
			sh.writeBlock("[", (sh2) -> {
				sh.writeElementWithPrefix("NAME: ", name);
				sh.writeElementWithPrefix("FOO: ", foo);
				sh.writeElementWithPrefix("CHILDREN: ", children);
			}, "]");
		}
	}
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() throws Exception {
		ToStringHelper sh;
		
		sh = new ToStringHelper();
		sh.writeElement("123");
		checkEquals(sh.getString(), "123\n");
		
		sh.writeElement("456");
		checkEquals(sh.getString(), "123\n456\n");
		
		sh = new ToStringHelper();
		sh.writeElement(null);
		checkEquals(sh.getString(), "");
		
		sh = new ToStringHelper();
		sh.indentation = 1;
		sh.writeElement("123");
		checkEquals(sh.getString(), "  123\n");

		sh.writeElement("456");
		checkEquals(sh.getString(), "  123\n  456\n");
		
		
		sh = new ToStringHelper();
		sh.writeElementWithPrefix("Field: ", "123");
		checkEquals(sh.getString(), "Field: 123\n");

		sh = new ToStringHelper();
		sh.indentation = 2;
		sh.writeElementWithPrefix("Field: ", "123");
		checkEquals(sh.getString(), "    Field: 123\n");

		/* ----------------- BLOCK ----------------- */

		sh = new ToStringHelper();
		sh.writeBlock("[", (sh2) -> {
			// Empty block
		}, "]");
		checkEquals(sh.getString(), "[\n]\n");

		sh = new ToStringHelper();
		sh.writeBlock("[", (sh2) -> {
			sh2.writeElement("xxx");
		}, "]");
		checkEquals(sh.getString(), "[\n  xxx\n]\n");

		sh = new ToStringHelper();
		sh.writeBlock("[", (sh2) -> {
			 sh2.writeElementWithPrefix("Foo: ", "xxx");
		}, "]");
		checkEquals(sh.getString(), "[\n  Foo: xxx\n]\n");
		
		/* -----------------  ----------------- */
		
		// write list element
		sh = new ToStringHelper();
		sh.writeList("<", list("one", "two"), ">");
		checkEquals(sh.getString(), "<\n  one\n  two\n>\n");

		sh = new ToStringHelper();
		sh.indentation = 1;
		sh.writeList(list("one", "two"));
		checkEquals(sh.getString(), "  [\n    one\n    two\n  ]\n");
		
		sh = new ToStringHelper();
		sh.writeList("<", null, ">");
		checkEquals(sh.getString(), "");
		
		sh = new ToStringHelper();
		sh.writeElementWithPrefix("CHILDREN: ", list("one", "two"));
		checkEquals(sh.getString(), "CHILDREN: [\n  one\n  two\n]\n");
		
		/* -----------------  ----------------- */
		
		checkEquals(
			 new Foo("xxx", null).toString(),
			 "[\n" +
			 "  NAME: xxx\n" +
			 "  FOO: null\n" +
			 "  CHILDREN: null\n" +
			 "]\n"
		);
		
		checkEquals(
			 new Foo("xxx", new Foo("yyy", null)).toString(),
			 "[\n" +
			 "  NAME: xxx\n" +
			 "  FOO: [\n" +
			 "    NAME: yyy\n" +
			 "    FOO: null\n" +
			 "    CHILDREN: null\n" +
			 "  ]\n" +
			 "  CHILDREN: null\n" +
			 "]\n"
		);
		
		checkEquals(
			 new Foo("xxx", null, list("123", "456")).toString(),
			 
			 "[\n" +
			 "  NAME: xxx\n" +
			 "  FOO: null\n" +
			 "  CHILDREN: [\n    123\n    456\n  ]\n" +
			 "]\n"
		);
		
		checkEquals(
			 new Foo("xxx", null, list(new Foo("yyy", null))).toString(),
			 
			 "[\n" +
			 "  NAME: xxx\n" +
			 "  FOO: null\n" +
			 "  CHILDREN: [\n" +
			 "    [\n" +
			 "      NAME: yyy\n" +
			 "      FOO: null\n" +
			 "      CHILDREN: null\n" +
			 "    ]\n" +
			 "  ]\n" +
			 "]\n"
		);
	}
	
}