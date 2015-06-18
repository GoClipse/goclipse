package LANG_PROJECT_ID.ide.ui.navigator;

import melnorme.lang.ide.ui.views.AbstractNavigatorContentProvider;

public class LANGUAGE_NavigatorContentProvider extends AbstractNavigatorContentProvider {
	
	
	@Override
	public boolean hasChildren(Object element) {
		return false;
	}
	
	@Override
	public Object[] getChildren(Object parent) {
		return null;
	}
	
	@Override
	public Object getParent(Object element) {
		return null;
	}
	
}