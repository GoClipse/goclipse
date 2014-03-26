/*******************************************************************************
 * Copyright (c) 2005, 2012 QNX Software Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     QNX Software Systems - Initial API and implementation
 *     IBM Corporation
 *     Bruno Medeiros - lang modifications
 *******************************************************************************/
package melnorme.lang.ide.ui.launch;

import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.util.swt.ControlAccessibleListener;
import melnorme.util.swt.components.FieldComponent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class LangArgumentsBlock extends FieldComponent<String> {
	
	protected Label fPrgmArgumentsLabel;
	protected Text fPrgmArgumentsText;
	protected Button fArgumentVariablesButton;
	
	@Override
	public Group createComponent(Composite comp) {
		Group topControl = new Group(comp, SWT.NONE);
		Font font = comp.getFont();
		topControl.setFont(font);
		topControl.setLayout(new GridLayout());
		
		topControl.setText(LangUIMessages.LangArgumentsTab_Program_Arguments);
		fPrgmArgumentsText = new Text(topControl, SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
		fPrgmArgumentsText.getAccessible().addAccessibleListener(
				new AccessibleAdapter() {
					@Override
					public void getName(AccessibleEvent e) {
						e.result = LangUIMessages.LangArgumentsTab_Program_Arguments;
					}
				});
		
		GridData gd;
		gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 40;
		gd.widthHint = 100;
		fPrgmArgumentsText.setLayoutData(gd);
		fPrgmArgumentsText.setFont(font);
		fPrgmArgumentsText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent evt) {
				fieldValueChanged(getFieldValue());
			}
		});
		fArgumentVariablesButton = AbstractLaunchConfigurationTabExt.
				createVariablesButton(topControl, LangUIMessages.LangArgumentsTab_Variables, fPrgmArgumentsText); 
		gd = new GridData(GridData.HORIZONTAL_ALIGN_END);
		fArgumentVariablesButton.setLayoutData(gd);
		// need to strip the mnemonic from buttons:
		ControlAccessibleListener.addControlAccessibleListener(fArgumentVariablesButton, 
				fArgumentVariablesButton.getText());
		
		return topControl;
	}
	
	/**
	 * Returns the string in the text widget, or <code>null</code> if empty.
	 * 
	 * @return text or <code>null</code>
	 */
	protected static String getAttributeValueFrom(Text text) {
		String content = text.getText().trim();
		// Bug #131513 - eliminate Windows \r line delimiter
		content = content.replaceAll("\r\n", "\n");  //$NON-NLS-1$//$NON-NLS-2$
		if (content.length() > 0) {
			return content;
		}
		return null;
	}
	
	@Override
	public String getFieldValue() {
		return getAttributeValueFrom(fPrgmArgumentsText);
	}
	
	@Override
	public void setFieldValue(String value) {
		fPrgmArgumentsText.setText(value);
	}
	
}