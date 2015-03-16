/*******************************************************************************
 * Copyright (c) 2008, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.text;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import melnorme.lang.ide.core.TextSettings_Actual;
import melnorme.utilbox.misc.ArrayUtil;

import org.eclipse.core.filebuffers.IDocumentSetupParticipant;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;

public class LangDocumentPartitionerSetup implements IDocumentSetupParticipant {
	
	public static final String[] LEGAL_CONTENT_TYPES = 
			ArrayUtil.remove(TextSettings_Actual.PARTITION_TYPES, IDocument.DEFAULT_CONTENT_TYPE);
	
	protected static LangDocumentPartitionerSetup instance = new LangDocumentPartitionerSetup();
	
	public static LangDocumentPartitionerSetup getInstance() {
		return instance;
	}
	
	public LangDocumentPartitionerSetup() {
	}
	
	@Override
	public void setup(IDocument document) {
		setupDocumentPartitioner(document, TextSettings_Actual.PARTITIONING_ID);
	}
	
	public IPartitionTokenScanner createPartitionScanner() {
		return TextSettings_Actual.createPartitionScanner();
	}
	
	public final IDocumentPartitioner createDocumentPartitioner() {
		IPartitionTokenScanner scanner = createPartitionScanner();
		return new FastPartitioner(scanner, LEGAL_CONTENT_TYPES);
	}
	
	protected void setupDocumentPartitioner(IDocument document, String partitioning) {
		IDocumentPartitioner partitioner = createDocumentPartitioner();
		setupDocumentPartitioner$2(partitioner, document, partitioning);
	}
	
	public static void setupDocumentPartitioner$2(IDocumentPartitioner partitioner, IDocument document, 
			String partitioning) {
		assertNotNull(partitioner);
		
		partitioner.connect(document);
		if (document instanceof IDocumentExtension3) {
			IDocumentExtension3 extension3 = (IDocumentExtension3) document;
			extension3.setDocumentPartitioner(partitioning, partitioner);
		} else {
			document.setDocumentPartitioner(partitioner);
		}
	}
	
	public void setupPartitioningIfNotSet(IDocument document) {
		if(document instanceof IDocumentExtension3) {
			IDocumentExtension3 extension3 = (IDocumentExtension3) document;
			
			String partitioning = TextSettings_Actual.PARTITIONING_ID;
			
			if(extension3.getDocumentPartitioner(partitioning) == null) {
				IDocumentPartitioner partitioner = createDocumentPartitioner();
				partitioner.connect(document);
				extension3.setDocumentPartitioner(partitioning, partitioner);
			}
		}
	}
	
}