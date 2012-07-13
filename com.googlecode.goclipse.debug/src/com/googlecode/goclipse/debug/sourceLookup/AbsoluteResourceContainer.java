package com.googlecode.goclipse.debug.sourceLookup;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.sourcelookup.ISourceContainerType;
import org.eclipse.debug.core.sourcelookup.containers.AbstractSourceContainer;
import org.eclipse.debug.core.sourcelookup.containers.WorkspaceSourceContainer;

import java.io.File;

public class AbsoluteResourceContainer extends AbstractSourceContainer {
  
  public AbsoluteResourceContainer() {
    
  }

  @Override
  public Object[] findSourceElements(String name) throws CoreException {
    File file = new File(name);
    
    return ResourcesPlugin.getWorkspace().getRoot().findFilesForLocationURI(file.toURI());
  }

  @Override
  public String getName() {
    return "File system";
  }

  @Override
  public ISourceContainerType getType() {
    return getSourceContainerType(WorkspaceSourceContainer.TYPE_ID);
  }

}
