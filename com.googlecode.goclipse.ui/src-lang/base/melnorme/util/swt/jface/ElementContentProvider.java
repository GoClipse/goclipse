package melnorme.util.swt.jface;

import melnorme.utilbox.tree.IElement;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;


/**
 * A simple content provider for IElement's
 */
public class ElementContentProvider implements ITreeContentProvider {
	
	@Override
	public void dispose() {
	}
	
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}
	
	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}
	
	@Override
	public boolean hasChildren(Object element) {
		return ((IElement) element).hasChildren();
	}
	
	@Override
	public Object getParent(Object element) {
		return ((IElement) element).getParent();
	}
	
	@Override
	public Object[] getChildren(Object parentElement) {
		return ((IElement) parentElement).getChildren();
	}
	
}
