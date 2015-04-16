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


import melnorme.lang.ide.core.ISourceFile;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.templates.GlobalTemplateVariables;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.TemplateException;
import org.eclipse.jface.text.templates.TemplateVariable;

import _org.eclipse.jdt.internal.corext.template.java.CompilationUnitContext;
import _org.eclipse.jdt.internal.corext.template.java.JavaTemplateMessages;

public class LangTemplateContextType extends TemplateContextType {
	
	public LangTemplateContextType() {
		super();
		
		setupResolvers();
	}
	
	public LangTemplateContextType(String id) {
		super(id);
		
		setupResolvers();
	}

	protected void setupResolvers() {
		addResolver(new GlobalTemplateVariables.Cursor());
		addResolver(new GlobalTemplateVariables.WordSelection());
		addResolver(new GlobalTemplateVariables.LineSelection());
		addResolver(new GlobalTemplateVariables.Dollar());
		addResolver(new GlobalTemplateVariables.Date());
		addResolver(new GlobalTemplateVariables.Year());
		addResolver(new GlobalTemplateVariables.Time());
		addResolver(new GlobalTemplateVariables.User());
		
		addAdditionalResolvers();
	}
	
	protected void addAdditionalResolvers() {
	}
	
	/* -----------------  ----------------- */
	
	
	@Override
	protected void validateVariables(TemplateVariable[] variables) throws TemplateException {
		// check for multiple cursor variables
		for(int i = 0; i < variables.length; i++) {
			TemplateVariable var = variables[i];
			if(var.getType().equals(GlobalTemplateVariables.Cursor.NAME)) {
				if(var.getOffsets().length > 1) {
					throw new TemplateException(JavaTemplateMessages.ContextType_error_multiple_cursor_variables);
				}
			}
		}
	}
	
	public CompilationUnitContext createContext(IDocument document, int offset, int length, 
			ISourceFile compilationUnit) {
		LangContext javaContext = new LangContext(this, document, offset, length, compilationUnit);
		initializeContext(javaContext);
		return javaContext;
	}
	
	public CompilationUnitContext createContext(IDocument document, Position completionPosition, 
			ISourceFile compilationUnit) {
		LangContext javaContext = new LangContext(this, document, completionPosition, compilationUnit);
		initializeContext(javaContext);
		return javaContext;
	}
	
	@SuppressWarnings("unused")
	protected void initializeContext(LangContext context) {
	}
	
}