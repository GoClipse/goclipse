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
package melnorme.lang.ide.ui.editor.structure;

import static melnorme.utilbox.misc.ArrayUtil.nullToEmpty;

import melnorme.lang.tooling.structure.IStructureElementContainer;
import melnorme.lang.tooling.structure.StructureElement;
import melnorme.util.swt.jface.AbstractTreeContentProvider;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;

public class StructureElementContentProvider extends AbstractTreeContentProvider {
	
	@Override
	public boolean hasChildren(Object element) {
		if(element instanceof IStructureElementContainer) {
			IStructureElementContainer structureElement = (IStructureElementContainer) element;
			return hasChildren(structureElement); 
		}
		return false;
	}
	
	protected boolean hasChildren(IStructureElementContainer structureElement) {
		return nullToEmpty(getChildren(structureElement)).length != 0;
	}
	
	@Override
	public Object[] getChildren(Object element) {
		if(element instanceof IStructureElementContainer) {
			IStructureElementContainer structureElement = (IStructureElementContainer) element;
			return getChildren(structureElement);
		}
		return null;
	}
	
	protected Object[] getChildren(IStructureElementContainer structureElement) {
		return toArray(structureElement.getChildren());
	}
	
	public static <T> Object[] toArray(Indexable<T> indexable) {
		return new ArrayList2<>().addAll2(indexable).toArray();
	}
	
	@Override
	public Object getParent(Object element) {
		if(element instanceof StructureElement) {
			StructureElement structureElement = (StructureElement) element;
			return structureElement.getParent(); 
		}
		return null;
	}

}