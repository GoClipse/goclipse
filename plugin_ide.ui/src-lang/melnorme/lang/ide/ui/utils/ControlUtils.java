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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.debug.ui.StringVariableSelectionDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.PixelConverter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.views.navigator.ResourceComparator;

import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.concurrency.OperationCancellation;

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
	
	public static String openProgramPathDialog(IProject project, Button button) {
		ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(
			button.getShell(), new WorkbenchLabelProvider(), new WorkbenchContentProvider());
		dialog.setTitle(LangUIMessages.ProgramPathDialog_title);
		dialog.setMessage(LangUIMessages.ProgramPathDialog_message);
		
		dialog.setInput(project);
		dialog.setComparator(new ResourceComparator(ResourceComparator.NAME));
		if (dialog.open() == IDialogConstants.OK_ID) {
			IResource resource = (IResource) dialog.getFirstResult();
			return resource.getProjectRelativePath().toPortableString();
		}
		return null;
	}
	
	public static <T> T setElementsAndOpenDialog(ElementListSelectionDialog elementListDialog, Indexable<T> configs) 
			throws OperationCancellation {
		return setElementsAndOpenDialog(elementListDialog, configs.toArray());
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T setElementsAndOpenDialog(ElementListSelectionDialog elementListDialog, Object[] elements)
			throws OperationCancellation {
		elementListDialog.setElements(elements);
		int result = elementListDialog.open();
		if(result != Window.OK) {
			throw new OperationCancellation();
		}
		
		return (T) elementListDialog.getFirstResult();
	}
	
}