package melnorme.util.swt.jface;

import melnorme.utilbox.core.Assert;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;


public abstract class SimpleLabelProvider implements ILabelProvider {
	
	@Override
	public void addListener(ILabelProviderListener listener) {
	}
	
	@Override
	public void removeListener(ILabelProviderListener listener) {
	}
	
	@Override
	public void dispose() {
	}
	
	@Override
	public boolean isLabelProperty(Object element, String property) {
		Assert.fail("This label provider does not support property based updating");
		return true;
	}
	
	@Override
    public String getText(Object element) {
        return element == null ? "" : element.toString();//$NON-NLS-1$
    }

}
