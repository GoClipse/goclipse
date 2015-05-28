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
package _org.eclipse.jdt.internal.ui.text;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.text.source.DefaultAnnotationHover;

/**
 * Determines all markers for the given line and collects, concatenates, and formats
 * returns their messages in HTML.
 *
 * @since 3.2
 */
public class HTMLAnnotationHover extends DefaultAnnotationHover {

	/**
	 * Creates a new HTML annotation hover.
	 *
	 * @param showLineNumber <code>true</code> if the line number should be shown when no annotation is found
	 * @since 3.4
	 */
	public HTMLAnnotationHover(boolean showLineNumber) {
		super(showLineNumber);
	}

	/*
	 * Formats a message as HTML text.
	 */
	@Override
	protected String formatSingleMessage(String message) {
		StringBuffer buffer= new StringBuffer();
		HTMLPrinter.addPageProlog(buffer);
		HTMLPrinter.addParagraph(buffer, HTMLPrinter.convertToHTMLContent(message));
		HTMLPrinter.addPageEpilog(buffer);
		return buffer.toString();
	}

	/*
	 * Formats several message as HTML text.
	 */
	@Override
	protected String formatMultipleMessages(List messages) {
		StringBuffer buffer= new StringBuffer();
		HTMLPrinter.addPageProlog(buffer);
		HTMLPrinter.addParagraph(buffer, HTMLPrinter.convertToHTMLContent(JavaUIMessages.JavaAnnotationHover_multipleMarkersAtThisLine));

		HTMLPrinter.startBulletList(buffer);
		Iterator<?> e= messages.iterator();
		while (e.hasNext())
			HTMLPrinter.addBullet(buffer, HTMLPrinter.convertToHTMLContent((String) e.next()));
		HTMLPrinter.endBulletList(buffer);

		HTMLPrinter.addPageEpilog(buffer);
		return buffer.toString();
	}
}

class JavaUIMessages {

	public static final String JavaAnnotationHover_multipleMarkersAtThisLine = 
			"Multiple markers at this line";
	
}