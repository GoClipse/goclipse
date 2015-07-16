/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package melnorme.util.swt;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Control;


/**
 * SWT Layout Utils.
 * See also org.eclipse.dltk.internal.ui.wizards.dialogfields.LayoutUtil
 */
public class SWTLayoutUtil {
	
	public static <CONTROL extends Control, DATA> CONTROL setLayoutData(CONTROL control, Object layoutData) {
		control.setLayoutData(layoutData);
		return control;
	}
	
	public static FillLayout createFillLayout(int marginWidth, int marginHeight, int spacing) {
		FillLayout fillLayout = new FillLayout();
		fillLayout.marginWidth = marginWidth;
		fillLayout.marginHeight = marginHeight;
		fillLayout.spacing = spacing;
		return fillLayout;
	}
	
	public static GridLayout createGridLayout() {
		return createGridLayout(1);
	}
	
	public static GridLayout createGridLayout(int numColumns) {
		return createGridLayout(numColumns, true);
	}

	/** Creates a default GridLayout with given numColumns , margins, baseControl*/
	public static GridLayout createGridLayout(int numColumns, boolean margins) {
		GridLayout gd = new GridLayout(numColumns, false);
		if(!margins) {
			gd.marginWidth = 0;
			gd.marginHeight = 0;
		}
		return gd;
	}
	
	/* ================ Layout Data Utils ================ */
	
	
	protected static GridData getControlGridData(Control control) {
		Object ld = control.getLayoutData();
		assertTrue(ld instanceof GridData);
		return (GridData) ld;
	}

	public static Point calcSimpleSizeMetrics(Control control) {
		Point size = control.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		return size;
	}
	
	public static Point calcSimpleSizeMetrics(Control control, double widthRatio, double heightRatio) {
		Point size = calcSimpleSizeMetrics(control);
		size.x = (int) (size.x * widthRatio); 
		size.y = (int) (size.y * heightRatio);
		return size;
	}

	
	public static Control setGridDataSize(Control control, int widthHint, int heightHint) {
		GridData gd = getControlGridData(control);
		gd.widthHint = widthHint;
		gd.heightHint = heightHint;
		return control;
	}
	
	
	public static Control setGridDataHeight(Control control, int heightHint) {
		GridData gd = getControlGridData(control);
		gd.heightHint = heightHint;
		return control;
	}
	
	public static Control setGridDataWidth(Control control, int widthHint) {
		GridData gd = getControlGridData(control);
		gd.widthHint = widthHint;
		return control;
	}
	
	
	
	public static Control setGridDataSize(Control control, Point sizeHint) {
		return setGridDataSize(control, sizeHint.x, sizeHint.y);
	}

	public static GridData newRowGridData() {
		return newGridData(true);
	}

	
	public static GridData newGridData() {
		return newHGridData(1, false, false);
	}
	
	public static GridData newGridData(boolean grabHorizontal) {
		return newHGridData(1, grabHorizontal);
	}
	
	public static GridData newGridData(boolean grabHorizontal, boolean grabVertical) {
		return newHGridData(1, grabHorizontal, grabVertical);
	}
	
	public static GridData newGridData(int widthHint, int heightHint) {
		return newHGridData(1, widthHint, heightHint);
	}
	
	
	
	public static GridData newHGridData(int horizontalSpan) {
		return newHGridData(horizontalSpan, false, false);
	}

	public static GridData newHGridData(int horizontalSpan, boolean grabHorizontal) {
		return newHGridData(horizontalSpan, grabHorizontal, false);
	}

	public static GridData newHGridData(int horizontalSpan, boolean grabHorizontal,
			boolean grabVertical) {
		GridData gd = new GridData();
		gd.horizontalSpan = horizontalSpan;
		gd.horizontalAlignment = SWT.FILL;
		gd.verticalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = grabHorizontal;
		gd.grabExcessVerticalSpace = grabVertical;
		return gd;
	}

	public static GridData newHGridData(int horizontalSpan, int widthHint, int heightHint) {
		GridData gd = new GridData();
		gd.horizontalSpan = horizontalSpan;
		gd.horizontalAlignment = SWT.FILL;
		gd.verticalAlignment = SWT.FILL;
		gd.widthHint = widthHint;
		gd.heightHint = heightHint;
		return gd;
	}
	
	/* ------------------------------------------------------- */
	
	/**
	 * Sets the span of a control. Assumes that GridData is used.
	 */
	public static void setHorizontalSpan(Control control, int span) {
		Object ld = control.getLayoutData();
		if (ld instanceof GridData) {
			((GridData) ld).horizontalSpan = span;
		} else if (span != 1) {
			GridData gd = new GridData();
			gd.horizontalSpan = span;
			control.setLayoutData(gd);
		}
	}

	/**
	 * Sets the width hint of a control. Assumes that GridData is used.
	 */
	public static void setWidthHint(Control control, int widthHint) {
		Object ld = control.getLayoutData();
		if (ld instanceof GridData) {
			((GridData) ld).widthHint = widthHint;
		}
	}

	/**
	 * Sets the heightHint hint of a control. Assumes that GridData is used.
	 */
	public static void setHeightHint(Control control, int heightHint) {
		Object ld = control.getLayoutData();
		if (ld instanceof GridData) {
			((GridData) ld).heightHint = heightHint;
		}
	}

	/**
	 * Sets the horizontal indent of a control. Assumes that GridData is used.
	 */
	public static void setHorizontalIndent(Control control, int horizontalIndent) {
		Object ld = control.getLayoutData();
		if (ld instanceof GridData) {
			((GridData) ld).horizontalIndent = horizontalIndent;
		}
	}

	/** Enables the horizontal grabbing of a control, if GridData is used. */
	public static void enableHorizontalGrabbing(Control control) {
		Object ld = control.getLayoutData();
		if (ld instanceof GridData) {
			((GridData) ld).grabExcessHorizontalSpace = true;
		}
	}

	/** Enables the vertical grabbing of a control, if GridData is used. */
	public static void enableVerticalGrabbing(Control control) {
		Object ld = control.getLayoutData();
		if (ld instanceof GridData) {
			((GridData) ld).grabExcessVerticalSpace = true;
		}
	}

	/*** Enables vertical and horizontal grabbing of a control, if GridData is
	 * used. */
	public static void enableDiagonalGrabbing(Control control) {
		Object ld = control.getLayoutData();
		if (ld instanceof GridData) {
			((GridData) ld).grabExcessHorizontalSpace = true;
			((GridData) ld).grabExcessVerticalSpace = true;
		}
	}


	/** Enables a diagonal expand GridData (grab and fill in both directions).
	 *  Creates a GridData if one doesn't exist already. */
	public static void enableDiagonalExpand(Control control) {
		GridData gd = getGD(control);
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		gd.horizontalAlignment = SWT.FILL;
		gd.verticalAlignment = SWT.FILL;
	}
	
	/** Sets horizontal and vertical grabbing.
	 *  Creates a GridData if one doesn't exist already. */
	public static void setHVGrabbing(Control control, boolean grabHorizontal, boolean grabVertical) {
		GridData gd = getGD(control);
		gd.grabExcessHorizontalSpace = grabHorizontal;
		gd.grabExcessVerticalSpace = grabVertical;
	}
	

	/** Get's the control's GridData. Creates one if one doesn't exist already. */
	public static GridData getGD(Control control) {
		Object ld = control.getLayoutData();
		GridData gd;
		if(ld == null) {
			gd = new GridData();
			control.setLayoutData(gd);
		} else {
			assertTrue(ld instanceof GridData);
			gd = (GridData) ld;
		}
		return gd;
	}

}