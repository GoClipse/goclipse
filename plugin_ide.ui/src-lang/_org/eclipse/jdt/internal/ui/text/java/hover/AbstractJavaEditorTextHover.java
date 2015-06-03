/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Brock Janiczak <brockj@tpg.com.au> - [implementation] Streams not being closed in Javadoc views - https://bugs.eclipse.org/bugs/show_bug.cgi?id=214854
 *******************************************************************************/
package _org.eclipse.jdt.internal.ui.text.java.hover;

import melnorme.lang.ide.ui.editor.hover.ILangEditorTextHover;
import melnorme.lang.ide.ui.text.util.WordFinder;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.editors.text.EditorsUI;


/**
 * Abstract class for providing hover information for Java elements.
 *
 * @since 2.1
 */
public abstract class AbstractJavaEditorTextHover implements ILangEditorTextHover<Object> {
	
	private IEditorPart fEditor;
	
	@Override
	public void setEditor(IEditorPart editor) {
		fEditor= editor;
	}

	protected IEditorPart getEditor() {
		return fEditor;
	}

//	protected ICodeAssist getCodeAssist() {
//		if (fEditor != null) {
//			IEditorInput input= fEditor.getEditorInput();
//			if (input instanceof IClassFileEditorInput) {
//				IClassFileEditorInput cfeInput= (IClassFileEditorInput) input;
//				return cfeInput.getClassFile();
//			}
//
//			WorkingCopyManager manager= JavaPlugin.getDefault().getWorkingCopyManager();
//			return manager.getWorkingCopy(input, false);
//		}
//
//		return null;
//	}

	@SuppressWarnings("deprecation")
	@Override
	public Object getHoverInfo2(ITextViewer textViewer, IRegion hoverRegion) {
		return getHoverInfo(textViewer, hoverRegion);
	}

	@Override
	public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
		return WordFinder.findWord(textViewer.getDocument(), offset);
	}

//	/**
//	 * Returns the Java elements at the given hover region.
//	 *
//	 * @param textViewer the text viewer
//	 * @param hoverRegion the hover region
//	 * @return the array with the Java elements or <code>null</code>
//	 * @since 3.4
//	 */
//	protected IJavaElement[] getJavaElementsAt(ITextViewer textViewer, IRegion hoverRegion) {
//		/*
//		 * The region should be a word region an not of length 0.
//		 * This check is needed because codeSelect(...) also finds
//		 * the Java element if the offset is behind the word.
//		 */
//		if (hoverRegion.getLength() == 0)
//			return null;
//		
//		IDocument document= textViewer.getDocument();
//		if (document != null && isInheritDoc(document, hoverRegion))
//			return null;
//
//		ICodeAssist resolve= getCodeAssist();
//		if (resolve != null) {
//			try {
//				return resolve.codeSelect(hoverRegion.getOffset(), hoverRegion.getLength());
//			} catch (JavaModelException x) {
//				return null;
//			}
//		}
//		return null;
//	}

	/**
	 * Returns whether the word is "inheritDoc".
	 * 
	 * @param document the document
	 * @param wordRegion the word region
	 * @return <code>true</code> iff the word is "inheritDoc"
	 * @since 3.7
	 */
	protected static boolean isInheritDoc(IDocument document, IRegion wordRegion) {
		try {
			String word= document.get(wordRegion.getOffset(), wordRegion.getLength());
			return "inheritDoc".equals(word); //$NON-NLS-1$
		} catch (BadLocationException e) {
			return false;
		}
	}

	/*
	 * @see ITextHoverExtension#getHoverControlCreator()
	 * @since 3.0
	 */
	@Override
	public IInformationControlCreator getHoverControlCreator() {
		return new IInformationControlCreator() {
			@Override
			public IInformationControl createInformationControl(Shell parent) {
				return new DefaultInformationControl(parent, EditorsUI.getTooltipAffordanceString());
			}
		};
	}
	
}