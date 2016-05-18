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
package melnorme.lang.tooling.commands;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.CoreUtil.areEqual;

import melnorme.utilbox.collections.HashMap2;
import melnorme.utilbox.misc.HashcodeUtil;

public class EnvironmentSettings {
	
	public final HashMap2<String, String> envVars;
	public boolean appendEnv;
	
	public EnvironmentSettings() {
		this(new HashMap2<>(), true);
	}
	
	public EnvironmentSettings(HashMap2<String, String> envVars, boolean appendEnv) {
		this.envVars = assertNotNull(envVars);
		this.appendEnv = appendEnv;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof EnvironmentSettings)) return false;
		
		EnvironmentSettings other = (EnvironmentSettings) obj;
		
		return 
			areEqual(envVars, other.envVars) &&
			areEqual(appendEnv, other.appendEnv);
	}
	
	@Override
	public int hashCode() {
		return HashcodeUtil.combinedHashCode(envVars, appendEnv);
	}
	
}