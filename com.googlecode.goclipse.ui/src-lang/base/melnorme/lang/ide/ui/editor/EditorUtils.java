/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.editor;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;

public class EditorUtils {

	public static IProject getProject(IEditorInput input) {
		if(input instanceof IFileEditorInput) {
			return ((IFileEditorInput) input).getFile().getProject();
		}
		
		IResource resource = (IResource) input.getAdapter(IResource.class);
		if(resource != null) {
			return resource.getProject();
		}
		
		return null;
	}
}