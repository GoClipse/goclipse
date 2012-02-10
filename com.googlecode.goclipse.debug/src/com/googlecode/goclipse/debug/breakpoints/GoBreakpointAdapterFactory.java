package com.googlecode.goclipse.debug.breakpoints;

import com.googlecode.goclipse.editors.GoEditor;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.debug.ui.actions.IToggleBreakpointsTarget;

/**
 * 
 * @author devoncarew
 */
public class GoBreakpointAdapterFactory implements IAdapterFactory {

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adapterType == IToggleBreakpointsTarget.class && adaptableObject instanceof GoEditor) {
			GoEditor editor = (GoEditor) adaptableObject;
			IResource resource = (IResource) editor.getEditorInput().getAdapter(IResource.class);

			if (resource != null) {
				String extension = resource.getFileExtension();

				if (extension != null && extension.equals("go")) {
					return new GoToggleBreakpointDelegate();
				}
			}
		}

		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class[] getAdapterList() {
		return new Class[] { IToggleBreakpointsTarget.class };
	}

}
