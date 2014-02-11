package com.googlecode.goclipse.navigator;

import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonLabelProvider;
import org.eclipse.ui.navigator.IExtensionStateModel;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.builder.GoNature;

/**
 * The label provider for the Go CNF navigator.
 */
public class NavigatorLabelProvider extends WorkbenchLabelProvider implements ICommonLabelProvider,
    IPropertyChangeListener {
  private IExtensionStateModel stateModel;
  private boolean flatLayout;
  
  public NavigatorLabelProvider() {

  }

  @Override
  protected ImageDescriptor decorateImage(ImageDescriptor input, Object element) {
    if (element instanceof IResource) {
      IResource resource = (IResource) element;

      if (isGoProject(resource.getProject())) {
        if (resource instanceof IFolder) {
          IFolder folder = (IFolder)resource;
          
          if (resource.getParent() instanceof IProject) {
            if ("src".equals(resource.getName())) {
              return Activator.getImageDescriptor("icons/src-folder.png");
            } else if ("pkg".equals(resource.getName())) {
              return Activator.getImageDescriptor("icons/open_artifact_obj.gif");
            } else if ("bin".equals(resource.getName())) {
              return Activator.getImageDescriptor("icons/cf_obj.gif");
            }
          } else if (containsGoSource(folder)) {
            return Activator.getImageDescriptor("icons/source-folder.gif");
          }
        } else if (resource instanceof IFile) {
          IFile file = (IFile) resource;
          String ext = file.getFileExtension();

          if ("a".equals(ext)) {
            return Activator.getImageDescriptor("icons/package_file.png");
          } else if (ext == null) {
            if (file.getResourceAttributes() != null && file.getResourceAttributes().isExecutable()) {
              return Activator.getImageDescriptor("icons/binary_file.png");
            }
          }
        }
      }
    }

    return input;
  }

  @Override
  protected String decorateText(String input, Object element) {
    if (flatLayout) {
      if (element instanceof IFolder) {
        IFolder folder = (IFolder)element;
        
        IPath path = folder.getProjectRelativePath();
        
        if (path.segment(0).equals("src") || path.segment(0).equals("pkg")) {
          //path = path.removeFirstSegments(0);
          
          return path.toPortableString();
        }
      }
    }
    
    return input;
  }

  private boolean isGoProject(IProject project) {
    try {
      return project.hasNature(GoNature.NATURE_ID);
    } catch (CoreException exception) {
      return false;
    }
  }

  @Override
  public void restoreState(IMemento aMemento) {

  }

  @Override
  public void saveState(IMemento aMemento) {

  }

  @Override
  public String getDescription(Object element) {
    if (element instanceof IResource) {
      return ((IResource) element).getFullPath().makeRelative().toString();
    } else if (element instanceof IFileStore) {
      return ((IFileStore) element).toString();
    } else if (element instanceof GoPathElement) {
      GoPathElement pathElement = (GoPathElement) element;

      return pathElement.getName() + " - " + pathElement.getDirectory().toString();
    } else {
      return null;
    }
  }

  @Override
  public void init(ICommonContentExtensionSite extensionSite) {
    stateModel = extensionSite.getExtensionStateModel();

    setIsFlatLayout(stateModel.getBooleanProperty(NavigatorContentProvider.IS_LAYOUT_FLAT));

    stateModel.addPropertyChangeListener(this);
  }

  @Override
  public void propertyChange(PropertyChangeEvent event) {
    if (NavigatorContentProvider.IS_LAYOUT_FLAT.equals(event.getProperty())) {
      if (event.getNewValue() != null) {
        boolean newValue = ((Boolean) event.getNewValue()).booleanValue() ? true : false;

        setIsFlatLayout(newValue);
      }
    }
  }

  private boolean containsGoSource(IFolder folder) {
    final boolean[] result = new boolean[1];
    final int parentDepth = folder.getProjectRelativePath().segmentCount();
    
    try {
      folder.accept(new IResourceVisitor() {
        @Override
        public boolean visit(IResource resource) throws CoreException {
          if (result[0]) {
            return false;
          }
          
          if (resource instanceof IFile) {
            IFile file = (IFile)resource;
            
            result[0] |= "go".equals(file.getFileExtension());
            
            return true;
          } else if (resource instanceof IContainer) {
            int depth = resource.getProjectRelativePath().segmentCount();
            
            if (depth - parentDepth > 3) {
              return false;
            } else {
              return true;
            }
          } else {
            return false;
          }
        }
      });
      
      return result[0];
    } catch (CoreException e) {
      return result[0];
    }
  }

  private void setIsFlatLayout(boolean value) {
    this.flatLayout = value;
    
    fireLabelProviderChanged(new LabelProviderChangedEvent(this));
  }

}
