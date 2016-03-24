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
package melnorme.lang.ide.core.operations.build;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

public class BuildTarget {
	
	protected final BuildTargetDataView data;
	
	public BuildTarget(BuildTargetData data) {
		assertNotNull(data.targetName);
		
		this.data = new BuildTargetData(data);
	}
	
	@Override
	public String toString() {
		return data.getTargetName() + (data.isEnabled() ? " [ENABLED]" : "");
	}
	
	/* -----------------  ----------------- */
	
	public String getTargetName() {
		return assertNotNull(data.getTargetName());
	}
	
	public boolean isEnabled() {
		return data.isEnabled();
	}
	
	public String getBuildArguments() {
		return data.getBuildArguments();
	}
	
	public String getCheckArguments() {
		return data.getCheckArguments();
	}
	
	public String getExecutablePath() {
		return data.getExecutablePath();
	}
	
	public BuildTargetDataView getData() {
		return data;
	}
	
	public BuildTargetData getDataCopy() {
		return new BuildTargetData(data);
	}
	
}