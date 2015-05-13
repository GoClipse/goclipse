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

import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;

public class TreeViewerExt extends TreeViewer {

	public TreeViewerExt(Composite parent, int style) {
		super(parent, style);
	}

	public TreeViewerExt(Composite parent) {
		super(parent);
	}

	public TreeViewerExt(Tree tree) {
		super(tree);
	}
	
	
	@Override
	public ITreeSelection getSelection() {
		return (ITreeSelection) super.getSelection();
	}
	
	public Object getSelectionFirstElement() {
		return getSelection().getFirstElement();
	}
	
	public void setSelectedElement(Object selectedElement) {
		setSelection(selectedElement == null ? StructuredSelection.EMPTY : new StructuredSelection(selectedElement));
	}
	
}