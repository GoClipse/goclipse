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
package melnorme.utilbox.collections;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import static melnorme.utilbox.core.CoreUtil.areEqual;

import java.util.ArrayList;
import java.util.LinkedList;

import org.junit.Test;

import melnorme.utilbox.tests.CommonTestUtils;

public class Indexable_Tests extends CommonTestUtils {
	
	@Test
	public void testEquals() throws Exception { testEquals$(); }
	public void testEquals$() throws Exception {
		testEquals(true, 
			ArrayView.create(array()), 
			ListView.create(createArrayList()),
			new ArrayList2<>(),
			createArrayList(),
			new LinkedList<>()
		);
		
		testEquals(true, 
			ArrayView.create(array("Foo", "Bar")), 
			ListView.create(ArrayList2.create("Foo", "Bar")),
			ArrayList2.create("Foo", "Bar"),
			createArrayList("Foo", "Bar")
		);
		
		testEquals(false, 
			ArrayList2.create("asdf"), 
			ArrayView.create(array("ss")), 
			ListView.create(ArrayList2.create("zzz")),
			createArrayList("Foo")
		);
		
		testEquals(false, 
			ArrayList2.create("one", "twoo"), 
			ArrayView.create(array("xxx")), 
			ListView.create(createArrayList()),
			createArrayList(1, 2, 3)
		);
		
	}
	
	protected ArrayList<Object> createArrayList(Object... objs) {
		ArrayList<Object> arrayList = new ArrayList<>();
		arrayList.addAll(ArrayList2.create(objs));
		return arrayList;
	}
	
	protected void testEquals(boolean expectedAreEqual, Object... objs) {
		for(Object objA : objs) {
			for(Object objB : objs) {
				if(expectedAreEqual == false && objA == objB) {
					continue;
				}
				checkAreEqual(expectedAreEqual, objA, objB);
			}
		}
	}
	
	protected void checkAreEqual(boolean expectedAreEqual, Object objA, Object objB) {
		assertTrue(areEqual(objA, objB) == expectedAreEqual);
	}
	
	@Test
	public void testListMethod() throws Exception { testListMethod$(); }
	public void testListMethod$() throws Exception {
		String[] array = ArrayView.create(array("String")).toArray(String.class);
		assertEqualArrays(array, array("String"));
		
		assertTrue(ArrayList2.create("zero", "one", "two", "two").indexOf("two") == 2);
		assertTrue(ArrayList2.create("zero", "one", "two", "zero").indexOf("zero") == 0);
		assertTrue(ArrayList2.create("zero", "one", "two", "two").lastIndexOf("two") == 3);
		assertTrue(ArrayList2.create("zero", "one", "two", "zero").lastIndexOf("zero") == 3);
		assertTrue(ArrayList2.create("zero", "one", "two").lastIndexOf("zero") == 0);
	}
	
}