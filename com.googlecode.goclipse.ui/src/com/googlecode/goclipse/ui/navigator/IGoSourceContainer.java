package com.googlecode.goclipse.ui.navigator;

/**
 * 
 * @author Mihailo Vasiljevic
 * 
 */
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;

public interface IGoSourceContainer {

	public IFolder getFolder();

	public IProject getProject();

}
