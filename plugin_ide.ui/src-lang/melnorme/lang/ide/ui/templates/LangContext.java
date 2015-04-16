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
import melnorme.lang.ide.core.ISourceFile;
import melnorme.lang.ide.ui.text.util.AutoEditUtils;
import melnorme.lang.utils.SimpleLexingHelper;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateBuffer;
import org.eclipse.jface.text.templates.TemplateException;
import org.eclipse.jface.text.templates.TemplateTranslator;

import _org.eclipse.jdt.internal.corext.template.java.JavaContext;

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
		if (!canEvaluate(template))
			return null;
		
		TemplateTranslator translator= new TemplateTranslator();
		String pattern = fixIndentation(template.getPattern());
		TemplateBuffer buffer = translator.translate(pattern);
		
		getContextType().resolve(buffer, this);
		
		return buffer;
	}
	
	protected String fixIndentation(String docString) throws BadLocationException {
		
		final String delimeter = "\n";
		final String indent = AutoEditUtils.getLineIndentOfOffset(getDocument(), getStart());
		
		
		StringBuilder newContents = new StringBuilder();
		
		SimpleLexingHelper parser = new SimpleLexingHelper(docString);
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
	
}