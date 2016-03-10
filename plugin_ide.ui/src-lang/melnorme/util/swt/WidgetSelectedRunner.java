/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.util.swt;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class WidgetSelectedRunner extends SelectionAdapter {
	
	protected final Runnable runnable;
	
	public WidgetSelectedRunner(Runnable runnable) {
		this.runnable = assertNotNull(runnable);
	}
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		runnable.run();
	}
	
}