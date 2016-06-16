package com.googlecode.goclipse.search;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.search.ui.ISearchPageScoreComputer;

public class GoSearchAdapterFactory implements IAdapterFactory {
	private GoSearchPageScoreComputer scoreComputer = new GoSearchPageScoreComputer();
	
	public GoSearchAdapterFactory() {
	
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(Object adaptableObject, Class<T> adapterType) {
		return (T) scoreComputer;
	}
	
	@Override
	public Class<?>[] getAdapterList() {
		return new Class[] { ISearchPageScoreComputer.class };
	}
	
}