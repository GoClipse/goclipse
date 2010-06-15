package com.googlecode.goclipse.debug;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.IPersistableSourceLocator;
import org.eclipse.debug.core.model.IStackFrame;

/**
 * 
 * @author steel
 */
public class PersistableSourceLocator implements IPersistableSourceLocator {

   @Override
   public String getMemento() throws CoreException {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public void initializeDefaults(ILaunchConfiguration configuration) throws CoreException {
      // TODO Auto-generated method stub

   }

   @Override
   public void initializeFromMemento(String memento) throws CoreException {
      // TODO Auto-generated method stub

   }

   @Override
   public Object getSourceElement(IStackFrame stackFrame) {
      // TODO Auto-generated method stub
      return null;
   }

}
