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



import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.CoreUtil.tryCast;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPartitioningException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.TypedRegion;

/**
 * Helper abstract class to scan a document, such that it is aware of document partitions.
 * With a partitioning there must be a partitioner for that partitioning, and document must be a IDocumentExtension3 
 */
public class AbstractDocumentScanner {
	
	public static int TOKEN_EOF = -1;
	public static int TOKEN_INVALID = -2;
	public static int TOKEN_OUTSIDE = -3; // Token for whole partitions that we skip over
	
	protected final IDocument document;
	protected final String source;
	protected final String partitioning;
	protected final IDocumentExtension3 documentExt3;
	protected final IDocumentPartitioner partitioner;
	
	/** the partition type for the partitions that the document will scan */
	protected final String contentType;
	
	/** the current position. */
	protected int pos;
	/** the limit position, where the scanner will not scan beyond. */
	protected int posLimit;
	/** the last read token. */
	protected int token;
	/** last accessed partition (for caching purposes) */
	protected ITypedRegion lastPartition = new TypedRegion(-1, 0, "__no_partition_at_all"); // init with empty value 
	
	protected AbstractDocumentScanner(IDocument document, String partitioning, String contentType) {
		this.document = assertNotNull(document);
		this.partitioning = partitioning;
		
		this.source = document.get();
		
		if(partitioning == null) {
			this.contentType = IDocument.DEFAULT_CONTENT_TYPE;
			this.documentExt3 = null;
			this.partitioner = null;
		} else {
			Assert.isLegal(partitioning != null);
			Assert.isLegal(contentType != null);
			this.contentType = contentType;
			
			this.documentExt3 = tryCast(document, IDocumentExtension3.class);
			Assert.isLegal(documentExt3 != null, "document must support IDocumentExtension3");
			this.partitioner = documentExt3.getDocumentPartitioner(partitioning);
			Assert.isLegal(partitioner != null, "document must have a partitioner for " + partitioning);
		}
	}
	
	public IDocument getDocument() {
		return document;
	}
	
	public int getPosition() {
		return pos;
	}
	
	protected void setPosition(int pos) {
		this.pos = pos;
	}
	
	public final void setScanRange(int pos, int posLimit) {
		this.pos = pos;
		this.posLimit = posLimit;
	}
	
	protected final int getSourceLength() {
		return document.getLength();
	}
	
	protected final int readPreviousCharacter() {
		if(pos <= posLimit) {
			return token = TOKEN_EOF;
		} else {
			
			ITypedRegion partition;
			try {
				partition = getPartition(pos-1);
			} catch(BadLocationException e) {
				return token = TOKEN_OUTSIDE;
			}
			
			pos--;
			if (contentType.equals(partition.getType())) {
				return token = source.charAt(pos);
			} else {
				pos = partition.getOffset();
				return token = TOKEN_OUTSIDE;
			}
		}
	}
	
	protected final int readNextCharacter() {
		if(pos >= posLimit) {
			return token = TOKEN_EOF;
		} else {
			int charPos = pos;
			
			ITypedRegion partition;
			try {
				partition = getPartition(charPos);
			} catch(BadLocationException e) {
				return token = TOKEN_OUTSIDE;
			}
			
			pos++;
			if (contentType.equals(partition.getType())) {
				return token = source.charAt(charPos);
			} else {
				pos = partition.getOffset() + partition.getLength();
				return token = TOKEN_OUTSIDE;
			}
		}
	}
	
	protected final void revertPreviousCharacter()  {
		pos++;
	}
	
	protected final void revertNextCharacter()  {
		pos--;
	}
	
	public ITypedRegion getPartition(int position) throws BadLocationException {
		if(!partitionContainsPosition(lastPartition, position)) {
			if(documentExt3 != null) {
				try {
					lastPartition = documentExt3.getPartition(partitioning, position, false);
				} catch (BadPartitioningException e) {
					throw assertFail(); // Cannot happen, we have ensured that partitioning exists
				}
			} else {
				lastPartition = document.getPartition(position);
			}
		}
		return lastPartition;
	}
	
	public static boolean partitionContainsPosition(IRegion region, int position) {
		return region.getOffset() <= position && position < region.getOffset() + region.getLength();
	}
	
}
