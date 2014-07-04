package com.googlecode.goclipse.navigator;

import java.io.File;

/**
 * A representation of top level root nodes, like GOROOT and GOPATH entries.
 */
public class GoPathElement {
  private String name;
  private File rootFolder;
  
  public GoPathElement(String name, File rootFolder) {
    this.name = name;
    this.rootFolder = rootFolder;
  }

  public String getName() {
    return name;
  }

  public File getDirectory() {
    return rootFolder;
  }

  @Override
  public String toString() {
    return name;
  }
  
  @Override
  public int hashCode() {
    return rootFolder.hashCode();
  }
  
  @Override
  public boolean equals(Object other) {
    if (!(other instanceof GoPathElement)) {
      return false;
    }
    
    GoPathElement obj = (GoPathElement)other;
    
    return rootFolder.equals(obj.rootFolder);
  }
  
}
