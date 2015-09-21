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
package com.googlecode.goclipse.tooling.oracle;

import static melnorme.lang.tooling.structure.StructureElementKind.CONST;
import static melnorme.lang.tooling.structure.StructureElementKind.FUNCTION;
import static melnorme.lang.tooling.structure.StructureElementKind.INTERFACE;
import static melnorme.lang.tooling.structure.StructureElementKind.METHOD;
import static melnorme.lang.tooling.structure.StructureElementKind.STRUCT;
import static melnorme.lang.tooling.structure.StructureElementKind.VARIABLE;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.junit.Test;

import com.googlecode.goclipse.tooling.CommonGoToolingTest;

import melnorme.lang.tooling.EAttributeFlag;
import melnorme.lang.tooling.EProtection;
import melnorme.lang.tooling.ElementAttributes;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.lang.tooling.structure.SourceFileStructure;
import melnorme.lang.tooling.structure.StructureElement;
import melnorme.lang.tooling.structure.StructureElementKind;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.ArrayUtil;
import melnorme.utilbox.misc.Location;

public class OraclePackageDescribeParser_Test extends CommonGoToolingTest {
	
	@Override
	protected String getClassResourceAsString(String resourceName) {
		return fixTestsPaths(super.getClassResourceAsString(resourceName));
	}
	
	public static ElementAttributes attrib(EProtection protection, EAttributeFlag... flags) {
		return new ElementAttributes(protection, flags);
	}
	
	public static ElementAttributes att(EAttributeFlag... flags) {
		return new ElementAttributes(EProtection.PUBLIC, flags);
	}
	
	public static ElementAttributes attPriv(EAttributeFlag... flags) {
		return new ElementAttributes(EProtection.PRIVATE, flags);
	}
	
	protected static ElementAttributes eat(EAttributeFlag... flags) {
		return att(ArrayUtil.concat(flags, EAttributeFlag.TEMPLATED));
	}
	
	protected SourceRange sr(int offset, int length) {
		return new SourceRange(offset, length);
	}
	
	protected ArrayList2<StructureElement> elems(StructureElement... expectedElements) {
		return new ArrayList2<>(expectedElements);
	}
	
	public StructureElement elem(String name, SourceRange nameSR, StructureElementKind elementKind,
			ElementAttributes elementAttributes, String type, Indexable<StructureElement> children) {
		return new StructureElement(name, nameSR, nameSR, elementKind, elementAttributes, type, children);
	}
	
	protected String goSource;
	
	protected void testParseStructure(String describeOutput, String goSource, StructureElement... expectedElements)
			throws CommonException {
		testParseStructure(describeOutput, goSource, null, expectedElements);
	}
	
	protected void testParseStructure(String describeOutput, String goSource, Location location, 
			StructureElement... expectedElements)
			throws CommonException {
		OraclePackageDescribeParser parser = new OraclePackageDescribeParser(location);
		SourceFileStructure structure = parser.parse(describeOutput, goSource);
		
		ArrayList2<StructureElement> expectedStructure = new ArrayList2<>(expectedElements);
		SourceFileStructure expected = new SourceFileStructure(location, expectedStructure, null);
		assertAreEqual(expected.getChildren(), structure.getChildren());
		
		assertEquals(structure, expected);
	}
	
	public int ixof(String marker) {
		int indexOf = goSource.indexOf(marker);
		assertTrue(indexOf >= 0);
		return indexOf;
	}
	
	public SourceRange sr(String marker) {
		return sr(ixof(marker), marker.length());
	}
	
	
	protected static final String USER__Type = "func(username string) *util.Userinfo";
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() throws Exception {
		
		testParseStructure(getClassResourceAsString("oracle_describe.0_Empty.json"), "");
		
		
		goSource = getClassResourceAsString("oracle_describe.1_Basic.go");
		testParseStructure(
			getClassResourceAsString("oracle_describe.1_Basic.json"), goSource,
			
			elem("Hello", sr("Hello"), FUNCTION, att(), "func()", null),
			elem("other", sr("other"), FUNCTION, attPriv(), "func()", null),
			elem("i2", sr("i2"), VARIABLE, attPriv(), "int", null),
			elem("xxx", sr("xxx"), VARIABLE, attPriv(), "int", null)
		);
		
		try {
			testParseStructure(getClassResourceAsString("oracle_describe.2_Error1.json"), "");
			assertFail();
		} catch (CommonException e) {
			// continue
		}
		
		// test bad source ranges
		try {
			testParseStructure(getClassResourceAsString("oracle_describe.2_Error2a.json"), "");
			assertFail();
		} catch (CommonException e) { 
			assertTrue(e.toString().contains("Invalid line number: 0"));
		}
		try {
			testParseStructure(getClassResourceAsString("oracle_describe.2_Error2b.json"), "");
			assertFail();
		} catch (CommonException e) { 
			assertTrue(e.toString().contains("Invalid line: 10 is over the max bound: 1."));
		}
		
		goSource = getClassResourceAsString("oracle_describe.2_Test.go");
		testParseStructure(getClassResourceAsString("oracle_describe.2_Test.json"), goSource,
			Location.create(fixTestsPaths("D:/devel/tools.Go/go-workspace/src/util/libfoo/libfoo.go")),
			
			elem("encodeFragment", sr("encodeFragment"), CONST, attPriv(), "util.encoding", null),
			elem("Hello2", sr("Hello2"), FUNCTION, att(), "func()", null),
			elem("xxx", sr("xxx"), VARIABLE, attPriv(), "int", null),
			elem("User", sr("User"), FUNCTION, att(), USER__Type, null),
			elem("geometry", sr("geometry"), INTERFACE, attPriv(), null, elems(
				elem("area", sr("area"), METHOD, att(), "func() float64", null),
				elem("perim", sr("perim"), METHOD, att(), "func() float64", null)
			)),
			elem("URL", sr("URL"), STRUCT, att(), null, elems(
				elem("IsAbs", sr("IsAbs"), METHOD, att(), "func() bool", null),
				elem("Parse", sr("Parse"), METHOD, att(), "func(ref string) (*URL, error)", null)
			))
		);
		
		goSource = getClassResourceAsString("oracle_describe.A_std_url.go");
		new OraclePackageDescribeParser(null).parse(
			getClassResourceAsString("oracle_describe.A_std_url.json"), goSource);
		
	}
	
}