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
package melnorme.util.swt.jface;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;


public abstract class AbstractContentProvider implements IStructuredContentProvider {
	
	protected Viewer viewer;
	protected Object input;
	
	@Override
	public void inputChanged(Viewer newViewer, Object oldInput, Object newInput) {
		boolean isInitialization = (viewer == null && newViewer != null);
		
		this.viewer = newViewer;
		this.input = newInput;
		
		if(isInitialization) {
			viewerInitialized();
		}
	}
	
	protected void viewerInitialized() {
	}
	
	@Override
	public void dispose() {
	}
	
}