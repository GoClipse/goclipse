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

import melnorme.lang.ide.launching.LaunchMessages;

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

public class LangArgumentsBlock {
	
	protected Label fPrgmArgumentsLabel;
	protected Text fPrgmArgumentsText;
	protected Button fArgumentVariablesButton;
	
	protected Group createArgumentsComponent(Composite comp) {
		Font font = comp.getFont();
		Group group = new Group(comp, SWT.NONE);
		group.setFont(font);
		group.setLayout(new GridLayout());
		GridData gd;
		
		group.setText(LaunchMessages.LangArgumentsTab_Program_Arguments);
		fPrgmArgumentsText = new Text(group, SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
		fPrgmArgumentsText.getAccessible().addAccessibleListener(
				new AccessibleAdapter() {
					@Override
					public void getName(AccessibleEvent e) {
						e.result = LaunchMessages.LangArgumentsTab_Program_Arguments;
					}
				});
		gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 40;
		gd.widthHint = 100;
		fPrgmArgumentsText.setLayoutData(gd);
		fPrgmArgumentsText.setFont(font);
		fPrgmArgumentsText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent evt) {
				valueChanged();
			}
		});
		fArgumentVariablesButton = AbstractLaunchConfigurationTabExt.
				createVariablesButton(group, LaunchMessages.LangArgumentsTab_Variables, fPrgmArgumentsText); 
		gd = new GridData(GridData.HORIZONTAL_ALIGN_END);
		fArgumentVariablesButton.setLayoutData(gd);
		// need to strip the mnemonic from buttons:
		ControlAccessibleListener.addControlAccessibleListener(fArgumentVariablesButton, 
				fArgumentVariablesButton.getText());
		
		return group;
	}
	
	protected void valueChanged() {
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
	
	public String getValue() {
		return getAttributeValueFrom(fPrgmArgumentsText);
	}
	
	public void setValue(String value) {
		fPrgmArgumentsText.setText(value);
	}
	
}