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
package melnorme.lang.ide.core.operations;

public class BuildTarget {
	
	protected final boolean enabled;
	protected final String targetName;
	
	public BuildTarget(boolean enabled, String targetName) {
		this.enabled = enabled;
		this.targetName = targetName;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public String getTargetName() {
		return targetName;
	}
	
}