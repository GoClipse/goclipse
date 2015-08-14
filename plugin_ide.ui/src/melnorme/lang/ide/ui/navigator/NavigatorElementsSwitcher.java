package melnorme.lang.ide.ui.navigator;

import org.eclipse.core.filesystem.IFileStore;

import com.googlecode.goclipse.ui.navigator.elements.GoPathElement;

import melnorme.lang.tooling.LANG_SPECIFIC;

@LANG_SPECIFIC
public interface NavigatorElementsSwitcher<RET> extends NavigatorElementsSwitcher_Default<RET> {
	
	@Override
	default RET switchElement(Object element) {
		if(element instanceof GoPathElement) {
			return visitGoPathElement((GoPathElement) element);
		}
		if(element instanceof IFileStore) {
			return visitFileStoreElement((IFileStore) element);
		}
		return NavigatorElementsSwitcher_Default.super.switchElement(element);
	}
	
	public abstract RET visitGoPathElement(GoPathElement goPathElement);
	
	public abstract RET visitFileStoreElement(IFileStore fileStore);
	
}