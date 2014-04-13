/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package melnorme.lang.jdt.ui.wizards.dialogfields;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import melnorme.lang.jdt.internal.ui.util.SWTUtil;
import melnorme.lang.jdt.internal.ui.util.TableLayoutComposite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.layout.PixelConverter;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerSorter;


/**
 * A list with a button bar.
 * Typical buttons are 'Add', 'Remove', 'Up' and 'Down'.
 * List model is independent of widget creation.
 * DialogFields controls are: Label, List and Composite containing buttons.
 * 
 * @param <E> the type of the list elements
 */
@SuppressWarnings({"unused", "static-method"})
public class ListDialogField<E> extends DialogField {

	public static class ColumnsDescription {
		private ColumnLayoutData[] columns;
		private String[] headers;
		private boolean drawLines;

		public ColumnsDescription(ColumnLayoutData[] columns, String[] headers, boolean drawLines) {
			this.columns= columns;
			this.headers= headers;
			this.drawLines= drawLines;
		}

		public ColumnsDescription(String[] headers, boolean drawLines) {
			this(createColumnWeightData(headers.length), headers, drawLines);
		}

		public ColumnsDescription(int nColumns, boolean drawLines) {
			this(createColumnWeightData(nColumns), null, drawLines);
		}

		private static ColumnLayoutData[] createColumnWeightData(int nColumns) {
			ColumnLayoutData[] data= new ColumnLayoutData[nColumns];
			for (int i= 0; i < nColumns; i++) {
				data[i]= new ColumnWeightData(1);
			}
			return data;
		}
	}

	protected TableViewer fTable;
	protected Control fTableControl;
	protected ILabelProvider fLabelProvider;
	protected ListViewerAdapter fListViewerAdapter;
	protected List<E> fElements;
	protected ViewerComparator fViewerComparator;

	protected String[] fButtonLabels;
	private Button[] fButtonControls;

	private boolean[] fButtonsEnabled;

	private int fRemoveButtonIndex;
	private int fUpButtonIndex;
	private int fDownButtonIndex;

	private Label fLastSeparator;


	private Composite fButtonsControl;
	private ISelection fSelectionWhenEnabled;

	private IListAdapter<E> fListAdapter;

	private Object fParentElement;

	private ColumnsDescription fTableColumns;


	/**
	 * Creates the <code>ListDialogField</code>.
	 * @param adapter A listener for button invocation, selection changes. Can
	 * be <code>null</code>.
	 * @param buttonLabels The labels of all buttons: <code>null</code> is a valid array entry and
	 * marks a separator.
	 * @param lprovider The label provider to render the table entries
	 */
	public ListDialogField(IListAdapter<E> adapter, String[] buttonLabels, ILabelProvider lprovider) {
		super();
		fListAdapter= adapter;

		fLabelProvider= lprovider;
		fListViewerAdapter= new ListViewerAdapter();
		fParentElement= this;

		fElements= new ArrayList<E>(10);

		fButtonLabels= buttonLabels;
		if (fButtonLabels != null) {
			int nButtons= fButtonLabels.length;
			fButtonsEnabled= new boolean[nButtons];
			for (int i= 0; i < nButtons; i++) {
				fButtonsEnabled[i]= true;
			}
		}

		fTable= null;
		fTableControl= null;
		fButtonsControl= null;
		fTableColumns= null;

		fRemoveButtonIndex= -1;
		fUpButtonIndex= -1;
		fDownButtonIndex= -1;
	}

	/**
	 * Sets the index of the 'remove' button in the button label array passed in the constructor.
	 * The behavior of the button marked as the 'remove' button will then be handled internally.
	 * (enable state, button invocation behavior)
	 */
	public void setRemoveButtonIndex(int removeButtonIndex) {
		Assert.isTrue(removeButtonIndex < fButtonLabels.length);
		fRemoveButtonIndex= removeButtonIndex;
	}

	/**
	 * Sets the index of the 'up' button in the button label array passed in the constructor.
	 * The behavior of the button marked as the 'up' button will then be handled internally.
	 * (enable state, button invocation behavior)
	 */
	public void setUpButtonIndex(int upButtonIndex) {
		Assert.isTrue(upButtonIndex < fButtonLabels.length);
		fUpButtonIndex= upButtonIndex;
	}

