package com.googlecode.goclipse.debug.presentation;

import org.eclipse.core.resources.IFile;
import org.eclipse.debug.core.model.ILineBreakpoint;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.ui.IDebugModelPresentation;
import org.eclipse.debug.ui.IValueDetailListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.FileEditorInput;

import com.googlecode.goclipse.debug.GoDebugPlugin;
import com.googlecode.goclipse.debug.breakpoints.GoBreakpoint;
import com.googlecode.goclipse.debug.model.GoDebugVariable;

/**
 * 
 * @author devoncarew
 */
public class GoModelPresentation implements IDebugModelPresentation {

	public GoModelPresentation() {

	}

	@Override
	public void addListener(ILabelProviderListener listener) {

	}

	@Override
	public void dispose() {

	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {

	}

	@Override
	public IEditorInput getEditorInput(Object element) {
		if (element instanceof IFile) {
			return new FileEditorInput((IFile) element);
		}
		
		if (element instanceof ILineBreakpoint) {
			return new FileEditorInput((IFile) ((ILineBreakpoint) element).getMarker().getResource());
		}

		return null;
	}

	@Override
	public String getEditorId(IEditorInput input, Object element) {
		if (element instanceof IFile || element instanceof ILineBreakpoint) {
			return "com.googlecode.goclipse.editors.Editor";
		}
		
		return null;
	}

	@Override
	public void setAttribute(String attribute, Object value) {

	}

	@Override
	public Image getImage(Object element) {
	  if (element instanceof GoBreakpoint) {
	    return null;
	  }
	  
	  if (element instanceof GoDebugVariable) {
	    return GoDebugPlugin.getImage("icons/obj16/compare_field.gif");
	  }

		return null;
	}

	@Override
	public String getText(Object element) {
		return null;
	}

	@Override
	public void computeDetail(IValue value, IValueDetailListener listener) {
		// TODO: add more detailed info about an object

	  listener.detailComputed(value, null);
	}

}
