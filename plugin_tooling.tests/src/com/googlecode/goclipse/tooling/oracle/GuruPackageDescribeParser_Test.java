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
package com.googlecode.goclipse.tooling.oracle;

import static com.googlecode.goclipse.tooling.CommonGoToolingTest.fixTestsPaths;
import static melnorme.lang.tooling.structure.StructureElementKind.CONST;
import static melnorme.lang.tooling.structure.StructureElementKind.FUNCTION;
import static melnorme.lang.tooling.structure.StructureElementKind.INTERFACE;
import static melnorme.lang.tooling.structure.StructureElementKind.METHOD;
import static melnorme.lang.tooling.structure.StructureElementKind.STRUCT;
import static melnorme.lang.tooling.structure.StructureElementKind.VARIABLE;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.junit.Test;

import melnorme.lang.tooling.EAttributeFlag;
import melnorme.lang.tooling.EProtection;
import melnorme.lang.tooling.ElementAttributes;
import melnorme.lang.tooling.ast.ParserErrorTypes;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.lang.tooling.common.ParserError;
import melnorme.lang.tooling.structure.AbstractStructureParser;
import melnorme.lang.tooling.structure.AbstractStructureParser_Test;
import melnorme.lang.tooling.structure.SourceFileStructure;
import melnorme.lang.tooling.structure.StructureElement;
import melnorme.lang.tooling.structure.StructureElementKind;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.ArrayUtil;
import melnorme.utilbox.misc.Location;

public class GuruPackageDescribeParser_Test extends AbstractStructureParser_Test {
	
	@Override
	public String getClassResource(String resourceName) {
		return fixTestsPaths(super.getClassResource(resourceName));
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
	
	protected ArrayList2<StructureElement> elems(StructureElement... expectedElements) {
		return ArrayList2.create(expectedElements);
	}
	
	public StructureElement elem(String name, SourceRange nameSR, StructureElementKind elementKind,
			ElementAttributes elementAttributes, String type, Indexable<StructureElement> children) {
		return new StructureElement(name, nameSR, nameSR, elementKind, elementAttributes, type, children);
	}
	
	protected Location location;
	
	protected void testParseStructure(String describeOutput, String goSource, StructureElement... expectedElements)
			throws CommonException {
		this.location = null;
		this.source = goSource;
		super.testParseStructure(describeOutput, list(), expectedElements);
	}
	
	protected void testParseStructure(String describeOutput, String goSource, Location location, 
			StructureElement... expectedElements)
			throws CommonException {
		this.location = location;
		this.source = goSource;
		
		AbstractStructureParser parser = createStructureParser();
		SourceFileStructure structure = parser.parse(describeOutput);
		
		ArrayList2<StructureElement> expectedStructure = ArrayList2.create(expectedElements);
		SourceFileStructure expected = new SourceFileStructure(location, expectedStructure, null);
		
		assertAreEqual(structure.getChildren(), expected.getChildren());
		assertEquals(structure, expected);	
	}
	
	@Override
	protected GuruPackageDescribeParser createStructureParser() {
		return new GuruPackageDescribeParser(location, source);
	}
	
	public int ixof(String marker) {
		int indexOf = source.indexOf(marker);
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
		
		testParseStructure(getClassResource("oracle_describe.0_Empty.json"), "");
		
		
		source = getClassResource("oracle_describe.1_Basic.go");
		testParseStructure(
			getClassResource("oracle_describe.1_Basic.json"), source,
			
			elem("Hello", sr("Hello"), FUNCTION, att(), "func()", null),
			elem("other", sr("other"), FUNCTION, attPriv(), "func()", null),
			elem("i2", sr("i2"), VARIABLE, attPriv(), "int", null),
			elem("xxx", sr("xxx"), VARIABLE, attPriv(), "int", null)
		);
		
		try {
			testParseStructure(getClassResource("oracle_describe.2_Error1.json"), "");
			assertFail();
		} catch (CommonException e) {
			// continue
		}
		
		// test bad source ranges
		try {
			testParseStructure(getClassResource("oracle_describe.2_Error2a.json"), "");
			assertFail();
		} catch (CommonException e) { 
			assertTrue(e.toString().contains("Invalid line number: 0"));
		}
		try {
			testParseStructure(getClassResource("oracle_describe.2_Error2b.json"), "");
			assertFail();
		} catch (CommonException e) { 
			assertTrue(e.toString().contains("Invalid line: 10 is over the max bound: 1."));
		}
		
		source = getClassResource("oracle_describe.2_Test.go");
		testParseStructure(getClassResource("oracle_describe.2_Test.json"), source,
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
		
		source = getClassResource("oracle_describe.A_std_url.go");
		new GuruPackageDescribeParser(null, source).parse(
			getClassResource("oracle_describe.A_std_url.json"));
		
		
		source = DEFAULT_SOURCE;
		testParseStructure(getClassResource("oracle_describe.3_methods.json"), source,
			Location.create(fixTestsPaths("D:/devel/tools.Go/go-workspace/src/util/other/hello_other.go")),
			
			new StructureElement("MainController", null, sr(7, 0), STRUCT, att(), null, elems(
				elem("RegisterJson", sr(7, 0), METHOD, att(), "func()", null)
			))
		);
		
	}
	
	
	@Test
	public void testErrorParse() throws Exception { testErrorParse$(); }
	public void testErrorParse$() throws Exception {
		source = DEFAULT_SOURCE; 
		GuruPackageDescribeParser parser = createStructureParser();
		
		String errorMsg = "oracle: C:\\Users\\Bruno\\src\\describe.go:1:2: expected operand, found 'for'";
		
		SourceFileStructure structure = parser.parseErrorMessage(errorMsg);
		assertEquals(structure, new SourceFileStructure(location, list(), list(
			new ParserError(ParserErrorTypes.GENERIC_ERROR, srAt(7, 8), "expected operand, found 'for'", null)))
		);
	}
	
}