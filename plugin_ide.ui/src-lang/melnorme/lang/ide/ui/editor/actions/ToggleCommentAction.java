/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package melnorme.lang.ide.ui.editor.actions;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.TextUtilities;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.texteditor.ITextEditor;

import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.editor.AbstractLangEditor;
import melnorme.lang.ide.ui.editor.LangEditorMessages;
import melnorme.lang.ide.ui.utils.operations.BasicEditorOperation;
import melnorme.utilbox.core.CommonException;


/**
 * An action which toggles comment prefixes on the selected lines.
 *
 * @since 3.0
 */
public class ToggleCommentAction extends TextEditorAction_Adapter {

	/** The text operation target */
	private ITextOperationTarget fOperationTarget;
	/** The document partitioning */
	private String fDocumentPartitioning;
	/** The comment prefixes */
	private Map<String, String[]> fPrefixesMap;
	
	public ToggleCommentAction(IWorkbenchPage page) {
		super(page);
	}
	
	protected String getOperationName() {
		return LangEditorMessages.ToggleComment_error_title;
	}
	
	@Override
	public boolean isEnabled() {
		return super.isEnabled() && canModifyEditor() && isTargetOperationEnabled();
	}
	
	protected boolean isTargetOperationEnabled() {
		ITextEditor editor = getTextEditor();
		if(editor != null) {
			fOperationTarget = (ITextOperationTarget) editor.getAdapter(ITextOperationTarget.class);
		}
		
		return fOperationTarget != null && 
			fOperationTarget.canDoOperation(ITextOperationTarget.PREFIX) && 
			fOperationTarget.canDoOperation(ITextOperationTarget.STRIP_PREFIX);
	}
	
	public void configure(ISourceViewer sourceViewer, SourceViewerConfiguration configuration) {
		fPrefixesMap= null;

		String[] types= configuration.getConfiguredContentTypes(sourceViewer);
		Map<String, String[]> prefixesMap= new HashMap<String, String[]>(types.length);
		for (int i= 0; i < types.length; i++) {
			String type= types[i];
			String[] prefixes= configuration.getDefaultPrefixes(sourceViewer, type);
			if (prefixes != null && prefixes.length > 0) {
				int emptyPrefixes= 0;
				for (int j= 0; j < prefixes.length; j++)
					if (prefixes[j].length() == 0)
						emptyPrefixes++;

				if (emptyPrefixes > 0) {
					String[] nonemptyPrefixes= new String[prefixes.length - emptyPrefixes];
					for (int j= 0, k= 0; j < prefixes.length; j++) {
						String prefix= prefixes[j];
						if (prefix.length() != 0) {
							nonemptyPrefixes[k]= prefix;
							k++;
						}
					}
					prefixes= nonemptyPrefixes;
				}

				prefixesMap.put(type, prefixes);
			}
		}
		fDocumentPartitioning= configuration.getConfiguredDocumentPartitioning(sourceViewer);
		fPrefixesMap= prefixesMap;
	}
	
	@Override
	protected BasicEditorOperation createOperation(ITextEditor editor) {
		return new BasicEditorOperation(getOperationName(), editor) {
			
	@Override
	protected void doRunWithEditor(AbstractLangEditor editor) throws CommonException {
		
		configure(editor.getSourceViewer_(), editor.getSourceViewerConfiguration_asLang());
		
		if (fOperationTarget == null || fDocumentPartitioning == null || fPrefixesMap == null) {
			throw new CommonException("Partitioning parameters not set");
		}
		
		if (!validateEditorInputState())
			return;
		
		if(!isTargetOperationEnabled()) {
			throw new CommonException(LangEditorMessages.ToggleComment_error_message);
		}
		
		final int operationCode;
		if (isSelectionCommented(editor.getSelectionProvider().getSelection())) {
			operationCode= ITextOperationTarget.STRIP_PREFIX;
		} else {
			operationCode= ITextOperationTarget.PREFIX;
		}
		
		Display display = getShell().getDisplay();
		
		BusyIndicator.showWhile(display, new Runnable() {
			@Override
			public void run() {
				fOperationTarget.doOperation(operationCode);
			}
		});
	}

		};
	}
	