	/**
	 * Sets the index of the 'down' button in the button label array passed in the constructor.
	 * The behavior of the button marked as the 'down' button will then be handled internally.
	 * (enable state, button invocation behavior)
	 */
	public void setDownButtonIndex(int downButtonIndex) {
		Assert.isTrue(downButtonIndex < fButtonLabels.length);
		fDownButtonIndex= downButtonIndex;
	}

	/**
	 * Sets the viewer comparator.
	 * @param viewerComparator The viewer comparator to set
	 */
	public void setViewerComparator(ViewerComparator viewerComparator) {
		fViewerComparator= viewerComparator;
	}

	public void setTableColumns(ColumnsDescription column) {
		fTableColumns= column;
	}



	// ------ adapter communication

	private void buttonPressed(int index) {
		if (!managedButtonPressed(index) && fListAdapter != null) {
			fListAdapter.customButtonPressed(this, index);
		}
	}

	/**
	 * Checks if the button pressed is handled internally
	 * @return Returns true if button has been handled.
	 */
	protected boolean managedButtonPressed(int index) {
		if (index == fRemoveButtonIndex) {
			remove();
		} else if (index == fUpButtonIndex) {
			up();
			if (!fButtonControls[index].isEnabled() && fDownButtonIndex != -1) {
				fButtonControls[fDownButtonIndex].setFocus();
			}
		} else if (index == fDownButtonIndex) {
			down();
			if (!fButtonControls[index].isEnabled() && fUpButtonIndex != -1) {
				fButtonControls[fUpButtonIndex].setFocus();
			}
		} else {
			return false;
		}
		return true;
	}


	// ------ layout helpers

	/*
	 * @see DialogField#doFillIntoGrid
	 */
	@Override
	public Control[] doFillIntoGrid(Composite parent, int nColumns) {
		PixelConverter converter= new PixelConverter(parent);

		assertEnoughColumns(nColumns);

		Label label= getLabelControl(parent);
		GridData gd= gridDataForLabel(1);
		gd.verticalAlignment= GridData.BEGINNING;
		label.setLayoutData(gd);

		Control list= getListControl(parent);
		gd= new GridData();
		gd.horizontalAlignment= GridData.FILL;
		gd.grabExcessHorizontalSpace= false;
		gd.verticalAlignment= GridData.FILL;
		gd.grabExcessVerticalSpace= true;
		gd.horizontalSpan= nColumns - 2;
		gd.widthHint= converter.convertWidthInCharsToPixels(50);
		gd.heightHint= converter.convertHeightInCharsToPixels(6);

		list.setLayoutData(gd);

		Composite buttons= getButtonBox(parent);
		gd= new GridData();
		gd.horizontalAlignment= GridData.FILL;
		gd.grabExcessHorizontalSpace= false;
		gd.verticalAlignment= GridData.FILL;
		gd.grabExcessVerticalSpace= true;
		gd.horizontalSpan= 1;
		buttons.setLayoutData(gd);

		return new Control[] { label, list, buttons };
	}

	/*
	 * @see DialogField#getNumberOfControls
	 */
	@Override
	public int getNumberOfControls() {
		return 3;
	}

	/**
	 * Sets the minimal width of the buttons. Must be called after widget creation.
	 */
	public void setButtonsMinWidth(int minWidth) {
		if (fLastSeparator != null) {
			((GridData)fLastSeparator.getLayoutData()).widthHint= minWidth;
		}
	}

	// ------- focus methods

	/*
	 * @see DialogField#setFocus
	 */
	@Override
	public boolean setFocus() {
		if (fTable != null && isOkToUse(fTable.getControl())) {
			fTable.getControl().setFocus();
		}
		return true;
	}

	// ------ UI creation

