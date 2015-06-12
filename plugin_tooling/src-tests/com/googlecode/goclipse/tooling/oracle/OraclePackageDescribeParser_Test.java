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
import static melnorme.lang.tooling.structure.StructureElementKind.STRUCT;
import static melnorme.lang.tooling.structure.StructureElementKind.VARIABLE;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;
import melnorme.lang.tests.CommonToolingTest;
import melnorme.lang.tooling.EAttributeFlag;
import melnorme.lang.tooling.EProtection;
import melnorme.lang.tooling.ElementAttributes;
import melnorme.lang.tooling.ast.ParserError;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.lang.tooling.structure.SourceFileStructure;
import melnorme.lang.tooling.structure.StructureElement;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.ArrayUtil;

import org.junit.Test;

public class OraclePackageDescribeParser_Test extends CommonToolingTest {
	
	public static ElementAttributes attrib(EProtection protection, EAttributeFlag... flags) {
		return new ElementAttributes(protection, flags);
	}
	
	public static ElementAttributes att(EAttributeFlag... flags) {
		return new ElementAttributes(EProtection.PUBLIC, flags);
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
	
	
	
	protected void testParseStructure(String source, StructureElement... expectedElements) throws CommonException {
		OraclePackageDescribeParser parser = new OraclePackageDescribeParser(null);
		SourceFileStructure structure = parser.parse(source);
		
		ArrayList2<StructureElement> expectedStructure = new ArrayList2<>(expectedElements);
		SourceFileStructure expected = new SourceFileStructure(null, expectedStructure, (Indexable<ParserError>) null);
		assertAreEqualLists(expected.getChildren(), structure.getChildren());
		
		assertEquals(structure, expected);
	}
	
	protected static final String USER__Type = "func(username string) *util.Userinfo";
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() throws Exception {
		
		testParseStructure(getClassResourceAsString("oracle_describe.0_Empty.json"));
		
		testParseStructure(getClassResourceAsString("oracle_describe.1_Basic.json"),
			new StructureElement("Hello2", sr(5, 6), sr(5, 6), FUNCTION, att(), "func()", null),
			new StructureElement("other", sr(10, 6), sr(10, 6), FUNCTION, att(), "func()", null),
			new StructureElement("i", sr(15, 5), sr(15, 5), VARIABLE, att(), "int", null),
			new StructureElement("xxx", sr(17, 5), sr(17, 5), VARIABLE, att(), "int", null)
		);
		
		try {
			testParseStructure(getClassResourceAsString("oracle_describe.2_Error1.json"));
			assertFail();
		} catch (CommonException e) {
			// continue
		}
		
		testParseStructure(getClassResourceAsString("oracle_describe.2_Test.json"),
			new StructureElement("Hello2", sr(5, 6), sr(5, 6), FUNCTION, att(), "func()", null),
			new StructureElement("xxx", sr(17, 5), sr(17, 5), VARIABLE, att(), "int", null),
			new StructureElement("encodeFragment", sr(56, 2), sr(56, 2), CONST, att(), "util.encoding", null),
			new StructureElement("User", sr(242, 6), sr(242, 6), FUNCTION, att(), USER__Type, null),
			new StructureElement("geometry", sr(59, 6), sr(59, 6), INTERFACE, att(), null, 
				elems(
					new StructureElement("(geometry) area() float64", sr(60, 5), sr(60, 5), FUNCTION, att(), null, null),
					new StructureElement("(geometry) perim() float64", sr(61, 5), sr(61, 5), FUNCTION, att(), null, null)
				)),
			new StructureElement("URL", sr(230, 6), sr(230, 6), STRUCT, att(), null, 
				elems(
					new StructureElement("(*URL) IsAbs() bool", sr(624, 15), sr(624, 15), FUNCTION, att(), null, null),
					new StructureElement("(*URL) Parse(ref string) (*URL, error)", sr(631, 15), sr(631, 15), FUNCTION, att(), null, null)
				))
		);

		new OraclePackageDescribeParser(null).parse(getClassResourceAsString("oracle_describe.A_std_url.json"));
		
	}
	
}