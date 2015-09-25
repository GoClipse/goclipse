/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.launch;

import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.fields.ArgumentsGroupField;
import melnorme.util.swt.ControlAccessibleListener;

import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.widgets.Composite;

public class LangArgumentsBlock2 extends ArgumentsGroupField {
	
	public LangArgumentsBlock2() {
		super(LangUIMessages.LangArgumentsTab_Program_Arguments);
	}
	
	@Override
	protected void createContents_all(Composite topControl) {
		super.createContents_all(topControl);
		
		text.getAccessible().addAccessibleListener(new AccessibleAdapter() {
			@Override
			public void getName(AccessibleEvent e) {
				e.result = LangUIMessages.LangArgumentsTab_Program_Arguments;
			}
		});
		
		// need to strip the mnemonic from buttons:
		ControlAccessibleListener.addControlAccessibleListener(button, button.getText());
	}
	
	/**
	 * @return the string in the text widget, or <code>null</code> if empty.
	 */
	protected static String getAttributeValueFrom(String content) {
		// Bug #131513 - eliminate Windows \r line delimiter
		content = content.replaceAll("\r\n", "\n");  //$NON-NLS-1$//$NON-NLS-2$
		return content;
	}
	
	@Override
	public String getFieldValue() {
		return getAttributeValueFrom(super.getFieldValue());
	}
	
}