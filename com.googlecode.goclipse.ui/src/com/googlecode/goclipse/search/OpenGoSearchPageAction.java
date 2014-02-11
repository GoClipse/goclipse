package com.googlecode.goclipse.search;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

import com.googlecode.goclipse.Activator;

/**
 * Opens the Search Dialog and brings the Go search page to front.
 */
public class OpenGoSearchPageAction implements IWorkbenchWindowActionDelegate {
  private IWorkbenchWindow fWindow;

  public OpenGoSearchPageAction() {

  }

  @Override
  public void dispose() {
    fWindow = null;
  }

  @Override
  public void init(IWorkbenchWindow window) {
    fWindow = window;
  }

  @Override
  public void run(IAction action) {
    if (fWindow == null || fWindow.getActivePage() == null) {
      beep();
      Activator.logError("Could not open the search dialog - for some reason the window handle was null");
    } else {
      NewSearchUI.openSearchDialog(fWindow, GoSearchPage.EXTENSION_POINT_ID);
    }
  }

  @Override
  public void selectionChanged(IAction action, ISelection selection) {
    // do nothing since the action isn't selection dependent.

  }

  private void beep() {
    Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

    if (shell != null && shell.getDisplay() != null) {
      shell.getDisplay().beep();
    }
  }

}
