/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
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
import melnorme.lang.ide.ui.LangUIPlugin_Actual;
import melnorme.lang.tooling.ElementAttributes;
import melnorme.lang.tooling.structure.StructureElement;
import melnorme.util.swt.jface.resources.LangElementImageDescriptor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;

public class StructureElementLabelProvider extends AbstractLangLabelProvider {
	
	public static DelegatingStyledCellLabelProvider createLangLabelProvider() {
		StructureElementLabelProvider labelProvider = LangUIPlugin_Actual.getStructureElementLabelProvider();
		// We wrap the base LabelProvider with a DelegatingStyledCellLabelProvider because for some reason
		// that prevents flicker problems when changing selection in Windows classic themes
		// Might not be necessary in the future.
		return new DelegatingStyledCellLabelProvider(labelProvider);
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public StyledString getStyledText(Object element) {
		if(element instanceof StructureElement) {
			StructureElement structureElement = (StructureElement) element;
			StyledString styledString = new StyledString(structureElement.getName());
			
			if(structureElement.getType() != null) {
				String typeSuffix = " : " + structureElement.getType();
				styledString.append(new StyledString(typeSuffix, StyledString.DECORATIONS_STYLER));
			}
			return styledString;
		}
		return null;
	}
	
	@Override
	public Image getImage(Object element) {
		if(element instanceof StructureElement) {
			StructureElement structureElement = (StructureElement) element;
			
			return getImage(structureElement);
		}
		return null;
	}
	
	public Image getImage(StructureElement element) {
		ImageDescriptor imageDescriptor = getImageDescriptor(element);
		return LangImages.getImageDescriptorRegistry().get(imageDescriptor);
	}
	
	
	protected ImageDescriptor getImageDescriptor(StructureElement element) {
		ImageDescriptor baseImageDescriptor = getBaseImageDescriptor(element);
		return getElementImageDescriptor(baseImageDescriptor, element.getAttributes());
	}
	
	protected ImageDescriptor getBaseImageDescriptor(StructureElement structureElement) {
		return structureElement.getKind().switchOnKind(new LangImageProvider()).getDescriptor();
	}
	
	public LangElementImageDescriptor getElementImageDescriptor(ImageDescriptor baseImage,
			ElementAttributes elementAttributes) {
		return new LangElementImageDescriptor(baseImage, elementAttributes);
	}
	
}