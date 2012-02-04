package com.googlecode.goclipse.search;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.search.ui.ISearchPageScoreComputer;

public class GoSearchAdapterFactory implements IAdapterFactory {
  private GoSearchPageScoreComputer scoreComputer = new GoSearchPageScoreComputer();

  public GoSearchAdapterFactory() {

  }

  @SuppressWarnings("rawtypes")
  @Override
  public Object getAdapter(Object adaptableObject, Class adapterType) {
    return scoreComputer;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public Class[] getAdapterList() {
    return new Class[] {ISearchPageScoreComputer.class};
  }

}
