package com.googlecode.goclipse.debug.sourceLookup;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.sourcelookup.ISourceContainerType;
import org.eclipse.debug.core.sourcelookup.containers.AbstractSourceContainer;
import org.eclipse.debug.core.sourcelookup.containers.DefaultSourceContainer;

import java.io.File;

public class AbsoluteFileContainer extends AbstractSourceContainer {
  private static Object[] EMPTY_RESULT = new Object[0];
  
  public AbsoluteFileContainer() {
    
  }

  @Override
  public Object[] findSourceElements(String name) throws CoreException {
    File file = new File(name);
    
    if (file.exists()) {
      return new Object[] { EFS.getStore(file.toURI()) };
    } else {
      return EMPTY_RESULT;
    }
  }

  @Override
  public String getName() {
    return "File system";
  }

  @Override
  public ISourceContainerType getType() {
    return getSourceContainerType(DefaultSourceContainer.TYPE_ID);
  }

}
