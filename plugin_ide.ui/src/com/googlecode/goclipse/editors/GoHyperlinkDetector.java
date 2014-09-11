package com.googlecode.goclipse.editors;

import java.io.File;

import melnorme.lang.ide.ui.editor.EditorUtils;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.AbstractHyperlinkDetector;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;

import com.googlecode.goclipse.go.CodeContext;
import com.googlecode.goclipse.go.lang.model.Function;
import com.googlecode.goclipse.go.lang.model.Import;
import com.googlecode.goclipse.go.lang.model.Node;
import com.googlecode.goclipse.go.lang.model.Var;

/**
 * A hyperlink detector for the Go editor.  The hyperlink detector
 * is used for code navigation.
 */
public class GoHyperlinkDetector extends AbstractHyperlinkDetector {
	
	public GoHyperlinkDetector() {
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.text.hyperlink.IHyperlinkDetector#detectHyperlinks(org.eclipse.jface.text.ITextViewer, org.eclipse.jface.text.IRegion, boolean)
	 */
	@Override
	public IHyperlink[] detectHyperlinks(ITextViewer textViewer, IRegion region, boolean showMultiple) {
		ITextEditor textEditor = (ITextEditor) getAdapter(ITextEditor.class);
		if (region == null || !(textEditor instanceof GoEditor))
			return null;
		
		GoEditor editor = (GoEditor) textEditor;
		
		IHyperlink[] link       = new IHyperlink[1];
		int          offset     = region.getOffset();
		IDocument    document   = editor.getDocumentProvider().getDocument(editor.getEditorInput());
		IRegion      wordRegion = findWord(document, offset);
		
		try {
			String   word    = document.get(wordRegion.getOffset(), wordRegion.getLength());
			
			IProject project = EditorUtils.getAssociatedProject(editor.getEditorInput());

			// get the current imports
			FileEditorInput fileEditorInput = null;
			if(editor.getEditorInput() instanceof FileEditorInput){
				fileEditorInput = ((FileEditorInput) editor.getEditorInput());
			}
			
			if (fileEditorInput == null) {
				return null;
			}
			
			CodeContext cc1 = CodeContext.getCodeContext(project, fileEditorInput.getFile().getLocation().toOSString(),
					document.get());

			if (cc1 == null) {
				return null;
			}

			GoHyperLink h    = new GoHyperLink();
			int         line = document.getLineOfOffset(offset);
			
			// imported function call or method call
			if (word.contains(".")) {
				
				String[] parts = word.split("\\.");
				Import   i     = cc1.getImportForName(parts[0]);
				
				if (i != null) {
				
					Node node = cc1.getLocationForPkgAndName(i.path, parts[1]);
					if (node == null) {
						return null;
					}

					h.node   = node;
					h.region = wordRegion;
					h.text   = word;
					h.type   = "go function";
					link[0]  = h;

					return link;
				}
				
				Var v = cc1.getVarForName(parts[0], line);
				if (v != null) {
					h.node = v;
					h.region = wordRegion;
					h.text = word;
					h.type = "go methods";
					link[0] = h;
					return link;
				}

				// variable or local function call
			} else {
				
				// Is there a variable?
				Var v = cc1.getVarForName(word, line+1);
				if (v != null) {
					h.node = v;
					h.region = wordRegion;
					h.text = word;
					h.type = "go variable";
					link[0] = h;
					return link;
				}
				
				// Is there a variable?
				Function f = cc1.getFunctionForName(word+"()");
				if (f != null) {
					h.node = f;
					h.region = wordRegion;
					h.text = word;
					h.type = "go variable";
					link[0] = h;
					return link;
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

		int start = -2;
		int end = -1;

		try {
			
			int pos = offset;
			char c;

			while (pos >= 0) {
				c = document.getChar(pos);
				if (!Character.isJavaIdentifierPart(c) && c != '.') {
					// Check for surrogates
//					if (sun.text.normalizer.UTF16.isSurrogate(c)) {
					if (Character.isLowSurrogate(c) || Character.isHighSurrogate(c)) {

					} else {
						break;
					}
				}
				--pos;
			}
			start = pos;

			pos = offset;
			int length = document.getLength();

			while (pos < length) {
				c = document.getChar(pos);
				if (!Character.isJavaIdentifierPart(c) && c != '.') {
					break;
				}
				++pos;
			}
			end = pos;

		} catch (BadLocationException x) {
		}

		if (start >= -1 && end > -1) {
			
			if (start == offset && end == offset) {
				return new Region(offset, 0);
			
			} else if (start == offset) {
				return new Region(start, end - start);
			
			} else {
				return new Region(start + 1, end - start - 1);
			}
		}

		return null;
	}

	/**
	 * 
	 */
	class GoHyperLink implements IHyperlink {

		public Node		node;
		public IRegion	region;
		public String	text;
		public String	type;

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

			if (fileToOpen != null && fileToOpen.exists() && fileToOpen.isFile()) {
				IFileStore fileStore = EFS.getLocalFileSystem().getStore(fileToOpen.toURI());
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

				try {
					IEditorPart part     = IDE.openEditorOnFileStore(page, fileStore);
					ITextEditor editor   = ((ITextEditor) part);
					IDocument   document = editor.getDocumentProvider().getDocument(editor.getEditorInput());

					if (document != null) {
						IRegion lineInfo = null;
						
						try {
							// line count internally starts with 0, and not with 1 like in a GUI
							lineInfo = document.getLineInformation(node.getLine() - 1);
			
						} catch (BadLocationException e) {
						}

						if (lineInfo != null) {
							editor.selectAndReveal(lineInfo.getOffset(), lineInfo.getLength());
						}
					}

				} catch (Exception e) {
					// Put your exception handler here if you wish to
				}
			} else {
				// Do something if the file does not exist
			}
		}
	}

}
