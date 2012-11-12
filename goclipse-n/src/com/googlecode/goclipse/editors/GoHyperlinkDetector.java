package com.googlecode.goclipse.editors;

import java.io.File;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;

import sun.text.normalizer.UTF16;

import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.go.CodeContext;
import com.googlecode.goclipse.go.lang.model.Import;
import com.googlecode.goclipse.go.lang.model.Node;

/**
 * A hyperlink detector for the Go editor.
 */
public class GoHyperlinkDetector implements IHyperlinkDetector {

	private GoEditor editor;
	
	/**
	 * Create a new GoHyperlinkDetector.
	 */
	public GoHyperlinkDetector(GoEditor editor) {
		this.editor = editor;
	}
	
	/**
	 * 
	 */
	@Override
	public IHyperlink[] detectHyperlinks(ITextViewer textViewer, IRegion region, boolean showMultiple) {
		IHyperlink[] link       = new IHyperlink[1];
		//ITextEditor  textEditor = (ITextEditor)getAdapter(ITextEditor.class);
		int          offset     = region.getOffset();
		
		IDocument document = editor.getDocumentProvider().getDocument(editor.getEditorInput());
		IRegion wordRegion = findWord(document, offset);
		try {
			String word = document.get(wordRegion.getOffset(), wordRegion.getLength());
			if (word.contains(".")) {
				IProject project = Environment.INSTANCE.getCurrentProject();
				String[] parts = word.split("\\.");
				
				// get the current imports
				CodeContext cc1 = CodeContext.getCodeContext(
									project,
									((FileEditorInput)editor.getEditorInput()).getFile().getLocation().toOSString(),
									document.get());
				
				if (cc1 == null) {
					return null;
				}
				
				Import i = cc1.getImportForName(parts[0]);
				if (i != null) {
					Node node = cc1.getLocationForPkgAndName(i.path, parts[1]);
					if (node == null) {
						return null;
					}
					
					GoHyperLink h = new GoHyperLink();
					h.node   = node;
					h.region = wordRegion;
					h.text   = word;
					h.type   = "go function";
					
					link[0]  = h;
					return link;
			
				}
				
				
				if (true) {
					
				}
				
			}
		
		// non critical code... ignoring exceptions
        } catch (Exception e) {
        	e.printStackTrace();
        }
		
		if (wordRegion == null || wordRegion.getLength() == 0) {
			return null;
		}
		
		return null;
	}

	/**
	 * @param document
	 * @param offset
	 * @return
	 */
	private static IRegion findWord(IDocument document, int offset) {

		int start= -2;
		int end= -1;

		try {
			int pos= offset;
			char c;

			while (pos >= 0) {
				c= document.getChar(pos);
				if (!Character.isJavaIdentifierPart(c) && c!='.' ) {
					// Check for surrogates
					if (UTF16.isSurrogate(c)) {
						
					} else {
						break;
					}
				}
				--pos;
			}
			start= pos;

			pos= offset;
			int length= document.getLength();

			while (pos < length) {
				c= document.getChar(pos);
				if (!Character.isJavaIdentifierPart(c) && c!='.')
					break;
				++pos;
			}
			end= pos;

		} catch (BadLocationException x) {
		}
		
		if (start >= -1 && end > -1) {
			if (start == offset && end == offset)
				return new Region(offset, 0);
			else if (start == offset)
				return new Region(start, end - start);
			else
				return new Region(start + 1, end - start - 1);
		}

		return null;
	}
	
	/**
	 * 
	 */
	class GoHyperLink implements IHyperlink {
		
		public Node    node;
		public IRegion region;
		public String  text;
		public String  type;

		@Override
        public IRegion getHyperlinkRegion() {
	        return region;
        }

		@Override
        public String getTypeLabel() {
	        return text;
        }

		@Override
        public String getHyperlinkText() {
	        return text;
        }

		@Override
        public void open() {
			
			File fileToOpen = node.getFile();
			 
			if (fileToOpen.exists() && fileToOpen.isFile()) {
			    IFileStore fileStore = EFS.getLocalFileSystem().getStore(fileToOpen.toURI());
			    IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			 
			    try {
			        IEditorPart part = IDE.openEditorOnFileStore( page, fileStore );
			        
			        ITextEditor editor = ((ITextEditor)part);
			        IDocument document = editor.getDocumentProvider().getDocument(editor.getEditorInput());
			        
			        if (document != null) {
			        	IRegion lineInfo = null;
			            try {
			              // line count internaly starts with 0, and not with 1 like in
			              // GUI
			              lineInfo = document.getLineInformation(node.getLine() - 1);
			            } catch (BadLocationException e) {}
			            
			            if (lineInfo != null) {
			              editor.selectAndReveal(lineInfo.getOffset(), lineInfo.getLength());
			            }
			        }
			        
			    } catch ( Exception e ) {
			        //Put your exception handler here if you wish to
			    }
			} else {
			    //Do something if the file does not exist
			}
	        
        }
		
	}

}
