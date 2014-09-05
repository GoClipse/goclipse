/*******************************************************************************
 * Copyright (c) 2004, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.tools.console;

import melnorme.lang.ide.ui.LangImages;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.console.IConsoleView;

/**
 * Toggles console auto-scroll
 */
public class ScrollLockAction extends Action {
	
	private IConsoleView fConsoleView;
	
	public ScrollLockAction(IConsoleView consoleView) {
		super(ConsoleMessages.ScrollLockAction_Name);
		fConsoleView = consoleView;
		
		setToolTipText(ConsoleMessages.ScrollLockAction_Tooltip);
		setImageDescriptor(LangImages.IMG_SCROLL_LOCK.getDescriptor());
		boolean checked = fConsoleView.getScrollLock();
		setChecked(checked);
	}
	
	@Override
	public void run() {
		fConsoleView.setScrollLock(isChecked());
	}
	
	public void dispose() {
		fConsoleView = null;
	}
	
}