	/**
	 * Is the given selection single-line commented?
	 *
	 * @param selection Selection to check
	 * @return <code>true</code> iff all selected lines are commented
	 */
	private boolean isSelectionCommented(ISelection selection) {
		if (!(selection instanceof ITextSelection))
			return false;

		ITextSelection textSelection= (ITextSelection) selection;
		if (textSelection.getStartLine() < 0 || textSelection.getEndLine() < 0)
			return false;

		IDocument document= getTextEditor().getDocumentProvider().getDocument(getTextEditor().getEditorInput());

		try {

			IRegion block= getTextBlockFromSelection(textSelection, document);
			ITypedRegion[] regions= TextUtilities.computePartitioning(document, fDocumentPartitioning, 
				block.getOffset(), block.getLength(), false);

			int[] lines= new int[regions.length * 2]; // [startline, endline, startline, endline, ...]
			for (int i= 0, j= 0; i < regions.length; i++, j+= 2) {
				// start line of region
				lines[j]= getFirstCompleteLineOfRegion(regions[i], document);
				// end line of region
				int length= regions[i].getLength();
				int offset= regions[i].getOffset() + length;
				if (length > 0)
					offset--;
				lines[j + 1]= (lines[j] == -1 ? -1 : document.getLineOfOffset(offset));
			}

			// Perform the check
			for (int i= 0, j= 0; i < regions.length; i++, j += 2) {
				String[] prefixes= fPrefixesMap.get(regions[i].getType());
				if (prefixes != null && prefixes.length > 0 && lines[j] >= 0 && lines[j + 1] >= 0)
					if (!isBlockCommented(lines[j], lines[j + 1], prefixes, document))
						return false;
			}

			return true;

		} catch (BadLocationException x) {
			// should not happen
			LangUIPlugin.logError("Unexpected error.", x);
		}

		return false;
	}

	/**
	 * Creates a region describing the text block (something that starts at
	 * the beginning of a line) completely containing the current selection.
	 *
	 * @param selection The selection to use
	 * @param document The document
	 * @return the region describing the text block comprising the given selection
	 */
	protected static IRegion getTextBlockFromSelection(ITextSelection selection, IDocument document) {

		try {
			IRegion line= document.getLineInformationOfOffset(selection.getOffset());
			int length= selection.getLength() == 0 ? 
					line.getLength() : 
					selection.getLength() + (selection.getOffset() - line.getOffset());
			return new Region(line.getOffset(), length);

		} catch (BadLocationException x) {
			// should not happen
			LangUIPlugin.logError("Unexpected error.", x);
		}

		return null;
	}

	/**
	 * Returns the index of the first line whose start offset is in the given text range.
	 *
	 * @param region the text range in characters where to find the line
	 * @param document The document
	 * @return the first line whose start index is in the given range, -1 if there is no such line
	 */
	protected static int getFirstCompleteLineOfRegion(IRegion region, IDocument document) {

		try {

			final int startLine= document.getLineOfOffset(region.getOffset());

			int offset= document.getLineOffset(startLine);
			if (offset >= region.getOffset())
				return startLine;

			final int nextLine= startLine + 1;
			if (nextLine == document.getNumberOfLines())
				return -1;

			offset= document.getLineOffset(nextLine);
			return (offset > region.getOffset() + region.getLength() ? -1 : nextLine);

		} catch (BadLocationException x) {
			// should not happen
			LangUIPlugin.logError("Unexpected error.", x);
		}

		return -1;
	}

	/**
	 * Determines whether each line is prefixed by one of the prefixes.
	 *
	 * @param startLine Start line in document
	 * @param endLine End line in document
	 * @param prefixes Possible comment prefixes
	 * @param document The document
	 * @return <code>true</code> iff each line from <code>startLine</code>
	 *             to and including <code>endLine</code> is prepended by one
	 *             of the <code>prefixes</code>, ignoring whitespace at the
	 *             begin of line
	 */
	protected boolean isBlockCommented(int startLine, int endLine, String[] prefixes, IDocument document) {

		try {

			// check for occurrences of prefixes in the given lines
			for (int i= startLine; i <= endLine; i++) {

				IRegion line= document.getLineInformation(i);
				String text= document.get(line.getOffset(), line.getLength());

				int[] found= TextUtilities.indexOf(prefixes, text, 0);

				if (found[0] == -1)
					// found a line which is not commented
					return false;

				String s= document.get(line.getOffset(), found[0]);
				s= s.trim();
				if (s.length() != 0)
					// found a line which is not commented
					return false;

			}

			return true;

		} catch (BadLocationException x) {
			// should not happen
			LangUIPlugin.logError("Unexpected error.", x);
		}

		return false;
	}
	
}