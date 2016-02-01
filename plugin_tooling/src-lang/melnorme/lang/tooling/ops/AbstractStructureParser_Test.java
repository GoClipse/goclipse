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
package melnorme.lang.tooling.ops;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import static melnorme.utilbox.misc.StringUtil.replaceAll;

import melnorme.lang.tests.CommonToolingTest;
import melnorme.lang.tooling.EAttributeFlag;
import melnorme.lang.tooling.EProtection;
import melnorme.lang.tooling.ElementAttributes;
import melnorme.lang.tooling.ast.ParserError;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.lang.tooling.structure.SourceFileStructure;
import melnorme.lang.tooling.structure.StructureElement;
import melnorme.lang.tooling.structure.StructureElementKind;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.core.CommonException;

public abstract class AbstractStructureParser_Test extends CommonToolingTest {
	
	public AbstractStructureParser_Test() {
		super();
	}
	
	protected String defaultSource = "aaaaa\n0123456789\nxxx\nabcdefghijkl\n";
	
	public static String quoteString(String string) {
		return '"' + replaceAll(string, "\"", "\\\"") + '"';
	}
	
	protected SourceRange sr(int offset, int length) {
		return new SourceRange(offset, length);
	}
	
	protected SourceRange srAt(int startPos, int endPos) {
		return SourceRange.srStartToEnd(startPos, endPos);
	}
	
	protected int pos(int line_0, int col_0) {
		int lineOffset = 0;
		while(line_0 > 0) {
			int newIx = defaultSource.indexOf('\n', lineOffset);
			assertTrue(newIx > 0);
			lineOffset = newIx + 1;
			line_0--;
		}
		return lineOffset + col_0;
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
	
	public StructureElement elem(String name, SourceRange sr, SourceRange nameSR, StructureElementKind elementKind,
			ElementAttributes elementAttributes, String type, Indexable<StructureElement> children) {
		if(elementAttributes == null) {
			elementAttributes = new ElementAttributes(null);
		}
		return new StructureElement(name, nameSR, sr, elementKind, elementAttributes, type, children);
	}
	
	/* -----------------  ----------------- */
	
	protected void testParseStructure(String describeOutput, Indexable<ParserError> parserProblems, 
			StructureElement... expectedElements)
			throws CommonException {
		ArrayList2<StructureElement> expectedStructure = new ArrayList2<>(expectedElements);
		SourceFileStructure expected = new SourceFileStructure(null, expectedStructure, parserProblems);
		
		testParseStructure(describeOutput, expected);
	}
	
	protected void testParseStructure(String describeOutput, SourceFileStructure expected) throws CommonException {
		AbstractStructureParser parser = createStructureParser();
		SourceFileStructure structure = parser.parse(describeOutput);
		assertAreEqual(structure.getElementsContainer(), expected.getElementsContainer());
		
		assertEquals(expected, structure);
	}
	
	protected abstract AbstractStructureParser createStructureParser();
	
}