/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.tooling.common;

import melnorme.utilbox.misc.Location;

public interface ISourceBuffer {
	
	public abstract Location getLocation_orNull();
	
	public abstract String getSource();
	
	public abstract boolean isEditable();
	
	public abstract boolean isDirty();
	
	public abstract boolean trySaveBuffer();
	
}