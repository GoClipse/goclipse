/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package _org.eclipse.jdt.internal.corext.template.java;

import melnorme.lang.ide.core.ISourceFile;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.templates.DocumentTemplateContext;
import org.eclipse.jface.text.templates.TemplateContextType;


/**
 * A compilation unit context.
 */
public abstract class CompilationUnitContext extends DocumentTemplateContext {
	
	/** The compilation unit, may be <code>null</code>. */
	private final ISourceFile fCompilationUnit;
	/** A flag to force evaluation in head-less mode. */
	protected boolean fForceEvaluation;
//	/** A global state for proposals that change if a master proposal changes. */
//	protected MultiVariableGuess fMultiVariableGuess;
	/** <code>true</code> if the context has a managed (i.e. added to the document) position, 
	 * <code>false</code> otherwise. */
	protected final boolean fIsManaged;
	
	/**
	 * Creates a compilation unit context.
	 *
	 * @param type   the context type
	 * @param document the document
	 * @param completionOffset the completion position within the document
	 * @param completionLength the completion length within the document
	 * @param compilationUnit the compilation unit (may be <code>null</code>)
	 */
	protected CompilationUnitContext(TemplateContextType type, IDocument document, int completionOffset,
			int completionLength, ISourceFile compilationUnit) {
		super(type, document, completionOffset, completionLength);
		fCompilationUnit= compilationUnit;
		fIsManaged= false;
	}
	
	/**
	 * Creates a compilation unit context.
	 *
	 * @param type   the context type
	 * @param document the document
	 * @param completionPosition the position defining the completion offset and length
	 * @param compilationUnit the compilation unit (may be <code>null</code>)
	 * @since 3.2
	 */
	protected CompilationUnitContext(TemplateContextType type, IDocument document, Position completionPosition,
			ISourceFile compilationUnit) {
		super(type, document, completionPosition);
		fCompilationUnit= compilationUnit;
		fIsManaged= true;
	}
	
	/**
	 * Returns the compilation unit if one is associated with this context,
	 * <code>null</code> otherwise.
	 *
	 * @return the compilation unit of this context or <code>null</code>
	 */
	public final ISourceFile getCompilationUnit() {
		return fCompilationUnit;
	}
	
	/**
	 * Sets whether evaluation is forced or not.
	 *
	 * @param evaluate <code>true</code> in order to force evaluation,
	 *            <code>false</code> otherwise
	 */
	public void setForceEvaluation(boolean evaluate) {
		fForceEvaluation= evaluate;
	}
	
//	/**
//	 * Returns the multi-variable guess.
//	 *
//	 * @return the multi-variable guess
//	 */
//	public MultiVariableGuess getMultiVariableGuess() {
//		return fMultiVariableGuess;
//	}
//	
//	/**
//	 * @param multiVariableGuess The multiVariableGuess to set.
//	 */
//	void setMultiVariableGuess(MultiVariableGuess multiVariableGuess) {
//		fMultiVariableGuess= multiVariableGuess;
//	}
	
}