	/**
	 * Returns the list control. When called the first time, the control will be created.
	 * @param parent The parent composite when called the first time, or <code>null</code>
	 * after.
	 */
	public Control getListControl(Composite parent) {
		if (fTableControl == null) {
			assertCompositeNotNull(parent);

			if (fTableColumns == null) {
				fTable= createTableViewer(parent);
				Table tableControl= fTable.getTable();

				fTableControl= tableControl;
				tableControl.setLayout(new TableLayout());
			} else {
				TableLayoutComposite composite= new TableLayoutComposite(parent, SWT.NONE);
				composite.setFont(parent.getFont());
				fTableControl= composite;

				fTable= createTableViewer(composite);
				Table tableControl= fTable.getTable();

				tableControl.setHeaderVisible(fTableColumns.headers != null);
				tableControl.setLinesVisible(fTableColumns.drawLines);
				ColumnLayoutData[] columns= fTableColumns.columns;
				for (int i= 0; i < columns.length; i++) {
					composite.addColumnData(columns[i]);
					TableColumn column= new TableColumn(tableControl, SWT.NONE);
					//tableLayout.addColumnData(columns[i]);
					if (fTableColumns.headers != null) {
						column.setText(fTableColumns.headers[i]);
					}
				}
			}

			fTable.getTable().addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					handleKeyPressed(e);
				}
			});

			//fTableControl.setLayout(tableLayout);

			fTable.setContentProvider(fListViewerAdapter);
			fTable.setLabelProvider(fLabelProvider);
			fTable.addSelectionChangedListener(fListViewerAdapter);
			fTable.addDoubleClickListener(fListViewerAdapter);

			fTable.setInput(fParentElement);

			if (fViewerComparator != null) {
				fTable.setComparator(fViewerComparator);
			}

			fTableControl.setEnabled(isEnabled());
			if (fSelectionWhenEnabled != null) {
				selectElements(fSelectionWhenEnabled);
			}
		}
		return fTableControl;
	}

	/**
	 * Returns the internally used table viewer.
	 */
	public TableViewer getTableViewer() {
		return fTable;
	}

	/*
	 * Subclasses may override to specify a different style.
	 */
	protected int getListStyle(){
		int style=  SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL ;
		if (fTableColumns != null) {
			style |= SWT.FULL_SELECTION;
		}
		return style;
	}

	protected TableViewer createTableViewer(Composite parent) {
		Table table= new Table(parent, getListStyle());
		table.setFont(parent.getFont());
		return new TableViewer(table);
	}

	protected Button createButton(Composite parent, String label, SelectionListener listener) {
		Button button= new Button(parent, SWT.PUSH);
		button.setFont(parent.getFont());
		button.setText(label);
		button.addSelectionListener(listener);
		GridData gd= new GridData();
		gd.horizontalAlignment= GridData.FILL;
		gd.grabExcessHorizontalSpace= true;
		gd.verticalAlignment= GridData.BEGINNING;
		gd.widthHint = SWTUtil.getButtonWidthHint(button);

		button.setLayoutData(gd);

		return button;
	}

	private Label createSeparator(Composite parent) {
		Label separator= new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator.setFont(parent.getFont());
		separator.setVisible(false);
		GridData gd= new GridData();
		gd.horizontalAlignment= GridData.FILL;
		gd.verticalAlignment= GridData.BEGINNING;
		gd.verticalIndent= 4;
		separator.setLayoutData(gd);
		return separator;
	}

	/**
	 * Returns the composite containing the buttons. When called the first time, the control
	 * will be created.
	 * @param parent The parent composite when called the first time, or <code>null</code>
	 * after.
	 */
	public Composite getButtonBox(Composite parent) {
		if (fButtonsControl == null) {
			assertCompositeNotNull(parent);

			SelectionListener listener= new SelectionListener() {
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					doButtonSelected(e);
				}
				@Override
				public void widgetSelected(SelectionEvent e) {
					doButtonSelected(e);
				}
			};

			Composite contents= new Composite(parent, SWT.NONE);
			contents.setFont(parent.getFont());
			GridLayout layout= new GridLayout();
			layout.marginWidth= 0;
			layout.marginHeight= 0;
			contents.setLayout(layout);

			if (fButtonLabels != null) {
				fButtonControls= new Button[fButtonLabels.length];
				for (int i= 0; i < fButtonLabels.length; i++) {
					String currLabel= fButtonLabels[i];
					if (currLabel != null) {
						fButtonControls[i]= createButton(contents, currLabel, listener);
						fButtonControls[i].setEnabled(isEnabled() && fButtonsEnabled[i]);
					} else {
						fButtonControls[i]= null;
						createSeparator(contents);
					}
				}
			}

			fLastSeparator= createSeparator(contents);

			updateButtonState();
			fButtonsControl= contents;
		}

		return fButtonsControl;
	}

	private void doButtonSelected(SelectionEvent e) {
		if (fButtonControls != null) {
			for (int i= 0; i < fButtonControls.length; i++) {
				if (e.widget == fButtonControls[i]) {
					buttonPressed(i);
					return;
				}
			}
		}
	}

	/**
	 * Handles key events in the table viewer. Specifically
	 * when the delete key is pressed.
	 */
	protected void handleKeyPressed(KeyEvent event) {
		if (event.character == SWT.DEL && event.stateMask == 0) {
			if (fRemoveButtonIndex != -1 && isButtonEnabled(fTable.getSelection(), fRemoveButtonIndex)) {
				managedButtonPressed(fRemoveButtonIndex);
			}
		}
	}

	// ------ enable / disable management

	/*
	 * @see DialogField#dialogFieldChanged
	 */
	@Override
	public void dialogFieldChanged() {
		super.dialogFieldChanged();
		updateButtonState();
	}

	/*
	 * Updates the enable state of the all buttons
	 */
	protected void updateButtonState() {
		if (fButtonControls != null && isOkToUse(fTableControl) && fTableControl.isEnabled()) {
			ISelection sel= fTable.getSelection();
			for (int i= 0; i < fButtonControls.length; i++) {
				Button button= fButtonControls[i];
				if (isOkToUse(button)) {
					button.setEnabled(isButtonEnabled(sel, i));
				}
			}
		}
	}

	protected boolean getManagedButtonState(ISelection sel, int index) {
		if (index == fRemoveButtonIndex) {
			return !sel.isEmpty();
		} else if (index == fUpButtonIndex) {
			return !sel.isEmpty() && canMoveUp();
		} else if (index == fDownButtonIndex) {
			return !sel.isEmpty() && canMoveDown();
		}
		return true;
	}

	/*
	 * @see DialogField#updateEnableState
	 */
	@Override
	protected void updateEnableState() {
		super.updateEnableState();

		boolean enabled= isEnabled();
		if (isOkToUse(fTableControl)) {
			if (!enabled) {
				if (fSelectionWhenEnabled == null) {
					fSelectionWhenEnabled= fTable.getSelection();
					selectElements(null);
				}
			} else if (fSelectionWhenEnabled != null) {
					selectElements(fSelectionWhenEnabled);
					fSelectionWhenEnabled= null;
			}
			fTableControl.setEnabled(enabled);
		}
		updateButtonState();
	}

	/**
	 * Sets a button enabled or disabled.
	 */
	public void enableButton(int index, boolean enable) {
		if (fButtonsEnabled != null && index < fButtonsEnabled.length) {
			fButtonsEnabled[index]= enable;
			updateButtonState();
		}
	}

	private boolean isButtonEnabled(ISelection sel, int index) {
		boolean extraState= getManagedButtonState(sel, index);
		return isEnabled() && extraState && fButtonsEnabled[index];
	}


	// ------ model access

	/**
	 * Sets the elements shown in the list.
	 */
	public void setElements(Collection<E> elements) {
		fElements= new ArrayList<E>(elements);
		if (isOkToUse(fTableControl)) {
			fTable.refresh();
		}
		dialogFieldChanged();
	}

	/**
	 * Gets the elements shown in the list.
	 * The list returned is a copy, so it can be modified by the user.
	 */
	public List<E> getElements() {
		return new ArrayList<E>(fElements);
	}

	/**
	 * Gets the elements shown at the given index.
	 */
	public E getElement(int index) {
		return fElements.get(index);
	}

	/**
	* Gets the index of an element in the list or -1 if element is not in list.
	*/
	public int getIndexOfElement(E elem) {
		return fElements.indexOf(elem);
	}

	/**
	 * Replaces an element.
	 */
	public void replaceElement(E oldElement, E newElement) throws IllegalArgumentException {
		int idx= fElements.indexOf(oldElement);
		if (idx != -1) {
			fElements.set(idx, newElement);
			if (isOkToUse(fTableControl)) {
				List<E> selected= getSelectedElements();
				if (selected.remove(oldElement)) {
					selected.add(newElement);
				}
				fTable.refresh();
				selectElements(new StructuredSelection(selected));
			}
			dialogFieldChanged();
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Notifies clients that the element has changed.
	 */
	public void elementChanged(E element) throws IllegalArgumentException {
		if (fElements.contains(element)) {
			if (isOkToUse(fTableControl)) {
				fTable.update(element, null);
			}
			dialogFieldChanged();
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Adds an element at the end of the list.
	 */
	public boolean addElement(E element) {
		return addElement(element, fElements.size());
	}

	/**
	 * Adds an element at a position.
	 */
	public boolean addElement(E element, int index) {
		if (fElements.contains(element)) {
			return false;
		}
		fElements.add(index, element);
		if (isOkToUse(fTableControl)) {
			fTable.refresh();
			fTable.setSelection(new StructuredSelection(element));
		}

		dialogFieldChanged();
		return true;
	}

	/**
	 * Adds elements at the given index
	 */
	public boolean addElements(List<E> elements, int index) {

		int nElements= elements.size();

		if (nElements > 0 && index >= 0 && index <= fElements.size()) {
			// filter duplicated
			ArrayList<E> elementsToAdd= new ArrayList<E>(nElements);

			for (int i= 0; i < nElements; i++) {
				E elem= elements.get(i);
				if (!fElements.contains(elem)) {
					elementsToAdd.add(elem);
				}
			}
			if (!elementsToAdd.isEmpty()) {
				fElements.addAll(index, elementsToAdd);
				if (isOkToUse(fTableControl)) {
					if (index == fElements.size()) {
						fTable.add(elementsToAdd.toArray());
					} else {
						for (int i= elementsToAdd.size() - 1; i >= 0 ; i--) {
							fTable.insert(elementsToAdd.get(i), index);
						}
					}
					fTable.setSelection(new StructuredSelection(elementsToAdd));
				}
				dialogFieldChanged();
				return true;
			}
		}
		return false;
	}

	/**
	 * Adds elements at the end of the list.
	 */
	public boolean addElements(List<E> elements) {
		return addElements(elements, fElements.size());
	}



	/**
	 * Adds an element at a position.
	 */
	public void removeAllElements() {
		if (fElements.size() > 0) {
			fElements.clear();
			if (isOkToUse(fTableControl)) {
				fTable.refresh();
			}
			dialogFieldChanged();
		}
	}

	/**
	 * Removes an element from the list.
	 */
	public void removeElement(Object element) throws IllegalArgumentException {
		if (fElements.remove(element)) {
			if (isOkToUse(fTableControl)) {
				fTable.remove(element);
			}
			dialogFieldChanged();
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Removes elements from the list.
	 */
	public void removeElements(List<E> elements) {
		if (elements.size() > 0) {
			fElements.removeAll(elements);
			if (isOkToUse(fTableControl)) {
				fTable.remove(elements.toArray());
			}
			dialogFieldChanged();
		}
	}

	/**
	 * Gets the number of elements
	 */
	public int getSize() {
		return fElements.size();
	}


	public void selectElements(ISelection selection) {
		fSelectionWhenEnabled= selection;
		if (isOkToUse(fTableControl)) {
			fTable.setSelection(selection, true);
		}
	}

	public void selectFirstElement() {
		Object element= null;
		if (fViewerComparator != null) {
			Object[] arr= fElements.toArray();
			fViewerComparator.sort(fTable, arr);
			if (arr.length > 0) {
				element= arr[0];
			}
		} else {
			if (fElements.size() > 0) {
				element= fElements.get(0);
			}
		}
		if (element != null) {
			selectElements(new StructuredSelection(element));
		}
	}

	public void editElement(Object element) {
		if (isOkToUse(fTableControl)) {
			fTable.refresh(element);
			fTable.editElement(element, 0);
		}
	}


	public void postSetSelection(final ISelection selection) {
		if (isOkToUse(fTableControl)) {
			Display d= fTableControl.getDisplay();
			d.asyncExec(new Runnable() {
				@Override
				public void run() {
					if (isOkToUse(fTableControl)) {
						selectElements(selection);
					}
				}
			});
		}
	}

	/**
	 * Refreshes the table.
	 */
	@Override
	public void refresh() {
		super.refresh();
		if (isOkToUse(fTableControl)) {
			fTable.refresh();
		}
	}

	// ------- list maintenance

	private List<E> moveUp(List<E> elements, List<E> move) {
		int nElements= elements.size();
		List<E> res= new ArrayList<E>(nElements);
		E floating= null;
		for (int i= 0; i < nElements; i++) {
			E curr= elements.get(i);
			if (move.contains(curr)) {
				res.add(curr);
			} else {
				if (floating != null) {
					res.add(floating);
				}
				floating= curr;
			}
		}
		if (floating != null) {
			res.add(floating);
		}
		return res;
	}

	private void moveUp(List<E> toMoveUp) {
		if (toMoveUp.size() > 0) {
			setElements(moveUp(fElements, toMoveUp));
			fTable.reveal(toMoveUp.get(0));
		}
	}

	private void moveDown(List<E> toMoveDown) {
		if (toMoveDown.size() > 0) {
			setElements(reverse(moveUp(reverse(fElements), toMoveDown)));
			fTable.reveal(toMoveDown.get(toMoveDown.size() - 1));
		}
	}

	private List<E> reverse(List<E> p) {
		List<E> reverse= new ArrayList<E>(p.size());
		for (int i= p.size()-1; i >= 0; i--) {
			reverse.add(p.get(i));
		}
		return reverse;
	}


	private void remove() {
		removeElements(getSelectedElements());
	}

	private void up() {
		moveUp(getSelectedElements());
	}

	private void down() {
		moveDown(getSelectedElements());
	}

	public boolean canMoveUp() {
		if (isOkToUse(fTableControl)) {
			int[] indc= fTable.getTable().getSelectionIndices();
			for (int i= 0; i < indc.length; i++) {
				if (indc[i] != i) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean canMoveDown() {
		if (isOkToUse(fTableControl)) {
			int[] indc= fTable.getTable().getSelectionIndices();
			int k= fElements.size() - 1;
			for (int i= indc.length - 1; i >= 0 ; i--, k--) {
				if (indc[i] != k) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns the selected elements.
	 */
	public List<E> getSelectedElements() {
		List<E> result= new ArrayList<E>();
		if (isOkToUse(fTableControl)) {
			ISelection selection= fTable.getSelection();
			if (selection instanceof IStructuredSelection) {
				Iterator<?> iter= ((IStructuredSelection)selection).iterator();
				while (iter.hasNext()) {
					E element= (E) iter.next();
					result.add(element);
				}
			}
		}
		return result;
	}

	// ------- ListViewerAdapter

	private class ListViewerAdapter implements IStructuredContentProvider, ISelectionChangedListener, IDoubleClickListener {

		// ------- ITableContentProvider Interface ------------

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// will never happen
		}

		@Override
		public void dispose() {
		}

		@Override
		public Object[] getElements(Object obj) {
			return fElements.toArray();
		}

		// ------- ISelectionChangedListener Interface ------------

		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			doListSelected(event);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.IDoubleClickListener#doubleClick(org.eclipse.jface.viewers.DoubleClickEvent)
		 */
		@Override
		public void doubleClick(DoubleClickEvent event) {
			doDoubleClick(event);
		}

	}


	protected void doListSelected(SelectionChangedEvent event) {
		updateButtonState();
		if (fListAdapter != null) {
			fListAdapter.selectionChanged(this);
		}
	}

	protected void doDoubleClick(DoubleClickEvent event) {
		if (fListAdapter != null) {
			fListAdapter.doubleClicked(this);
		}
	}

	/**
	 * @deprecated Use {@link #setViewerComparator(ViewerComparator)} instead}
	 */
	public void setViewerSorter(ViewerSorter sorter) {
		setViewerComparator(sorter);
	}

}
