/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
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

import java.util.Iterator;
import java.util.RandomAccess;

import melnorme.utilbox.collections.ArrayView;
import melnorme.utilbox.misc.StringUtil;

/**
 * A qualfied name of an element, usually a module, compilation unit, or a package.
 */
public class AbstractElementName2 implements RandomAccess, Iterable<String> {
	
	protected final String elementName;
	protected final String separator;
	protected final ArrayView<String> segments; // cached
	
	public AbstractElementName2(String elementName, String separator) {
		this(elementName, separator, StringUtil.splitString(elementName, separator.charAt(0)));
	}
	
	/** Note: the new class will own segments array, it should not be modified. */
	public AbstractElementName2(String[] segments, String separator) {
		this(StringUtil.collToString(segments, separator), separator, segments);
	}
	
	protected AbstractElementName2(String elementName, String separator, String[] segments) {
		this(elementName, separator, ArrayView.create(segments));
	}
	
	protected AbstractElementName2(String elementName, String separator, ArrayView<String> segments) {
		this.separator = assertNotNull(separator);
		this.elementName = assertNotNull(elementName);
		this.segments = assertNotNull(segments);
		
		assertTrue(segments.size() > 0);
		for (String segment : segments) {
			assertNotNull(segment);
			assertTrue(segment.contains(separator) == false);
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof AbstractElementName2)) return false;
		
		AbstractElementName2 other = (AbstractElementName2) obj;
		
		return areEqual(elementName, other.elementName) && areEqual(separator, other.separator);
	}
	
	@Override
	public int hashCode() {
		// We use hashcode of moduleFullName instead of using segments since it's cached.
		// This might cause colisions with segments with '.' in them, but that's a totally unimportant case.
		return elementName.hashCode();
	}
	
	public String getLastSegment() {
		return segments.get(segments.size() - 1);
	}
	
	public ArrayView<String> getSegments() {
		return segments;
	}
	
	public String getSegment(int index) {
		return segments.get(index);
	}
	
	public String getFullNameAsString() {
		return elementName;
	}
	
	@Override
	public Iterator<String> iterator() {
		return segments.iterator();
	}
	
	/* ----------------- ----------------- */
	
	@Override
	public String toString() {
		return "[" + elementName + "]";
	}
	
}