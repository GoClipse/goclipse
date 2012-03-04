package com.googlecode.goclipse.navigator;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.ide.ResourceUtil;
import org.eclipse.ui.navigator.ILinkHelper;
import org.eclipse.ui.part.FileEditorInput;

import java.net.URI;

/**
 * Link IFileStore objects in editors to the selection in the Project Explorer.
 * 
 * @author devoncarew
 */
public class NavigatorLinkHelper implements ILinkHelper {

  public NavigatorLinkHelper() {

  }

  @Override
  public void activateEditor(IWorkbenchPage page, IStructuredSelection selection) {
    if (selection == null || selection.isEmpty()) {
      return;
    }
    
    Object element = selection.getFirstElement();
    IEditorInput input = null;
    
    if (element instanceof IEditorInput) {
      input = (IEditorInput)element;
    } else if (element instanceof IFile) {
      input = new FileEditorInput((IFile)element);
    } else if (element instanceof IFileStore) {
      input = new FileStoreEditorInput((IFileStore)element);
    }
    
    if (input != null) {
      IEditorPart part = page.findEditor(input);
      
      if (part != null) {
        page.bringToTop(part);
      }
    }
  }

  @Override
  public IStructuredSelection findSelection(IEditorInput input) {
    IFile file = ResourceUtil.getFile(input);

    if (file != null) {
      return new StructuredSelection(file);
    }

    IFileStore fileStore = (IFileStore) input.getAdapter(IFileStore.class);

    if (fileStore == null && input instanceof FileStoreEditorInput) {
      URI uri = ((FileStoreEditorInput)input).getURI();
      
      try {
        fileStore = EFS.getStore(uri);
      } catch (CoreException e) {

      }
    }
    
    if (fileStore != null) {
      return new StructuredSelection(fileStore);
    }

    return StructuredSelection.EMPTY;
  }

}
