/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.utils;

import org.eclipse.debug.ui.StringVariableSelectionDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.PixelConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PreferencesUtil;

import melnorme.util.swt.SWTFactoryUtil;

public class ControlUtils {

	public static void createHorizontalSpacer(Composite topControl, int charHeight, PixelConverter pc) {
		SWTFactoryUtil.createLabel(topControl, SWT.LEFT, "", 
			GridDataFactory.fillDefaults().hint(pc.convertHeightInCharsToPixels(charHeight), SWT.DEFAULT).create());
	}

	public static Link createOpenPreferencesDialogLink(final Composite topControl, String linkText) {
		Link link = new Link(topControl, SWT.NONE);
		link.setText(linkText);
		link.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				PreferencesUtil.createPreferenceDialogOn(topControl.getShell(), e.text, null, null);
			}
		});
		return link;
	}
	
	public static void openStringVariableSelectionDialog_ForText(Text text) {
		Shell shell = text.getShell();
		String variable = openStringVariableSelectionDialog(shell);
		if (variable != null) {
			text.insert(variable);
		}
	}
	
	public static String openStringVariableSelectionDialog(final Shell shell) {
		StringVariableSelectionDialog dialog = new StringVariableSelectionDialog(shell);
		dialog.open();
		return dialog.getVariableExpression();
	}
	
}