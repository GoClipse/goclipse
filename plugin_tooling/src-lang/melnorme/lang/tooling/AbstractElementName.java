/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
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
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import static melnorme.utilbox.core.CoreUtil.areEqual;

import melnorme.utilbox.misc.StringUtil;

/**
 * A qualfied name of an element, usually a module, compilation unit, or a package.
 */
public class AbstractElementName {
	
	protected final String elementName;
	protected final String separator;
	protected final String[] segments; // cached
	
	public AbstractElementName(String elementName, String separator) {
		this(elementName, separator, StringUtil.splitString(elementName, separator.charAt(0)));
	}
	
	/** Note: the new class will own segments array, it should not be modified. */
	public AbstractElementName(String[] segments, String separator) {
		this(StringUtil.collToString(segments, separator), separator, segments);
	}
	
	protected AbstractElementName(String elementName, String separator, String[] segments) {
		this.elementName = assertNotNull(elementName);
		this.separator = assertNotNull(separator);
		this.segments = assertNotNull(segments);
		
		assertTrue(segments.length > 0);
		for (String segment : segments) {
			assertNotNull(segment);
			assertTrue(segment.contains(separator) == false);
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof AbstractElementName)) return false;
		
		AbstractElementName other = (AbstractElementName) obj;
		
		return areEqual(elementName, other.elementName) && areEqual(separator, other.separator);
	}
	
	@Override
	public int hashCode() {
		// We use hashcode of moduleFullName instead of using segments since it's cached.
		// This might cause colisions with segments with '.' in them, but that's a totally unimportant case.
		return elementName.hashCode();
	}
	
	@Override
	public String toString() {
		return "[" + elementName + "]";
	}
	
	/* ----------------- ----------------- */
	
	public String getLastSegment() {
		return segments[segments.length - 1];
	}
	
	public String getFullNameAsString() {
		return elementName;
	}
	
}