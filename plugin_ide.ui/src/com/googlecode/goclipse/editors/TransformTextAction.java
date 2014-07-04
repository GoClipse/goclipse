package com.googlecode.goclipse.editors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

import com.googlecode.goclipse.ui.util.ErrorDialogUtil;

/**
 * An action used to modify the contents of the current editor.
 */
abstract class TransformTextAction implements IEditorActionDelegate {
	protected String name;
	protected GoEditor editor;
	
	public TransformTextAction(String actionName) {
		this.name = actionName;
	}
	
	@Override
	public final void selectionChanged(IAction action, ISelection selection) {
		
	}

	@Override
    public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		if (targetEditor instanceof GoEditor) {
			editor = (GoEditor)targetEditor;
		} else {
			editor = null;
		}
		
		action.setEnabled(editor != null);
	}
	
	@Override
	public final void run(IAction action) {
		if (editor != null) {
			String currentContent = editor.getText();
			
			try {
				String newText = transformText(currentContent);
				
				if (newText != null) {
					editor.replaceText(newText);
				}
			} catch (CoreException ce) {
				ErrorDialogUtil.displayError(editor.getSite().getShell(), "Error Running " + name, ce.getMessage());
			}
		}
	}

	protected abstract String transformText(String text) throws CoreException;
	
}
