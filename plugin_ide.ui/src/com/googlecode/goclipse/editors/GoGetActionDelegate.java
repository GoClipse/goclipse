package com.googlecode.goclipse.editors;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

import com.googlecode.goclipse.ui.editor.actions.RunGoGetOperation;

/**
 * 
 */
@Deprecated
final public class GoGetActionDelegate implements IEditorActionDelegate {
	
	protected String	name;
	protected GoEditor	editor;
	
	@Override
	public final void selectionChanged(IAction action, ISelection selection) {
		
	}
	
	@Override
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		
		if (targetEditor instanceof GoEditor) {
			editor = (GoEditor) targetEditor;
		} else {
			editor = null;
		}
		
		action.setEnabled(editor != null);
	}
	
	@Override
	public final void run(IAction action) {
		if (editor != null) {
			new RunGoGetOperation(editor).executeAndHandle();
		}
	}
}