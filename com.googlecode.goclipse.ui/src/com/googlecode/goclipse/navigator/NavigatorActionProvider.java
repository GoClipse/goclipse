package com.googlecode.goclipse.navigator;

import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionConstants;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;

import com.googlecode.goclipse.SysUtils;

/**
 * Handle the open action on IFileStore elements.
 */
public class NavigatorActionProvider extends CommonActionProvider {

  private class OpenAction extends Action implements ISelectionChangedListener {
    private IWorkbenchPage page;
    private TreeViewer treeViewer;
    
    private Object selectedObject;
    
    public OpenAction(IWorkbenchPage page, TreeViewer treeViewer) {
      this.page = page;
      this.treeViewer = treeViewer;
    }

    @Override
    public void run() {
      IFileStore selectedFile = null;

      if (selectedObject instanceof IFileStore) {
        selectedFile = (IFileStore) selectedObject;
        
        if (selectedFile.fetchInfo().isDirectory()) {
          selectedFile = null;
        }
      }

      if (selectedFile == null) {
        if (oldAction != null) {
          oldAction.run();
        } else {
          expand(selectedObject);
        }
      } else {
        try {
          IDE.openEditorOnFileStore(page, selectedFile);
        } catch (PartInitException exception) {
          SysUtils.displayError("Error Opening File", exception);
        }
      }
    }

    private void expand(Object obj) {
      if (treeViewer != null) {
        treeViewer.setExpandedState(obj, !treeViewer.getExpandedState(obj));
      }
    }

    @Override
    public void selectionChanged(SelectionChangedEvent event) {
      selectedObject = null;

      if (event.getSelection() instanceof IStructuredSelection) {
        selectedObject = ((IStructuredSelection) event.getSelection()).getFirstElement();
      }
    }
  }

  private OpenAction openAction;
  private IAction oldAction;

  public NavigatorActionProvider() {

  }

  @Override
  public void fillActionBars(IActionBars actionBars) {
    super.fillActionBars(actionBars);

    oldAction = actionBars.getGlobalActionHandler(ICommonActionConstants.OPEN);

    actionBars.setGlobalActionHandler(ICommonActionConstants.OPEN, openAction);
  }

  @Override
  public void init(ICommonActionExtensionSite site) {
    super.init(site);

    TreeViewer treeViewer = null;
    
    if (site.getStructuredViewer() instanceof TreeViewer) {
      treeViewer = (TreeViewer)site.getStructuredViewer();
    }
    
    openAction = new OpenAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(), treeViewer);
    
    site.getStructuredViewer().addSelectionChangedListener(openAction);
  }

  @Override
  public void dispose() {
    getActionSite().getStructuredViewer().removeSelectionChangedListener(openAction);

    super.dispose();
  }

}
