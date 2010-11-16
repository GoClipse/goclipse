package com.googlecode.goclipse.editors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;

/**
 * 
 * @author 
 */
public class TextDocumentProvider extends TextFileDocumentProvider {	
	protected FileInfo createFileInfo(Object element) throws CoreException {
        FileInfo info = super.createFileInfo(element);
        if(info==null){
                info = createEmptyFileInfo();
        }
        IDocument document = info.fTextFileBuffer.getDocument();
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
        return info;
    }
	
}