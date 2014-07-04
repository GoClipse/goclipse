package com.googlecode.goclipse.navigator;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonContentProvider;
import org.eclipse.ui.navigator.IExtensionStateModel;

// TODO: support flat vs. hierarchical modes ?

/**
 * A CNF content provider that decorates the the standard resource content provider with a GOROOT
 * node. This shows the IFileStore files in the GOROOT/src directory.
 * 
 * @author devoncarew
 */
public class NavigatorContentProvider implements ICommonContentProvider, IPropertyChangeListener {
  static final String IS_LAYOUT_FLAT = "isLayoutFlat";

  private final Object[] NO_CHILDREN = new Object[0];

  private Viewer viewer;

  private IExtensionStateModel stateModel;
  @SuppressWarnings("unused")
  private boolean flatLayout;

  /**
   * Create a new content provider.
   */
  public NavigatorContentProvider() {

  }

  @Override
  public void dispose() {

  }

  @Override
  public Object[] getChildren(Object parentElement) {
    return NO_CHILDREN;
  }

  @Override
  public Object[] getElements(Object inputElement) {
    return getChildren(inputElement);
  }

  @Override
  public Object getParent(Object element) {
    return null;
  }

  @Override
  public boolean hasChildren(Object element) {
    return getChildren(element).length > 0;
  }

  @Override
  public void init(ICommonContentExtensionSite extensionSite) {
    stateModel = extensionSite.getExtensionStateModel();

    setIsFlatLayout(stateModel.getBooleanProperty(IS_LAYOUT_FLAT));

    stateModel.addPropertyChangeListener(this);
  }

  @Override
  public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    this.viewer = viewer;
  }

  @Override
  public void restoreState(IMemento aMemento) {

  }

  @Override
  public void saveState(IMemento aMemento) {

  }

  @Override
  public void propertyChange(PropertyChangeEvent event) {
    if (IS_LAYOUT_FLAT.equals(event.getProperty())) {
      if (event.getNewValue() != null) {
        boolean newValue = ((Boolean) event.getNewValue()).booleanValue() ? true : false;

        setIsFlatLayout(newValue);
      }
    }

    updateViewer();
  }

  private void setIsFlatLayout(boolean value) {
    this.flatLayout = value;
  }

  private void updateViewer() {
    Display.getDefault().asyncExec(new Runnable() {
      @Override
      public void run() {
        if (viewer != null) {
          viewer.refresh();
        }
      }
    });
  }

}
