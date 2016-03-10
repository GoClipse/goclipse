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


import melnorme.lang.ide.ui.preferences.pages.DownloadToolTextField;
import melnorme.lang.ide.ui.utils.operations.BasicUIOperation;
import melnorme.util.swt.components.AbstractGroupWidget;

public abstract class AbstractToolLocationGroup extends AbstractGroupWidget {
	
	public final String toolName;
	public final DownloadToolTextField toolLocationField;
	
	public AbstractToolLocationGroup(String toolName) {
		super(toolName + ": ", 4);
		this.toolName = toolName;
		
		this.toolLocationField = initToolLocationField();
		addSubComponent(toolLocationField);
	}
	
	protected DownloadToolTextField initToolLocationField() {
		return new DownloadToolTextField() {
			@Override
			public BasicUIOperation getDownloadButtonHandler() {
				return do_getDownloadButtonHandler(this);
			}
		};
	}
	
	protected abstract BasicUIOperation do_getDownloadButtonHandler(DownloadToolTextField toolLocationField);
	
}