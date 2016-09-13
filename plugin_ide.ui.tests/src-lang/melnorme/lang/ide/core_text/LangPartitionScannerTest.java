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
package melnorme.lang.ide.core_text;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import static melnorme.utilbox.core.CoreUtil.downCast;

import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.TypedPosition;
import org.eclipse.jface.text.rules.FastPartitioner;

import melnorme.utilbox.misc.ArrayUtil;
import melnorme.utilbox.tests.CommonTest;

public class LangPartitionScannerTest extends CommonTest {
	
	protected static final String NL = "\n";
	protected Document document;
	protected FastPartitioner fp;
	protected boolean recreateDocSetup = true;
	
	public LangPartitionScannerTest() {
		super();
	}
	
	
	protected void testPartitions(String source, Enum<?>... expectedPositions) throws BadPositionCategoryException {
		testPartitions(source, ArrayUtil.map(expectedPositions, 
			(enm) -> enm.toString(), String.class));
	}
	
	protected void testPartitions(String source, String... expectedPositions) throws BadPositionCategoryException {
		Position[] positions = calculatePartitions(source);
		checkPositions(positions, expectedPositions);
	}
	
	protected Position[] calculatePartitions(String docContents) throws BadPositionCategoryException {
		setupDocument(docContents);
		Position[] positions = getPartitionPositions();
		return positions;
	}
	protected void setupDocument(String docContents) {
		if(recreateDocSetup){ 
			document = new Document(docContents);
			fp = LangDocumentPartitionerSetup.getInstance().setupDocument(document);
		} else {
			document.set(docContents);
			assertNotNull(fp);
		}
	}
	protected Position[] getPartitionPositions() throws BadPositionCategoryException {
		return document.getPositions(fp.getManagingPositionCategories()[0]);
	}
	
	protected void checkPositions(Position[] positions, String[] expectedPositions) {
		assertTrue(positions.length == expectedPositions.length);
		for (int i = 0; i < positions.length; i++) {
			TypedPosition position = downCast(positions[i], TypedPosition.class);
			assertTrue(position.getType() == expectedPositions[i]);
		}
	}
	
}