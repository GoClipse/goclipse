/*******************************************************************************
 * Copyright (c) 2010 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.utilbox.misc;

import melnorme.utilbox.core.CoreUtil;


/**
 * A Tuple of two elements
 */
public final class Pair<T1, T2> {
	
	private final T1 first;
	private final T2 second;
	
	public static <T1, T2> Pair<T1, T2> create(T1 first, T2 second) {
		return new Pair<T1, T2>(first, second);
	}
	
	public Pair(T1 first, T2 second) {
		this.first = first;
		this.second = second;
	}
	
	public T1 getFirst() {
		return first;
	}
	
	public T2 getSecond() {
		return second;
	}
	
	public Object getElement(int n) {
		return (n == 0) ? first : second;
	}
	
	@Override
	public final boolean equals(Object obj) {
		if (!(obj instanceof Pair<?, ?>)) {
			return false;
		}
		Pair<?, ?> other = (Pair<?,?>) obj;
		return CoreUtil.areEqual(first, other.first) && CoreUtil.areEqual(second, other.second);
	}
	
	@Override
	public int hashCode() {
		return HashcodeUtil.combineHashCodes(first.hashCode(), second.hashCode());
	}
	
	@Override
	public String toString() {
		return "<" + getFirst() + "," + getSecond() + ">";
	}
}