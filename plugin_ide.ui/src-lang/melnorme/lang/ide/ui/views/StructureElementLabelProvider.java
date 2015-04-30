/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.views;

import melnorme.lang.tooling.structure.IStructureElement;

import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;

public class StructureElementLabelProvider extends AbstractLangLabelProvider {
	
	@Override
	public StyledString getStyledText(Object element) {
		if(element instanceof IStructureElement) {
			IStructureElement structureElement = (IStructureElement) element;
			StyledString styledString = new StyledString(structureElement.getName());
			
			if(structureElement.getType() != null) {
				styledString.append(" : " + structureElement.getType());
			}
			return styledString;
		}
		return null;
	}
	
	@Override
	public Image getImage(Object element) {
		return null;
	}
	
}