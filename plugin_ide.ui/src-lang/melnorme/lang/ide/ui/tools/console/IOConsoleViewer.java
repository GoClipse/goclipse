/*******************************************************************************
 * Copyright (c) 2000, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Bruno Medeiros - LangIDE adaptation.
 *******************************************************************************/
package melnorme.lang.ide.ui.tools.console;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsoleDocumentPartitioner;
import org.eclipse.ui.console.TextConsole;
import org.eclipse.ui.console.TextConsoleViewer;

/**
 * Viewer used to display an IOConsole
 */
public class IOConsoleViewer extends TextConsoleViewer {
	
	/**
	 * will always scroll with output if value is true.
	 */
	private boolean fAutoScroll = true;
	
	private IDocumentListener fDocumentListener;
	
	public IOConsoleViewer(Composite parent, TextConsole console) {
		super(parent, console);
	}
	
	public boolean isAutoScroll() {
		return fAutoScroll;
	}
	
	public void setAutoScroll(boolean scroll) {
		fAutoScroll = scroll;
	}
	
	@Override
	protected void handleVerifyEvent(VerifyEvent e) {
		IDocument doc = getDocument();
		String[] legalLineDelimiters = doc.getLegalLineDelimiters();
		String eventString = e.text;
		try {
			IConsoleDocumentPartitioner partitioner = (IConsoleDocumentPartitioner) doc.getDocumentPartitioner();
			if (!partitioner.isReadOnly(e.start)) {
				boolean isCarriageReturn = false;
				for (int i = 0; i < legalLineDelimiters.length; i++) {
					if (e.text.equals(legalLineDelimiters[i])) {
						isCarriageReturn = true;
						break;
					}
				}
				
				if (!isCarriageReturn) {
					super.handleVerifyEvent(e);
					return;
				}
			}
			
			int length = doc.getLength();
			if (e.start == length) {
				super.handleVerifyEvent(e);
			} else {
				try {
					doc.replace(length, 0, eventString);
				} catch (BadLocationException e1) {
				}
				e.doit = false;
			}
		} finally {
			StyledText text = (StyledText) e.widget;
			text.setCaretOffset(text.getCharCount());
		}
	}
	
	/**
	 * makes the associated text widget uneditable.
	 */
	 public void setReadOnly() {
		ConsolePlugin.getStandardDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				StyledText text = getTextWidget();
				if (text != null && !text.isDisposed()) {
					text.setEditable(false);
				}
			}
		});
	}
	
	/**
	 * @return <code>false</code> if text is editable
	 */
	 public boolean isReadOnly() {
		 return !getTextWidget().getEditable();
	 }
	 
	 @Override
	 public void setDocument(IDocument document) {
		 IDocument oldDocument= getDocument();
		 
		 super.setDocument(document);
		 
		 if (oldDocument != null) {
			 oldDocument.removeDocumentListener(getDocumentListener());
		 }
		 if (document != null) {
			 document.addDocumentListener(getDocumentListener());
		 }
	 }
	 
	 private IDocumentListener getDocumentListener() {
		 if (fDocumentListener == null) {
			 fDocumentListener= new IDocumentListener() {
				 @Override
				 public void documentAboutToBeChanged(DocumentEvent event) {
				 }
				 
				 @Override
				 public void documentChanged(DocumentEvent event) {
					 if (fAutoScroll) {
						 revealEndOfDocument();
					 }
				 }
			 };
		 }
		 return fDocumentListener;
	 }
}
