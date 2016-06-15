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
package _org.eclipse.jdt.internal.ui.text.template.contentassist;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.templates.GlobalTemplateVariables;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.TemplateProposal;
import org.eclipse.swt.graphics.Image;

import _org.eclipse.jdt.internal.corext.template.java.CompilationUnitContext;
import _org.eclipse.jdt.internal.corext.template.java.CompilationUnitContextType;
import melnorme.lang.ide.core.ISourceFile;
import melnorme.lang.ide.ui.LangElementImages;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.templates.LangTemplateProposal;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.lang.tooling.toolchain.ops.SourceOpContext;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.CollectionUtil;
import melnorme.utilbox.misc.Location;


public class TemplateEngine {

	protected static final String $_LINE_SELECTION= "${" + GlobalTemplateVariables.LineSelection.NAME + "}";
	protected static final String $_WORD_SELECTION= "${" + GlobalTemplateVariables.WordSelection.NAME + "}";

	protected TemplateContextType fContextType;
	protected ArrayList2<TemplateProposal> fProposals= new ArrayList2<>();
	/** Positions created on the key documents to remove in reset. */
	protected final Map<IDocument, Position> fPositions= new HashMap<IDocument, Position>();

	/**
	 * Creates the template engine for the given <code>contextType</code>.
	 * <p>
	 * The <code>JavaPlugin.getDefault().getTemplateContextRegistry()</code>
	 * defines the supported context types.</p>
	 *
	 * @param contextType the context type
	 */
	public TemplateEngine(TemplateContextType contextType) {
		Assert.isNotNull(contextType);
		fContextType= contextType;
	}

	/**
	 * Empties the collector.
	 */
	public void reset() {
		fProposals.clear();
		
		for (Entry<IDocument, Position> entry : fPositions.entrySet()) {
			IDocument doc= entry.getKey();
			Position position= entry.getValue();
			doc.removePosition(position);
		}
		fPositions.clear();
	}

	/**
	 * Returns the array of matching templates.
	 *
	 * @return the template proposals
	 */
	public TemplateProposal[] getResults() {
		return fProposals.toArray(new TemplateProposal[fProposals.size()]);
	}

	public List<ICompletionProposal> completeAndReturnResults(SourceOpContext sourceOpContext, IDocument document) 
			throws CommonException {
		complete(sourceOpContext, document);
		
		TemplateProposal[] templateProposals = getResults();
		return CollectionUtil.<ICompletionProposal>createArrayList(templateProposals);
	}

	public void complete(SourceOpContext sourceOpContext, IDocument document) throws CommonException {
		
		final Location sourceFileLoc = sourceOpContext.getFileLocation();
		
		if (!(fContextType instanceof CompilationUnitContextType))
			return;
		CompilationUnitContextType compilationUnitContextType = (CompilationUnitContextType) fContextType;

		SourceRange selection = sourceOpContext.getSelection();
		Position position= new Position(selection.getStartPos(), selection.getLength());

		// remember selected text
		String selectedText= null;
		if (selection.getLength() != 0) {
			try {
				selectedText= document.get(selection.getStartPos(), selection.getLength());
				document.addPosition(position);
				fPositions.put(document, position);
			} catch (BadLocationException e) {}
		}

		ISourceFile compilationUnit = new ISourceFile() {
			@Override
			public Location getLocation() {
				return sourceFileLoc;
			}
		};
		CompilationUnitContext context= compilationUnitContextType.createContext(document, position, compilationUnit);
		context.setVariable("selection", selectedText); //$NON-NLS-1$
		int start= context.getStart();
		int end= context.getEnd();
		IRegion region= new Region(start, end - start);

		Template[] templates = getTemplates();

		if (selection.getLength() == 0) {
			for (Template template : templates) {
				if (context.canEvaluate(template)) {
					fProposals.add(new LangTemplateProposal(template, context, region, getImage(), 0));
				}
			}
		} else {

			if (context.getKey().length() == 0)
				context.setForceEvaluation(true);

			boolean multipleLinesSelected= areMultipleLinesSelected(document, selection);

			for (Template template : templates) {
				if (context.canEvaluate(template) &&
					(!multipleLinesSelected && template.getPattern().indexOf($_WORD_SELECTION) != -1 
					|| (multipleLinesSelected && template.getPattern().indexOf($_LINE_SELECTION) != -1)))
				{
					fProposals.add(new LangTemplateProposal(template, context, region, getImage(), 0));
				}
			}
		}
	}
	
	protected Template[] getTemplates() {
		return LangUIPlugin.getTemplateRegistry().getTemplateStore().getTemplates();
	}
	
	protected Image getImage() {
		return LangElementImages.CA_SNIPPET.getImage();
	}
	
	/**
	 * Returns <code>true</code> if one line is completely selected or if multiple lines are selected.
	 * Being completely selected means that all characters except the new line characters are
	 * selected.
	 *
	 * @param viewer the text viewer
	 * @return <code>true</code> if one or multiple lines are selected
	 * @since 2.1
	 */
	protected boolean areMultipleLinesSelected(IDocument document, SourceRange s) {
		if (s.getLength() == 0)
			return false;

		try {
			
			int startLine= document.getLineOfOffset(s.getOffset());
			int endLine= document.getLineOfOffset(s.getEndPos());
			IRegion line= document.getLineInformation(startLine);
			return startLine != endLine || (s.getOffset() == line.getOffset() && s.getLength() == line.getLength());
		} catch (BadLocationException x) {
			return false;
		}
	}
	
}