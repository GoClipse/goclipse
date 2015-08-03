/*******************************************************************************
 * Copyright (c) 2007, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.templates;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.templates.TemplateContextType;

import _org.eclipse.jdt.internal.ui.text.template.contentassist.TemplateEngine;
import melnorme.lang.ide.ui.EditorSettings_Actual;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.editor.actions.SourceOperationContext;
import melnorme.lang.ide.ui.text.completion.AbstractCompletionProposalComputer;
import melnorme.lang.tooling.ops.OperationSoftFailure;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.collections.ListView;
import melnorme.utilbox.core.CommonException;


/**
 * An template completion proposal computer can generate template completion proposals
 * from a given TemplateEngine.
 *
 * @since 3.4
 */
public class LangTemplateCompletionProposalComputer extends AbstractCompletionProposalComputer {
	
	/**
	 * The engine for the current session, if any
	 */
	protected TemplateEngine fEngine;
	
	@Override
	public void sessionStarted() {
	}
	
	@Override
	public void sessionEnded() {
		if (fEngine != null) {
			fEngine.reset();
			fEngine= null;
		}
	}
	
	@Override
	protected Indexable<ICompletionProposal> doComputeCompletionProposals(SourceOperationContext context, int offset)
			throws CoreException, CommonException, OperationSoftFailure {
		
		fEngine= computeCompletionEngine(context);
		if (fEngine == null)
			return null;
		
		fEngine.reset();
		List<ICompletionProposal> result= fEngine.completeAndReturnResults(context);
		
//		IJavaCompletionProposal[] keyWordResults= javaContext.getKeywordProposals();
//		if (keyWordResults.length == 0)
//			return result;
//
//		/* Update relevance of template proposals that match with a keyword
//		 * give those templates slightly more relevance than the keyword to
//		 * sort them first.
//		 */
//		for (int k= 0; k < templateProposals.length; k++) {
//			TemplateProposal curr= templateProposals[k];
//			String name= curr.getTemplate().getPattern();
//			for (int i= 0; i < keyWordResults.length; i++) {
//				String keyword= keyWordResults[i].getDisplayString();
//				if (name.startsWith(keyword)) {
//					String content= curr.getTemplate().getPattern();
//					if (content.startsWith(keyword)) {
//						curr.setRelevance(keyWordResults[i].getRelevance() + 1);
//						break;
//					}
//				}
//			}
//		}
		return new ListView<>(result);
	}
	
	protected String getContextTypeId() {
		return EditorSettings_Actual.TEMPLATE_CONTEXT_TYPE_ID;
	}
	
	@SuppressWarnings("unused")
	protected TemplateEngine computeCompletionEngine(SourceOperationContext context) {
		TemplateContextType contextType = LangUIPlugin.getTemplateRegistry().getContextType(getContextTypeId());
		return new TemplateEngine(contextType);
	}
	
}