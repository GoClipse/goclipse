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
package melnorme.lang.jdt.internal.ui.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import org.eclipse.core.runtime.Assert;

import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;

// Copied from JDT version 3.9.2 
// It is very similar to the correspoding class from DLTK 5.0.0, but has some differences


/**
 * A special composite to layout columns inside a table. The composite is needed since we have
 * to layout the columns "before" the actual table gets layouted. Hence we can't use a normal
 * layout manager.
 * <p>
 * XXX: Should switch to use {@link org.eclipse.jface.layout.TableColumnLayout}.
 * </p>
 */
public class TableLayoutComposite extends Composite {

	/**
	 * The number of extra pixels taken as horizontal trim by the table column.
	 * To ensure there are N pixels available for the content of the column,
	 * assign N+COLUMN_TRIM for the column width.
	 * <p>
	 * XXX: Should either switch to use {@link org.eclipse.jface.layout.TableColumnLayout} or get API from JFace or SWT, see https://bugs.eclipse.org/bugs/show_bug.cgi?id=218483
	 * </p>
	 *
	 * @since 3.1
	 */
	private static int COLUMN_TRIM;
	static {
		String platform= SWT.getPlatform();
		if ("win32".equals(platform)) //$NON-NLS-1$
			COLUMN_TRIM= 4;
		else if ("carbon".equals(platform)) //$NON-NLS-1$
			COLUMN_TRIM= 24;
		else
			COLUMN_TRIM= 3;
	}

	private List<ColumnLayoutData> columns= new ArrayList<ColumnLayoutData>();

	/**
	 * Creates a new <code>TableLayoutComposite</code>.
	 *
	 * @param parent the parent composite
	 * @param style the SWT style
	 */
	public TableLayoutComposite(Composite parent, int style) {
		super(parent, style);
        addControlListener(new ControlAdapter() {
            @Override
			public void controlResized(ControlEvent e) {
                Rectangle area= getClientArea();
                Table table= (Table)getChildren()[0];
                Point preferredSize= computeTableSize(table);
                int width= area.width - 2 * table.getBorderWidth();
                if (preferredSize.y > area.height) {
                    // Subtract the scrollbar width from the total column width
                    // if a vertical scrollbar will be required
                    Point vBarSize = table.getVerticalBar().getSize();
                    width -= vBarSize.x;
                }
                layoutTable(table, width, area, table.getSize().x < area.width);
            }
        });
	}

	/**
	 * Adds a new column of data to this table layout.
	 *
	 * @param data the column layout data
	 */
	public void addColumnData(ColumnLayoutData data) {
		columns.add(data);
	}

	//---- Helpers -------------------------------------------------------------------------------------

	private Point computeTableSize(Table table) {
		Point result= table.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		
		int width= 0;
		int size= columns.size();
		for (int i= 0; i < size; ++i) {
			ColumnLayoutData layoutData= columns.get(i);
			if (layoutData instanceof ColumnPixelData) {
				ColumnPixelData col= (ColumnPixelData) layoutData;
				width += col.width;
				if (col.addTrim) {
					width += COLUMN_TRIM;
				}
			} else if (layoutData instanceof ColumnWeightData) {
				ColumnWeightData col= (ColumnWeightData) layoutData;
				width += col.minimumWidth;
			} else {
				Assert.isTrue(false, "Unknown column layout data"); //$NON-NLS-1$
			}
		}
		if (width > result.x)
			result.x= width;
		return result;
	}

	private void layoutTable(Table table, int width, Rectangle area, boolean increase) {
		// XXX: Layout is being called with an invalid value the first time
		// it is being called on Linux. This method resets the
		// Layout to null so we make sure we run it only when
		// the value is OK.
		if (width <= 1)
			return;

		TableColumn[] tableColumns= table.getColumns();
		int size= Math.min(columns.size(), tableColumns.length);
		int[] widths= new int[size];
		int fixedWidth= 0;
		int numberOfWeightColumns= 0;
		int totalWeight= 0;

		// First calc space occupied by fixed columns
		for (int i= 0; i < size; i++) {
			ColumnLayoutData col= columns.get(i);
			if (col instanceof ColumnPixelData) {
				ColumnPixelData cpd= (ColumnPixelData) col;
				int pixels= cpd.width;
				if (cpd.addTrim) {
					pixels += COLUMN_TRIM;
				}
				widths[i]= pixels;
				fixedWidth += pixels;
			} else if (col instanceof ColumnWeightData) {
				ColumnWeightData cw= (ColumnWeightData) col;
				numberOfWeightColumns++;
				// first time, use the weight specified by the column data, otherwise use the actual width as the weight
				// int weight = firstTime ? cw.weight : tableColumns[i].getWidth();
				int weight= cw.weight;
				totalWeight += weight;
			} else {
				Assert.isTrue(false, "Unknown column layout data"); //$NON-NLS-1$
			}
		}

		// Do we have columns that have a weight
		if (numberOfWeightColumns > 0) {
			// Now distribute the rest to the columns with weight.
			int rest= width - fixedWidth;
			int totalDistributed= 0;
			for (int i= 0; i < size; ++i) {
				ColumnLayoutData col= columns.get(i);
				if (col instanceof ColumnWeightData) {
					ColumnWeightData cw= (ColumnWeightData) col;
					// calculate weight as above
					// int weight = firstTime ? cw.weight : tableColumns[i].getWidth();
					int weight= cw.weight;
					int pixels= totalWeight == 0 ? 0 : weight * rest / totalWeight;
					if (pixels < cw.minimumWidth)
						pixels= cw.minimumWidth;
					totalDistributed += pixels;
					widths[i]= pixels;
				}
			}

			// Distribute any remaining pixels to columns with weight.
			int diff= rest - totalDistributed;
			for (int i= 0; diff > 0; ++i) {
				if (i == size)
					i= 0;
				ColumnLayoutData col= columns.get(i);
				if (col instanceof ColumnWeightData) {
					++widths[i];
					--diff;
				}
			}
		}

		if (increase) {
			table.setSize(area.width, area.height);
		}
		for (int i= 0; i < size; i++) {
			tableColumns[i].setWidth(widths[i]);
		}
		if (!increase) {
			table.setSize(area.width, area.height);
		}
	}
}
