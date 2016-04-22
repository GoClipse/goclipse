/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.util.swt.components;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.widgets.Composite;

import melnorme.util.swt.SWTFactoryUtil;

public abstract class AbstractGroupWidget extends CompositeWidget {
	
	public String groupLabel;
	
	public AbstractGroupWidget(String groupLabel, int layoutColumns) {
		super(true);
		this.groupLabel = assertNotNull(groupLabel);
		this.layoutColumns = layoutColumns;
	}
	
	@Override
	protected Composite doCreateTopLevelControl(Composite parent) {
		return SWTFactoryUtil.createGroup(parent, groupLabel);
	}
	
	@Override
	protected GridLayoutFactory createTopLevelLayout() {
		return GridLayoutFactory.swtDefaults().numColumns(getPreferredLayoutColumns());
	}
	
}