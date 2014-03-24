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
package melnorme.lang.ide.ui.fields;

import melnorme.util.swt.components.FieldWithListeners;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class CommonTableBasedField<T> extends FieldWithListeners<T> {
	
	@Override
	public Control createComponent(Composite parent) {
		return createTable(parent);
	}
	
	/* ----------------------------------- */
	
	protected Table table;
	protected TableColumnLayout tableLayout;
	
	protected int sortingColumn = 0;
	
	protected Composite createTable(Composite parent) {
		Composite tableComposite = new Composite(parent, SWT.NONE);
		
		table = new Table(tableComposite, getCreateTableStyle());
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		tableLayout = new TableColumnLayout();
		createTableColumns();
		tableComposite.setLayout(tableLayout);
		
		return tableComposite;
	}
	
	protected int getCreateTableStyle() {
		return SWT.CHECK | SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION;
	}
	
	protected void createTableColumns() {
	}
	
	protected TableColumn createColumn(final int columnIndex, String text) {
		TableColumn column = new TableColumn(table, SWT.NULL);
		column.setText(text);
		column.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				sortByColumn(columnIndex);
			}
		});
		return column;
	}
	
	@SuppressWarnings("unused")
	protected void sortByColumn(int columnIndex) {
	}
	
	/* ----------------------------------- */
	
	public void saveTableSettings(IDialogSettings dialogStore) {
		int columnCount = table.getColumnCount();
		for (int i = 0; i < columnCount; i++) {
			dialogStore.put("ColumnWidth" + i, table.getColumn(i).getWidth());
		}
		dialogStore.put("SortingColumn", sortingColumn);
	}
	
	public void restoreTableSettings(IDialogSettings dialogStore) {
		if(dialogStore == null)
			return;
		
		restoreColumnWidths(dialogStore);
		table.layout(true);
		try {
			sortingColumn = dialogStore.getInt("SortingColumn");
			sortByColumn(sortingColumn);
		} catch (NumberFormatException e) {
		}
	}
	
	protected void restoreColumnWidths(IDialogSettings dialogStore) {
		int columnCount = table.getColumnCount();
		for(int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
			try {
				int width = dialogStore.getInt("ColumnWidth" + columnIndex);
				restoreColumnLayoutData(columnIndex, width);
			} catch (NumberFormatException e) {
			}
		}
	}
	
	protected void restoreColumnLayoutData(int columnIndex, int width) {
		TableColumn column = table.getColumn(columnIndex);
		restoreColumnLayoutData(columnIndex, column, width);
	}
	
	@SuppressWarnings("unused")
	protected void restoreColumnLayoutData(int columnIndex, TableColumn column, int width) {
		tableLayout.setColumnData(column, new ColumnPixelData(width));
	}
	
}