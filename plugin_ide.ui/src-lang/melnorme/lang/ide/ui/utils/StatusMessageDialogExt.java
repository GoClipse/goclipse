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
package melnorme.lang.ide.ui.utils;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;

import melnorme.util.swt.SWTFactoryUtil;
import melnorme.utilbox.fields.validation.IDetailsMessage;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.status.StatusException;

public class StatusMessageDialogExt extends StatusMessageDialog2 {

	public StatusMessageDialogExt(Shell shell, String title, StatusException statusMessage) {
		super(shell, title, statusMessage);
	}
	
	@Override
	protected IconAndMessageWidget createIconAndMessageWidget() {
		return new IconAndMessageWidgetExt();
	}
	
	/**
	 * Extension to {@link IconAndMessageWidget}, allows displaying additional info from {@link IDetailsMessage}
	 * in a control with links in texts.
	 */
	public static class IconAndMessageWidgetExt extends IconAndMessageWidget {
		
		protected Link helpControl;
		
		@Override
		protected void createMessageControl(Composite topControl) {
			super.createMessageControl(topControl);
			
			if(statusMessage instanceof IDetailsMessage) {
				IDetailsMessage detailsMessage = (IDetailsMessage) statusMessage;
				createDetailsMessage(topControl, detailsMessage);
			}
		}
		
		protected void createDetailsMessage(Composite topControl, IDetailsMessage detailsMessage) {
			if(!detailsMessage.getDetailsMessage2().isPresent()) {
				return;
			}
			String additionalMessage = "\n" + detailsMessage.getDetailsMessage2().get();
			
			helpControl = SWTFactoryUtil.createLink(topControl, SWT.WRAP, additionalMessage, 
				gdfFillDefaults().grab(true, true).span(2, 1).create()
			);
			
			helpControl.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					String uri = e.text;
					if(uri.startsWith("http")) {
						Program.launch(uri);
					} else if(uri.startsWith("pref:")) {
						String prefId = StringUtil.removeStart("pref:", uri);
						WorkbenchUtils.openPreferencePage(helpControl.getShell(), prefId);
					} else {
						UIOperationsStatusHandler.handleInternalError("Unknown link URI:\n" + uri, null);
					}
				}
			});
		}
		
		public Link getHelpControl() {
			return helpControl;
		}
	}
	
}