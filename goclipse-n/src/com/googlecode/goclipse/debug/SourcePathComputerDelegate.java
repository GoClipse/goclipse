package com.googlecode.goclipse.debug;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.sourcelookup.ISourceContainer;
import org.eclipse.debug.core.sourcelookup.ISourcePathComputerDelegate;

/**
 * 
 * @author steel
 */
public class SourcePathComputerDelegate implements ISourcePathComputerDelegate{

   @Override
   public ISourceContainer[] computeSourceContainers(ILaunchConfiguration configuration, IProgressMonitor monitor)
         throws CoreException {
      // TODO Auto-generated method stub
      return null;
   }

}
