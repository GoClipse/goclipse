/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.util.swt.jface;

import melnorme.utilbox.tree.IElement;
import melnorme.utilbox.tree.SimpleTreeElement;

import org.eclipse.jface.viewers.LabelProvider;

public class LabeledTreeElement extends SimpleTreeElement {
	
	protected final String label; 
	
	public LabeledTreeElement(IElement parent, IElement[] children, String label) {
		super(parent, children);
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
	
	public static class LabeledTreeElementLabelProvider extends LabelProvider {
		
		@Override
		public String getText(Object element) {
			if(element instanceof LabeledTreeElement) {
				LabeledTreeElement labeledTreeElement = (LabeledTreeElement) element;
				return labeledTreeElement.getLabel();
			}
			return null;
		}
		
	}
	
}