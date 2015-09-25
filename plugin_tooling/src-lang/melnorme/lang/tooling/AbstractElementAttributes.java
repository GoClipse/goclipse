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
package melnorme.lang.tooling;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.CoreUtil.areEqual;

import java.util.EnumSet;

import melnorme.lang.tooling.EAttributeFlag;
import melnorme.lang.tooling.EProtection;
import melnorme.lang.tooling.ElementAttributes;
import melnorme.utilbox.misc.HashcodeUtil;
import melnorme.utilbox.misc.StringUtil;


/** 
 * A lightweight description of element attributes. 
 * Not intended for use in semantic analasys, but only as a UI model. 
 */
abstract class AbstractElementAttributes {
	
	protected final EProtection protection;
	protected final EnumSet<EAttributeFlag> flagsSet;
	
	public AbstractElementAttributes(EProtection protection, EnumSet<EAttributeFlag> flagsSet) {
		this.protection = protection;
		this.flagsSet = assertNotNull(flagsSet);
	}
	
	public AbstractElementAttributes(EProtection protection, EAttributeFlag... flagsArray) {
		this(protection, newFlagsSet(flagsArray));
	}
	
	public EProtection getProtection() {
		return protection;
	}
	
	public Iterable<EAttributeFlag> getFlagsSet() {
		return flagsSet;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof ElementAttributes)) return false;
		
		ElementAttributes other = (ElementAttributes) obj;
		
		return areEqual(protection, other.protection) && areEqual(flagsSet, other.flagsSet);
	}
	
	@Override
	public int hashCode() {
		return HashcodeUtil.combinedHashCode(protection, flagsSet);
	}
	
	@Override
	public String toString() {
		return "FLAGS[" + getProtection() + ";" + StringUtil.collToString(flagsSet, ",") + "]";
	}
	
	/* -----------------  ----------------- */
	
	public boolean hasFlag(EAttributeFlag flag) {
		return flagsSet.contains(flag);
	}
	
	protected void setFlag(EAttributeFlag flag, boolean enabled) {
		if(enabled) {
			flagsSet.add(flag);
		} else {
			flagsSet.remove(flag);
		}
	}
	
	/* -----------------  helpers  ----------------- */
	
	public static EnumSet<EAttributeFlag> newFlagsSet(EAttributeFlag... flagsArray) {
		assertNotNull(flagsArray);
		EnumSet<EAttributeFlag> flags = EnumSet.noneOf(EAttributeFlag.class);
		
		for(EAttributeFlag flag : flagsArray) {
			flags.add(flag);
		}
		return flags;
	}
	
}