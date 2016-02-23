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
package melnorme.lang.ide.ui.preferences;


import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.widgets.Composite;

import melnorme.lang.ide.ui.preferences.pages.DownloadToolTextField;
import melnorme.lang.ide.ui.utils.operations.BasicUIOperation;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.components.AbstractCompositeWidget;
import melnorme.util.swt.components.fields.ButtonTextField;

public abstract class AbstractToolLocationGroup extends AbstractCompositeWidget {
	
	public final ButtonTextField toolLocationField = new DownloadToolTextField("Executable:", "Download...") {
		@Override
		public BasicUIOperation getDownloadButtonHandler() {
			return do_getDownloadButtonHandler(this);
		}
	};
	
	public final String toolName;
	
	public AbstractToolLocationGroup(String toolName) {
		this.toolName = toolName;
		this.subComponents.add(toolLocationField);
	}
	
	@Override
	protected Composite doCreateTopLevelControl(Composite parent) {
		return SWTFactoryUtil.createGroup(parent, toolName + ": ");
	}
	
	@Override
	protected GridLayoutFactory createTopLevelLayout() {
		return GridLayoutFactory.swtDefaults().numColumns(getPreferredLayoutColumns());
	}
	
	@Override
	public int getPreferredLayoutColumns() {
		return 4;
	}
	
	protected abstract BasicUIOperation do_getDownloadButtonHandler(DownloadToolTextField downloadToolTextField);
	
}