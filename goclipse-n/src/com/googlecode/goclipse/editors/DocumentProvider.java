package com.googlecode.goclipse.editors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.FileDocumentProvider;

/**
 * 
 * @author steel
 */
public class DocumentProvider extends FileDocumentProvider {
	
	private static final String CHARSET="UTF-8";
	
   /**
    * 
    */
	protected IDocument createDocument(Object element) throws CoreException {
		IFileEditorInput fei = (IFileEditorInput)element;
		if (!fei.getFile().getCharset().equalsIgnoreCase(CHARSET)) {
			//this forces a build!
			fei.getFile().setCharset(CHARSET, null);	
		}
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