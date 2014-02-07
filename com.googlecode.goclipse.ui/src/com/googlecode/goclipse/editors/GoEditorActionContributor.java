package com.googlecode.goclipse.editors;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ide.IDEActionFactory;
import org.eclipse.ui.texteditor.BasicTextEditorActionContributor;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;

public class GoEditorActionContributor extends BasicTextEditorActionContributor {

  public GoEditorActionContributor() {

  }

  @Override
  public void setActiveEditor(IEditorPart part) {
    super.setActiveEditor(part);

    ITextEditor textEditor = null;

    if (part instanceof ITextEditor) {
      textEditor = (ITextEditor) part;
    }

    IActionBars bars = getActionBars();

    bars.setGlobalActionHandler("com.googlecode.goclipse.actions.ToggleComment",
        getAction(textEditor, "ToggleComment"));

    bars.setGlobalActionHandler("com.googlecode.goclipse.actions.ShiftRight",
        getAction(textEditor, "ShiftRight"));
    bars.setGlobalActionHandler("com.googlecode.goclipse.actions.ShiftLeft",
        getAction(textEditor, "ShiftLeft"));

    bars.setGlobalActionHandler(IDEActionFactory.OPEN_PROJECT.getId(),
        getAction(textEditor, IDEActionFactory.OPEN_PROJECT.getId()));
    bars.setGlobalActionHandler(IDEActionFactory.CLOSE_PROJECT.getId(),
        getAction(textEditor, IDEActionFactory.CLOSE_PROJECT.getId()));

    IAction action = getAction(textEditor, ITextEditorActionConstants.REFRESH);
    bars.setGlobalActionHandler(ITextEditorActionConstants.REFRESH, action);
  }

}
