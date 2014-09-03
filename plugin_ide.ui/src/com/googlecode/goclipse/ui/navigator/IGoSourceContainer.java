package com.googlecode.goclipse.ui.navigator;

/**
 * 
 * @author Mihailo Vasiljevic
 * 
 */
public interface IGoSourceContainer {

	public Object getParent();
	
	public Object[] getChildren();

	public int findMaxProblemSeverity();

}
