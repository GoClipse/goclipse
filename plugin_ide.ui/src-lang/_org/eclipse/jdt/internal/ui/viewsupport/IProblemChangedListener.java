/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package _org.eclipse.jdt.internal.ui.viewsupport;

import org.eclipse.core.resources.IResource;

/**
 * Can be added to a ProblemMarkerManager to get notified about problem
 * marker changes. Used to update error ticks.
 */
public interface IProblemChangedListener {
	
	/**
	 * Called when problems changed. This call is posted in an aynch exec, therefore passed
	 * resources may not exist.
	 * 
	 * @param changedResources  A set with elements of type <code>IResource</code> that
	 * describe the resources that had an problem change.
	 * @param isMarkerChange If set to <code>true</code>, the change was a marker change, if
	 * <code>false</code>, the change came from an annotation model modification.
	 * 
	 * @param calledFromDisplayThread always true (always called from Display thread);
	 */
	void problemsChanged(IResource[] changedResources, boolean isMarkerChange, boolean calledFromDisplayThread);
	
}