/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.text;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import melnorme.lang.ide.core.text.BlockHeuristicsScannner;
import melnorme.lang.ide.core.text.BlockHeuristicsScannner.BlockTokenRule;
import melnorme.utilbox.misc.ArrayUtil;
import melnorme.utilbox.tests.CommonTest;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;



public abstract class Scanner_BaseTest extends CommonTest {
	
	protected static final String NL = "\r\n";
	protected static final String TAB = "\t";
	
	public static final String NEUTRAL_SRC1 = 
		line("void func() {")+
		line(TAB+"blah();")+
		line(TAB+"blah2([1, 2, 3]);")+
		line("}")
		;
	public static final String NEUTRAL_SRC2 = NEUTRAL_SRC1; // TODO: should write some other sample code
	public static final String NEUTRAL_SRC3 = NEUTRAL_SRC1;
	
	public static String line(String string) {
		return string+NL;
	}
	
	
	protected Document document;
	
	protected Document getDocument() {
		if(document == null) {
			document = createDocument();
		}
		return document;
	}
	
	protected Document createDocument() {
		Document document = new Document();
		assertTrue(ArrayUtil.contains(document.getLegalLineDelimiters(), NL));
		setupSamplePartitioner(document);
		return document;
	}
	
	public static BlockHeuristicsScannner createBlockHeuristicScannerWithSamplePartitioning(IDocument document) {
		String partitioning = SamplePartitionScanner.LANG_PARTITIONING;
		String contentType = IDocument.DEFAULT_CONTENT_TYPE;
		BlockTokenRule[] blockTokens = BlockHeuristicsScannnerTest.SAMPLE_BLOCK_TOKENS;
		return new BlockHeuristicsScannner(document, partitioning, contentType, blockTokens);
	}
	
	public static void setupSamplePartitioner(Document document) {
		SamplePartitionScanner partitionScanner = new SamplePartitionScanner();
		setupPartitioner(document, partitionScanner, 
			SamplePartitionScanner.LANG_PARTITIONING, SamplePartitionScanner.LEGAL_CONTENT_TYPES);
	}
	
	public static FastPartitioner setupPartitioner(Document document, IPartitionTokenScanner partitionScanner,
			String partitioning, String[] legalContentTypes) {
		FastPartitioner fp = new FastPartitioner(partitionScanner, legalContentTypes);
		LangDocumentPartitionerSetup.setupDocumentPartitioner(document, partitioning, fp);
		return fp;
	}
	
}