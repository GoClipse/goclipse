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
package melnorme.lang.tooling.bundle;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.CoreUtil.areEqual;
import static melnorme.utilbox.misc.StringUtil.nullAsEmpty;

import java.text.MessageFormat;

import melnorme.utilbox.misc.HashcodeUtil;

public class DependencyRef {
	
	protected final String bundleName;
	protected final String version;
	protected final boolean optional;
	
	public DependencyRef(String bundleName, String version) {
		this(bundleName, version, false);
	}
	
	public DependencyRef(String bundleName, String version, boolean optional) {
		this.bundleName = assertNotNull(bundleName);
		this.version = version;
		this.optional = optional;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof DependencyRef)) return false;
		
		DependencyRef other = (DependencyRef) obj;
		
		return 
			areEqual(bundleName, other.bundleName) &&
			areEqual(version, other.version) &&
			areEqual(optional, other.optional)
		;
	}
	
	@Override
	public int hashCode() {
		return HashcodeUtil.combinedHashCode(bundleName, version, optional);
	}
	
	@Override
	public String toString() {
		return MessageFormat.format("{0}@{1}{2}", bundleName, nullAsEmpty(version), optional ? " OPT" : "");
	}
	
	/* -----------------  ----------------- */
	
	public String getBundleName() {
		return bundleName;
	}
	
	public String getVersion() {
		return version;
	}
	
	public boolean getIsOptional() {
		return optional;
	}
	
}