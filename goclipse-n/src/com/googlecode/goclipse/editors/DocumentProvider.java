package com.googlecode.goclipse.editors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

/**
 * 
 * @author steel
 */
public class DocumentProvider extends FileDocumentProvider {
	
   /**
    * 
    */
	@Override
  protected IDocument createDocument(Object element) throws CoreException {
		IDocument document = super.createDocument(element);
		if (document != null) {
			IDocumentPartitioner partitioner =
				new FastPartitioner(
					new PartitionScanner(),
					new String[] {
							PartitionScanner.COMMENT,
							PartitionScanner.STRING,
							PartitionScanner.MULTILINE_STRING
						});
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		return document;
	}
	
}