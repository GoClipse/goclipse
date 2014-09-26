/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *     CDT - certain methods
 *******************************************************************************/
package melnorme.lang.ide.ui.launch;

import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.util.swt.SWTFactoryUtil;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.debug.ui.StringVariableSelectionDialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public abstract class AbstractLaunchConfigurationTabExt extends AbstractLaunchConfigurationTab {
	
	public static String getConfigAttribute(ILaunchConfiguration config, String key, String defaultValue) {
		String projectName = "";
		try {
			projectName = config.getAttribute(key, defaultValue);
		} catch (CoreException ce) {
			LangUIPlugin.logStatus(ce);
		}
		return projectName;
	}
	
	/**
	 * Creates a button that allows user to insert build variables.
	 */
	public static Button createVariablesButton(Composite parent, String label, final Text textField) {
		Button variablesButton = SWTFactoryUtil.createPushButton(parent, label, null);
		variablesButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				handleVariablesButtonSelected(textField);
			}
		});
		return variablesButton;
	}
	
	/**
	 * A variable entry button has been pressed for the given text
	 * field. Prompt the user for a variable and enter the result
	 * in the given field.
	 */
	private static void handleVariablesButtonSelected(Text textField) {
		final Shell shell = textField.getShell();
		StringVariableSelectionDialog dialog = new StringVariableSelectionDialog(shell);
		dialog.open();
		String variable = dialog.getVariableExpression();
		if (variable != null) {
			textField.insert(variable);
		}
	}
	
}