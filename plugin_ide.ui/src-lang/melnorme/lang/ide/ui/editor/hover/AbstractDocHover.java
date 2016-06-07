/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.editor.hover;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.texteditor.ITextEditor;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.editor.AbstractLangEditor;
import melnorme.lang.ide.ui.text.DocumentationHoverCreator;
import melnorme.lang.ide.ui.utils.operations.CalculateValueUIOperation;
import melnorme.lang.ide.ui.utils.operations.RunnableWithProgressOperationAdapter.ProgressMonitorDialogOpRunner;
import melnorme.lang.ide.ui.utils.operations.UIOperation;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.StringUtil;

public abstract class AbstractDocHover implements ILangEditorTextHover<String> {
	
	protected final DocumentationHoverCreator hoverCreator = new DocumentationHoverCreator(); 
	
	@Override
	public IInformationControlCreator getHoverControlCreator() {
		return hoverCreator.getHoverControlCreator();
	}
	
	@Override
	public IInformationControlCreator getInformationPresenterControlCreator() {
		return hoverCreator.getInformationPresenterControlCreator();
	}
	
	protected boolean requiresSavedEditor() {
		return true;
	}
	
	@Override
	public String getHoverInfo(AbstractLangEditor editor, IRegion hoverRegion, boolean canSaveEditor) {
		assertNotNull(editor);
		
		if(requiresSavedEditor() && editor.isDirty()) {
			
			if(!canSaveEditor) {
				return null;
			}
			
			Shell shell = editor.getSourceViewer_().getTextWidget().getShell();
			UIOperation op = new UIOperation("Saving editor for hover information", editor::saveWithoutSaveActions2) {
				@Override
				protected void executeBackgroundOperation() throws CommonException, OperationCancellation {
					new ProgressMonitorDialogOpRunner(shell, editor::saveWithoutSaveActions2) {{ 
						fork = false; 
					}}.execute();
				}
			};
			boolean success = op.executeAndHandle();
			if(!success) {
				return null;
			}
			
		}
		
		return getHoverInfo(editor, hoverRegion);
	}
	
	@Override
	public String getHoverInfo(AbstractLangEditor editor, IRegion hoverRegion) {
		
		try {
			int offset = hoverRegion.getOffset();
			String rawDocumentation = getRawDocumentation(editor, offset);
			
			if(rawDocumentation == null) {
				return null;
			}
			
			return HTMLEscapeUtil.escapeToToHTML(rawDocumentation);
		} catch(CommonException ce) {
			LangCore.logStatusException(ce.toStatusException());
			// TODO: we could add a nicer HTML formatting:
			return "<b>Error:</b> " + ce.getMessage() + StringUtil.asString(" ", ce.getCause());
		}
	}
	
	protected String getRawDocumentation(ITextEditor editor, int offset) throws CommonException {
		CalculateValueUIOperation<String> op = getOpenDocumentationOperation(editor, offset);
		return op.executeAndGetValidatedResult();
	}
	
	protected abstract CalculateValueUIOperation<String> getOpenDocumentationOperation(ITextEditor editor, int offset);
	
	
	public static class HTMLEscapeUtil {
		
		public static String escapeToToHTML(String string) {
			String content = string;
			content = content.replace("&", "&amp;");
			content = content.replace("\"", "&quot;");
			content = content.replace("<", "&lt;");
			content = content.replace(">", "&gt;");
			content = content.replace("\n", "<br/>");
			return content;
		}
		
	}
}