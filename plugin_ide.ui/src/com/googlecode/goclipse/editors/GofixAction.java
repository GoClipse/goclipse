package com.googlecode.goclipse.editors;

import com.googlecode.goclipse.ui.editor.actions.RunGoFixOperation;

/**
 * An action to run the gofix command.
 */
@Deprecated
public class GofixAction extends TransformTextAction {
	
	public GofixAction() {
		super("Gofix");
	}
	
	@Override
	protected void doRun(GoEditor editor) {
		new RunGoFixOperation(editor).executeAndHandle();
	}

}