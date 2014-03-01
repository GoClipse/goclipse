package com.googlecode.goclipse.search;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.search.ui.ISearchPageScoreComputer;

import com.googlecode.goclipse.builder.GoNature;

class GoSearchPageScoreComputer implements ISearchPageScoreComputer {

  public GoSearchPageScoreComputer() {

  }

  @Override
  public int computeScore(String searchPageId, Object input) {
    if (!GoSearchPage.EXTENSION_POINT_ID.equals(searchPageId)) {
      return ISearchPageScoreComputer.UNKNOWN;
    }

    IResource resource = getResource(input);

    if (resource != null) {
      if (resource.getName().endsWith(".go")) {
        return 90;
      }

      try {
        if (resource.getProject().isNatureEnabled(GoNature.NATURE_ID)) {
          return 90;
        }
      } catch (CoreException e) {

      }
    }

    return UNKNOWN;
  }

  private IResource getResource(Object input) {
    if (input instanceof IResource) {
      return (IResource) input;
    }

    if (input instanceof IAdaptable) {
      IAdaptable adapt = (IAdaptable) input;

      return (IResource) adapt.getAdapter(IResource.class);
    }

    if (input instanceof IMarker) {
      IMarker marker = (IMarker) input;

      return marker.getResource();
    }

    return null;
  }

}
