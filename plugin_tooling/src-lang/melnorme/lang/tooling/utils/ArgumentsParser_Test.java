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
package melnorme.lang.tooling.utils;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertEquals;
import static melnorme.utilbox.core.CoreUtil.list;

import org.junit.Test;

import melnorme.utilbox.collections.Indexable;

public class ArgumentsParser_Test {
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() throws Exception {
		assertEquals(parse(""), list());
		assertEquals(parse(" "), list());
		assertEquals(parse("    \t "), list());
		
		assertEquals(parse("foo bar"), list("foo", "bar"));
		assertEquals(parse("foo  'bar'  |thr#ee| "), list("foo", "bar", "thr#ee"));
		assertEquals(parse("'#_## #''|#_## #|| || |#||"), list("#_# '", "#_# |", "", "|"));
	}
	
	protected Indexable<String> parse(String source) {
		return new ArgumentsParser('\'', '|', '#').parseFrom(source);
	}
	
}