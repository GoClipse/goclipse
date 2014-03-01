/*******************************************************************************
 * Copyright (c) 2007 DSource.org and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial implementation
 *******************************************************************************/
package melnorme.util.swt;

import org.eclipse.swt.widgets.Composite;

/**
 * A composite that lays out children in rows. 
 * Uses GridLayout.
 */
public class GridComposite extends BaseComposite {

	/** Creates a 1 column composite with no margins and SWT default spacing. */
	public GridComposite(Composite parent) {
		this(parent, false);
	}

	/** Creates a 1 column composite with optional margins and default spacing. */
	public GridComposite(Composite parent, boolean margins) {
		this(parent, 1, margins);
	}
	
	/** Creates a composite with given numCol columns and no margins */
	public GridComposite(Composite parent, int numCol) {
		this(parent, numCol, false);
	}

	/** Creates a composite with given numCol columns and optional margins */
	public GridComposite(Composite parent, int numColumns, boolean margins) {
		super(parent);
		setLayout(SWTLayoutUtil.createGridLayout(numColumns, margins));
	}

 }