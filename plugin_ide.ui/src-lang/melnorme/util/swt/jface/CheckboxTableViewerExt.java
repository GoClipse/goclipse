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

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;

/**
 * Extension to CheckboxTableViewer providing type-safe access to content elements.
 */
public class CheckboxTableViewerExt<ELEMENTS> extends CheckboxTableViewer {
	
	public CheckboxTableViewerExt(Table table) {
		super(table);
	}
	
	@Deprecated
	@Override
	public void setContentProvider(IContentProvider provider) {
		super.setContentProvider(provider);
	}
	
	@Deprecated
	@Override
	public void setLabelProvider(IBaseLabelProvider labelProvider) {
		super.setLabelProvider(labelProvider);
	}
	
	public void setContentProvider0(IStructuredContentProvider0<ELEMENTS> provider) {
		super.setContentProvider(provider);
	}
	
	public void setLabelProvider0(ITableLabelProvider0<ELEMENTS> labelProvider) {
		super.setLabelProvider(labelProvider);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ITypedStructuredSelection<ELEMENTS> getSelection() {
		Control control = getControl();
		if (control == null || control.isDisposed()) {
			return (ITypedStructuredSelection<ELEMENTS>) TypedStructuredSelection.EMPTY;
		}
		List<?> list = getSelectionFromWidget();
		return new TypedStructuredSelection<ELEMENTS>(list, getComparer());
	}
	
	public Iterator<ELEMENTS> getSelectionIterator() {
		return getSelection().iterator();
	}
	
	public static interface ITypedStructuredSelection<ELEMENTS> extends IStructuredSelection, Iterable<ELEMENTS> {
		
		@Override
		public ELEMENTS getFirstElement();
		
		@Override
		public Iterator<ELEMENTS> iterator();
		
		@Override
		public List<ELEMENTS> toList();
		
		// Note: we don't change the type of array because we don't know the runtime component type
		@Override
		public Object[] toArray();  
		
	}
	
	public static class TypedStructuredSelection<ELEMENTS> extends StructuredSelection implements
			ITypedStructuredSelection<ELEMENTS> {
		
		public static final TypedStructuredSelection<Object> EMPTY = new TypedStructuredSelection<Object>();
		
		public TypedStructuredSelection() {
			super();
		}
		
		public TypedStructuredSelection(List<?> elements, IElementComparer comparer) {
			super(elements, comparer);
		}
		
		public TypedStructuredSelection(List<?> elements) {
			super(elements);
		}
		
		public TypedStructuredSelection(Object element) {
			super(element);
		}
		
		public TypedStructuredSelection(Object[] elements) {
			super(elements);
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public ELEMENTS getFirstElement() {
			return (ELEMENTS) super.getFirstElement();
		}
		
		@Override
		public Iterator<ELEMENTS> iterator() {
			return super.iterator();
		}
		
		@Override
		public List<ELEMENTS> toList() {
			return super.toList();
		}
		
		// Note: we don't change the type of array because we don't know the runtime component type
		@Override
		public Object[] toArray() {
			return super.toArray();
		}
		
	}
	
}