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


import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.utils.WorkbenchUtils;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension4;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension6;
import org.eclipse.jface.text.templates.DocumentTemplateContext;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateException;
import org.eclipse.jface.text.templates.TemplateProposal;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.IWorkbenchPartOrientation;

public class LangTemplateProposal extends TemplateProposal implements 
	ICompletionProposalExtension4, ICompletionProposalExtension6 
{
	
	public LangTemplateProposal(Template template, TemplateContext context, IRegion region, Image image) {
		super(template, context, region, image);
	}
	
	public LangTemplateProposal(Template template, TemplateContext context, IRegion region, Image image, int relevance) {
		super(template, context, region, image, relevance);
	}
	
	
	@Override
	public String getDisplayString() {
		return getStyledDisplayString().getString();
	}
	
	protected StyledString styledDisplayString;
	
	@Override
	public StyledString getStyledDisplayString() {
		if(styledDisplayString == null) {
			Template template = getTemplate();
			StyledString styledString;
			styledString = new StyledString(template.getName(), StyledString.COUNTER_STYLER);
			styledString.append(new StyledString(" - " + template.getDescription(), StyledString.QUALIFIER_STYLER));
			styledDisplayString = styledString;
		}
		return styledDisplayString;
	}
	
	@Override
	public String getAdditionalProposalInfo() {
		if (getContext() instanceof LangContext) {
			LangContext context = (LangContext) getContext();
			
			try {
				return context.evaluate(getTemplate(), false).getString();
			} catch (BadLocationException | TemplateException e) {
				LangCore.logError("Error evaluating template", e);
			}
		}
		
		return getTemplate().getPattern();
	}
	
	@Override
	public String toString() {
		return getDisplayString();
	}
	
	/* ----------------- JDT copied stuff: ----------------- */
	
	@Override
	public IInformationControlCreator getInformationControlCreator() {
		int orientation;
		IEditorPart editor = WorkbenchUtils.getActiveEditor();
		if (editor instanceof IWorkbenchPartOrientation) {
			orientation = ((IWorkbenchPartOrientation)editor).getOrientation();
		} else {
			orientation = SWT.LEFT_TO_RIGHT;
		}
		return new TemplateInformationControlCreator(orientation);
	}
	
	@Override
	public boolean isAutoInsertable() {
		if (isSelectionTemplate())
			return false;
		return getTemplate().isAutoInsertable();
	}

	/**
	 * Returns <code>true</code> if the proposal has a selection, e.g. will wrap some code.
	 *
	 * @return <code>true</code> if the proposals completion length is non zero
	 * @since 3.2
	 */
	private boolean isSelectionTemplate() {
		if (getContext() instanceof DocumentTemplateContext) {
			DocumentTemplateContext ctx= (DocumentTemplateContext) getContext();
			if (ctx.getCompletionLength() > 0)
				return true;
		}
		return false;
	}
	
}