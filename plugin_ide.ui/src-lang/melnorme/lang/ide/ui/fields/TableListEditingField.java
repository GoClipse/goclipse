/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation (JDT)
 *     Bruno Medeiros - modifications, abstracted TableListEditingField
 *******************************************************************************/
package melnorme.lang.ide.ui.fields;

import static melnorme.utilbox.core.CoreUtil.arrayFrom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import melnorme.lang.ide.ui.FieldMessages;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.jface.AbstractContentProvider;
import melnorme.util.swt.jface.CheckboxTableViewerExt;
import melnorme.util.swt.jface.IStructuredContentProvider0;
import melnorme.util.swt.jface.ITableLabelProvider0;
import melnorme.utilbox.misc.ArrayUtil;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.PixelConverter;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

/**
 * A component to manage a list of elements.
 * List is presented under a table with columns.
 * The component also has several buttons are added to add/remove/edit the elements.
 */
public abstract class TableListEditingField<E> extends CommonTableBasedField {
	
	protected final List<E> elements = new ArrayList<>();
	protected E checkedElement = null;
	
	protected Composite componentComposite;
	protected CheckboxTableViewerExt<E> elementsTableViewer;
	
	protected Button buttonAdd;
	protected Button buttonRemove;
	protected Button buttonEdit;
	protected Button buttonCopy;
	
	public List<E> getElements() {
		return Collections.unmodifiableList(elements);
	}
	
	public E[] getElementsArray(Class<E> klass) {
		return arrayFrom(elements, klass);
	}
	
	public void setElements(Collection<? extends E> newElements) {
		elements.clear();
		elements.addAll(newElements);
		checkedElement = null;
		updateControlsAndNotifyListeners();
	}
	
	public void addElement(E newElement) {
		elements.add(newElement);
		updateControlsAndNotifyListeners();
	}
	
	public void removeElements(Collection<E> toRemove) {
		elements.removeAll(toRemove);
		if(!elements.contains(checkedElement)) {
			checkedElement = null;
		}
		updateControlsAndNotifyListeners();
	}
	
	public E getCheckedElement() {
		return checkedElement;
	}
	
	public void setCheckedElement(E element) {
		if(element != checkedElement) {
			checkedElement = element;
			updateControlCheckedElement(element);
			fireFieldValueChanged();
		}
	}
	
	protected void updateControlsAndNotifyListeners() {
		if(elementsTableViewer != null) {
			elementsTableViewer.setInput(elements);
			elementsTableViewer.refresh();
		}
		fireFieldValueChanged();
	}
	
	protected void updateControlCheckedElement(E element) {
		if(elementsTableViewer != null) {
			elementsTableViewer.setCheckedElements(ArrayUtil.singletonArray(element, Object.class));
		}
	}
	
	protected E getFirstSelectedElement() {
		return elementsTableViewer.getSelection().getFirstElement();
	}
	
	protected Iterator<E> getSelectionIterator() {
		return elementsTableViewer.getSelection().iterator();
	}
	
	/* ----------------- controls ----------------- */
	
	protected static Button createPushButton(Composite parent, String label) {
		return SWTFactoryUtil.createPushButton(parent, label, null);
	}
	
	@Override
	public Composite createComponent(Composite parent) {
		componentComposite = new Composite(parent, SWT.NULL);
		componentComposite.setLayout(GridLayoutFactory.swtDefaults().numColumns(2).margins(0, 0).create());
		
		Composite tableComposite = createTable(componentComposite);
		PixelConverter conv = new PixelConverter(componentComposite);
		tableComposite.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).
			hint(conv.convertWidthInCharsToPixels(80), SWT.DEFAULT).create());
		
		elementsTableViewer = new CheckboxTableViewerExt<E>(table);
		elementsTableViewer.setContentProvider0(new TableContentProvider());
		elementsTableViewer.setLabelProvider0(createLabelProvider());
		
		elementsTableViewer.addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				// Ensure only one element checked
				setCheckedElement(event.getChecked() ? (E) event.getElement() : null);
			}
		});
		
		elementsTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent evt) {
				updateButtonEnablement();
			}
		});
		elementsTableViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent e) {
				if (!elementsTableViewer.getSelection().isEmpty()) {
					editElementButtonPressed();
				}
			}
		});
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if(event.character == SWT.DEL && event.stateMask == 0) {
					if(buttonRemove.getEnabled()) {
						removeElementButtonPressed();
					}
				}
			}
		});
		
		table.layout();
		
		Composite buttonComposite = new Composite(componentComposite, SWT.NULL);
		buttonComposite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
		createButtons(buttonComposite);
		
		updateButtonEnablement();
		
		return componentComposite;
	}
	
	public Control getControl() {
		return componentComposite;
	}
	
	protected Shell getShell() {
		return getControl().getShell();
	}
	
	public Table getTable() {
		return elementsTableViewer.getTable();
	}
	
	protected class TableContentProvider extends AbstractContentProvider 
			implements IStructuredContentProvider0<E> {
		@Override
		public E[] getElements(Object input) {
			return (E[]) arrayFrom(elements, Object.class);
		}
	}
	
	protected abstract ITableLabelProvider0<E> createLabelProvider();
	
	/* ----------------------------------- */
	
	protected void createButtons(Composite buttonComposite) {
		buttonComposite.setLayout(GridLayoutFactory.swtDefaults().margins(0, 0).create());
		
		buttonAdd = createPushButton(buttonComposite, FieldMessages.ListField_ButtonAdd_Label);
		buttonAdd.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event evt) {
				addElementButtonPressed();
			}
		});
		
		buttonEdit = createPushButton(buttonComposite, FieldMessages.ListField_ButtonEdit_Label);
		buttonEdit.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event evt) {
				editElementButtonPressed();
			}
		});
		
		buttonCopy = createPushButton(buttonComposite, FieldMessages.ListField_ButtonCopy_Label);
		buttonCopy.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event evt) {
				copyElementButtonPressed();
			}
		});
		
		buttonRemove = createPushButton(buttonComposite, FieldMessages.ListField_ButtonRemove_Label);
		buttonRemove.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event evt) {
				removeElementButtonPressed();
			}
		});
	}
	
	protected void updateButtonEnablement() {
		IStructuredSelection selection = elementsTableViewer.getSelection();
		buttonEdit.setEnabled(selection.size() == 1);
		if(!buttonCopy.isDisposed()) {
			buttonCopy.setEnabled(selection.size() > 0);
		}
	}
	
	protected void addElementButtonPressed() {
	}

	protected void editElementButtonPressed() {
	}

	protected void copyElementButtonPressed() {
	}
	
	protected void removeElementButtonPressed() {
		List<E> toRemove = elementsTableViewer.getSelection().toList();
		removeElements(toRemove);
	}
	
}