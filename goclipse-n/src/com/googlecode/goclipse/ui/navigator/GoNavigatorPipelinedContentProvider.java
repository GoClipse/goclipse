package com.googlecode.goclipse.ui.navigator;

import java.util.Set;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.IPipelinedTreeContentProvider;
import org.eclipse.ui.navigator.PipelinedShapeModification;
import org.eclipse.ui.navigator.PipelinedViewerUpdate;

/**
 * 
 * @author Mihailo Vasiljevic
 * 
 */
public class GoNavigatorPipelinedContentProvider implements
		IPipelinedTreeContentProvider {

	@Override
	public void getPipelinedChildren(Object aParent, Set theCurrentChildren) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getPipelinedElements(Object anInput, Set theCurrentElements) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getPipelinedParent(Object anObject, Object aSuggestedParent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PipelinedShapeModification interceptAdd(
			PipelinedShapeModification anAddModification) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean interceptRefresh(
			PipelinedViewerUpdate aRefreshSynchronization) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public PipelinedShapeModification interceptRemove(
			PipelinedShapeModification aRemoveModification) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean interceptUpdate(PipelinedViewerUpdate anUpdateSynchronization) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void init(ICommonContentExtensionSite aConfig) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object[] getChildren(Object parentElement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub

	}

	@Override
	public void restoreState(IMemento aMemento) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveState(IMemento aMemento) {
		// TODO Auto-generated method stub

	}

}
