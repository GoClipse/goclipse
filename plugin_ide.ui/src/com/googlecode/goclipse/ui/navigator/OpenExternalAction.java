package com.googlecode.goclipse.ui.navigator;

import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.googlecode.goclipse.Activator;

public class OpenExternalAction extends Action implements ISelectionChangedListener {

	private TreeViewer treeViewer;
	private IWorkbenchPage page;
	private Object selectedObject;

	public OpenExternalAction(TreeViewer treeViewer) {
		this.treeViewer = treeViewer;
		page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	}

	@Override
	public void run() {
		if (selectedObject instanceof IProject || 
				selectedObject instanceof IGoSourceContainer) {
			if (treeViewer != null) {
				treeViewer.setExpandedState(selectedObject,
						!treeViewer.getExpandedState(selectedObject));
			}
		} else if (selectedObject instanceof IFileStore) {
			IFileStore selectedFile = (IFileStore) selectedObject;
			if (!selectedFile.fetchInfo().isDirectory()) {
				try {
					IDE.openEditorOnFileStore(page, selectedFile);
				} catch (PartInitException e) {
					Activator.logError(e);	
				}
			}
		}
	}
	
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		ISelection selection = event.getSelection();
		if (selection instanceof IStructuredSelection) {
			selectedObject = ((IStructuredSelection) selection).getFirstElement();
		} else {
			selectedObject = null;
		}
	}

}
