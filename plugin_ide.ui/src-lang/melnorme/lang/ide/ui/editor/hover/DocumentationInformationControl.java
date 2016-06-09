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
package melnorme.lang.ide.ui.editor.hover;

import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import melnorme.utilbox.misc.ReflectionUtils;
import melnorme.utilbox.misc.ReflectionUtils.IllegalFieldValue;

// TODO: DefaultInformationControl has several limitations, it should be rewritten
public class DocumentationInformationControl extends DefaultInformationControl {
	
	public static class DocumentationInformationControlCreator implements IInformationControlCreator {
		
		@Override
		public IInformationControl createInformationControl(Shell parent) {
			return new DocumentationInformationControl(parent);
		}
		
	}
	/* -----------------  ----------------- */
	
	public DocumentationInformationControl(Shell parent) {
		super(parent, true);
	}
	
	@Override
	protected void createContent(Composite parent) {
		try {
			ReflectionUtils.writeField(this, "fAdditionalTextStyles", SWT.NONE | SWT.WRAP);
		} catch(NoSuchFieldException | IllegalFieldValue e) {
			// Ignore
		}
		super.createContent(parent);
		FillLayout layout= (FillLayout)parent.getLayout();
		layout.marginWidth = 4;
		layout.marginHeight = 4;
	}
	
	@Override
	public void setInformation(String content) {
		super.setInformation(content);
	}
	
	@Override
	public IInformationControlCreator getInformationPresenterControlCreator() {
		return null; // return null, prevent control from being changed on focus
	}
}