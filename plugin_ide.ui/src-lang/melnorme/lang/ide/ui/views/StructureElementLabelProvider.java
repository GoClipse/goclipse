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

import melnorme.lang.ide.ui.LangImageProvider;
import melnorme.lang.ide.ui.LangImages;
import melnorme.lang.tooling.ElementAttributes;
import melnorme.lang.tooling.structure.IStructureElement;
import melnorme.util.swt.jface.resources.LangElementImageDescriptor;

import org.eclipse.jface.resource.ImageDescriptor;
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
		if(element instanceof IStructureElement) {
			IStructureElement structureElement = (IStructureElement) element;
			
			return getImage(structureElement);
		}
		return null;
	}
	
	public Image getImage(IStructureElement element) {
		ImageDescriptor imageDescriptor = getImageDescriptor(element);
		return LangImages.getImageDescriptorRegistry().get(imageDescriptor);
	}
	
	
	protected ImageDescriptor getImageDescriptor(IStructureElement element) {
		ImageDescriptor baseImageDescriptor = getBaseImageDescriptor(element);
		return getElementImageDescriptor(baseImageDescriptor, element.getAttributes());
	}
	
	protected ImageDescriptor getBaseImageDescriptor(IStructureElement structureElement) {
		return structureElement.getKind().switchOnKind(new LangImageProvider()).getDescriptor();
	}
	
	public LangElementImageDescriptor getElementImageDescriptor(ImageDescriptor baseImage,
			ElementAttributes elementAttributes) {
		return new LangElementImageDescriptor(baseImage, elementAttributes);
	}
	
}