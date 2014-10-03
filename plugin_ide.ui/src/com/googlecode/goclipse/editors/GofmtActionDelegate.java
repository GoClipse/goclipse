package com.googlecode.goclipse.editors;

import com.googlecode.goclipse.ui.editor.actions.RunGoFmtOperation;

/**
 * 
 * @author steel
 *
 */
@Deprecated
public class GofmtActionDelegate extends TransformTextAction {
	
	public GofmtActionDelegate() {
		super("gofmt");
	}
	
	@Override
	protected void doRun(GoEditor editor) {
		new RunGoFmtOperation(editor).executeAndHandle();
	}
	
}