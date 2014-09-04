package com.googlecode.goclipse.navigator;

import java.io.File;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.builder.GoConstants;
import com.googlecode.goclipse.core.GoCore;
import com.googlecode.goclipse.preferences.PreferenceConstants;
import com.googlecode.goclipse.ui.GoUIPlugin;

// TODO: this content provider is hard-coded to show files from GOROOT; we'll probably want this
// to have better knowledge of the GOROOT / GOPATH directories that are in use by the project.

/**
 * A CNF content provider that decorates the the standard resource content provider with a GOROOT
 * node. This shows the IFileStore files in the GOROOT/src directory.
 * 
 * @author devoncarew
 */
public class NavigatorContentProvider2 implements ITreeContentProvider, IPropertyChangeListener {
  private final Object[] NO_CHILDREN = new Object[0];

  private Viewer viewer;

  public NavigatorContentProvider2() {
    // TODO: we really want to listen for changes to the root directories referenced by the project.
    GoUIPlugin.getPrefStore().addPropertyChangeListener(this);
    GoUIPlugin.getCorePrefStore().addPropertyChangeListener(this);
  }

  @Override
  public void dispose() {
	GoUIPlugin.getPrefStore().removePropertyChangeListener(this);
	GoUIPlugin.getCorePrefStore().removePropertyChangeListener(this);
  }

  @Override
  public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    this.viewer = viewer;
  }

  @Override
  public Object[] getElements(Object inputElement) {
    return getChildren(inputElement);
  }

  @Override
  public Object[] getChildren(Object parentElement) {
    if (parentElement instanceof IProject) {
      File[] goPath = getGoPathSrcFolder((IProject)parentElement);

      if (!isGoRootSet()) {
        return NO_CHILDREN;
        
      } else {
    	  
        if (goPath!=null && goPath.length > 0) {
        	
        	// populate the go paths
        	if (goPath.length == 1) {
        		return new GoPathElement[] {
        				new GoPathElement(GoConstants.GOROOT, getGoRootSrcFolder()),
        				new GoPathElement(goPath[0].getParent(), goPath[0])};
        
        	} else if (goPath.length > 1) {
        		GoPathElement[] gpe = new GoPathElement[goPath.length+1];
        		gpe[0] = new GoPathElement(GoConstants.GOROOT, getGoRootSrcFolder());
        		
        		for (int i = 0; i < goPath.length; i++){
        			gpe[i+1] = new GoPathElement(goPath[i].getParent(), goPath[i]);
        		}
        	}
        } else {
          return new GoPathElement[] {new GoPathElement(GoConstants.GOROOT, getGoRootSrcFolder())};
        }
      }
    } else if (parentElement instanceof GoPathElement) {
      GoPathElement pathElement = (GoPathElement) parentElement;

      try {
        IFileStore fileStore = EFS.getStore(pathElement.getDirectory().toURI());

        return fileStore.childStores(EFS.NONE, null);
      } catch (CoreException exception) {
        return NO_CHILDREN;
      }
    } else if (parentElement instanceof IFileStore) {
      IFileStore file = (IFileStore) parentElement;

      try {
        return file.childStores(EFS.NONE, null);
      } catch (CoreException e) {
        return NO_CHILDREN;
      }
    }

    return NO_CHILDREN;
  }

  @Override
  public Object getParent(Object element) {
    if (element instanceof IFileStore) {
      IFileStore file = (IFileStore) element;

      // TODO: trim this at the GOROOT directory

      return file.getParent();
    }

    return null;
  }

  @Override
  public boolean hasChildren(Object element) {
    return getChildren(element).length > 0;
  }

  @Override
  public void propertyChange(PropertyChangeEvent event) {
    updateViewer();
  }

  private boolean isGoRootSet() {
    String goRoot = GoCore.getPreferences().getString(PreferenceConstants.GOROOT);

    return !"".equals(goRoot);
  }

  protected File getGoRootSrcFolder() {
    String goRoot = GoCore.getPreferences().getString(PreferenceConstants.GOROOT);

    File srcFolder = Path.fromOSString(goRoot).append("src/pkg").toFile();

    return srcFolder;
  }

  /**
   * @return File representing the external GOPATH
   */
	protected File[] getGoPathSrcFolder(IProject project) {
		
		try {
			
			String[] goPath = {};//Environment.INSTANCE.getGoPath(project);
			
			if("".equals(goPath[0])){
				return null;
			}
			
			File[] files = new File[goPath.length];

			for (int i = 0; i < goPath.length; i++) {
				String path = goPath[i];
				File srcFolder = Path.fromOSString(path).append("src").toFile();
				
				if (!srcFolder.exists()) {

					try {
						srcFolder.mkdirs();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				files[i] = srcFolder;

			}

			return files;

		} catch (Exception e) {
			return null;
		}
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
