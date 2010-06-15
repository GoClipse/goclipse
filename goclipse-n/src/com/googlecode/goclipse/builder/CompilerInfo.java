package com.googlecode.goclipse.builder;

import org.eclipse.core.resources.IResource;

/**
 * @author steel
 */
public class CompilerInfo {

   IResource resource;
   int       line;
   String    message;

   boolean   isLocationFound;
   int       begin;
   int       end;

   public CompilerInfo(IResource resource, int line, String message) {
      isLocationFound = false;
      this.resource = resource;
      this.line = line;
      this.message = message;
   }
}
