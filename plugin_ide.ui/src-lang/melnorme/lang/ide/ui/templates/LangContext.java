/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.templates;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.util.Collections;

import melnorme.lang.ide.core.ISourceFile;
import melnorme.lang.ide.core.text.DocumentModification;
import melnorme.lang.ide.ui.text.util.AutoEditUtils;
import melnorme.lang.utils.parse.StringParseSource;
import melnorme.utilbox.collections.ArrayList2;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.TextUtilities;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateBuffer;
import org.eclipse.jface.text.templates.TemplateException;
import org.eclipse.jface.text.templates.TemplateTranslator;
import org.eclipse.text.edits.MalformedTreeException;

import _org.eclipse.jdt.internal.corext.template.java.JavaContext;
import _org.eclipse.jdt.internal.corext.template.java.JavaFormatter;

public class LangContext extends JavaContext {
	
	public LangContext(LangTemplateContextType type, IDocument document, int completionOffset,
			int completionLength, ISourceFile compilationUnit) {
		super(type, document, completionOffset, completionLength, compilationUnit);
	}
	
	public LangContext(LangTemplateContextType type, IDocument document, Position completionPosition,
			ISourceFile compilationUnit) {
		super(type, document, completionPosition, compilationUnit);
	}
	
	@Override
	public TemplateBuffer evaluate(Template template) throws BadLocationException, TemplateException {
		return evaluate(template, true);
	}
	
	public TemplateBuffer evaluate(Template template, boolean fixIndentation) 
			throws BadLocationException, TemplateException {
		if (!canEvaluate(template))
			return null;
		
		TemplateTranslator translator= new TemplateTranslator();
		
		String pattern = template.getPattern();
//		if(fixIndentation) {
//			pattern = fixIndentation(pattern);
//		}
		TemplateBuffer buffer = translator.translate(pattern);
		
		getContextType().resolve(buffer, this);
		
		if(fixIndentation) {
			String delimiter = TextUtilities.getDefaultLineDelimiter(getDocument());
			JavaFormatter formatter = new JavaFormatter(delimiter) {
				@Override
				protected void indent(IDocument document) throws BadLocationException, MalformedTreeException {
					simpleIndent(document);
				}
			};
			formatter.format(buffer, this);
		}
		
		return buffer;
	}
	
	// Alternative method to fix indentation, not used currently.
	protected String fixIndentation(String docString) throws BadLocationException {
		
		final String delimeter = "\n";
		final String indent = AutoEditUtils.getLineIndentOfOffset(getDocument(), getStart());
		
		
		StringBuilder newContents = new StringBuilder();
		
		StringParseSource parser = new StringParseSource(docString);
		String start = parser.consumeUntil(delimeter);
		newContents.append(start);
		
		while(true) {
			if(!parser.tryConsume(delimeter)) {
				assertTrue(parser.lookaheadIsEOF());
				break;
			}
			newContents.append(delimeter);
			newContents.append(indent);
			
			String line = parser.consumeUntil(delimeter);
			newContents.append(line);
		}
		
		return newContents.toString();
	}
	
	/**
	 * This method is better than {@link #fixIndentation(String)} since it updates variable positions,
	 * as such, it can be used after the template pattern has been evaluate. 
	 */
	protected void simpleIndent(IDocument document) throws BadLocationException {
		
		final String delimeter = "\n";
		final String indent = AutoEditUtils.getLineIndentOfOffset(getDocument(), getStart());
		
		ArrayList2<DocumentModification> changes = new ArrayList2<>();
		
		StringParseSource parser = new StringParseSource(document.get());
		parser.consumeUntil(delimeter);
		while(true) {
			if(!parser.tryConsume(delimeter)) {
				assertTrue(parser.lookaheadIsEOF());
				break;
			}
			
			changes.add(new DocumentModification(parser.getOffset(), 0, indent));
			
			parser.consumeUntil(delimeter);
		}
		
		// Need to reverse so that the offset of each change remains meaningul.
		Collections.reverse(changes);
		
		for (DocumentModification docMod : changes) {
			docMod.apply(document);
		}
		
	}
	
}