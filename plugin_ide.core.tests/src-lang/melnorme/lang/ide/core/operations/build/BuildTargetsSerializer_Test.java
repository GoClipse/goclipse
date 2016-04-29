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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;

import org.junit.Test;

import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.HashMap2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Pair;
import melnorme.utilbox.tests.CommonTest;

public class BuildTargetsSerializer_Test extends CommonTest {
	
	private static final String STRING_ENCODING__ALL_ASCII = "value:a;b\\\"()dff{}[]";
	
	public static CommandInvocation cmd(String commandArguments) {
		if(commandArguments == null) {
			return null;
		}
		HashMap2<String, String> map = new HashMap2<>();
		map.put("MY_ENV_VAR", STRING_ENCODING__ALL_ASCII);
		return new CommandInvocation(commandArguments, map, true);
	}
	
	public static CommandInvocation cmd(String commandArguments, boolean append, Pair<String, String>[] entries) {
		HashMap2<String, String> envMap = null;
		
		if(entries != null) {
			envMap = new HashMap2<>();
			for (Pair<String,String> pair : entries) {
				envMap.put(pair.getFirst(), pair.getSecond());
			}
		}
		return new CommandInvocation(commandArguments, envMap, append);
	}
	
	public static Pair<String, String> entry(String key, String value) {
		return new Pair<>(key, value);
	}
	
	@Test
	public void test_CommandInvocation() throws Exception { test_CommandInvocation$(); }
	public void test_CommandInvocation$() throws Exception {
		testSerializeCmd(null);
		testSerializeCmd(cmd("", true, null));
		testSerializeCmd(cmd("", false, null));
		testSerializeCmd(cmd("asdf", true, array()));
		
		testSerializeCmd(cmd("asdf", true, array(
			entry("one", "1")
		)));
		testSerializeCmd(cmd("asdf", true, array(
			entry("one", "")
		)));
		testSerializeCmd(cmd("asdf", true, array(
			entry("one", "1"),
			entry("two", ""),
			entry(STRING_ENCODING__ALL_ASCII, STRING_ENCODING__ALL_ASCII)
		)));
	}
	
	protected void testSerializeCmd(CommandInvocation command) {
		CommandInvocationSerializer serializer = new CommandInvocationSerializer();
		try {
			String xml = serializer.writeToString(command);
			assertAreEqual(command, serializer.readFromString(xml));
		} catch(CommonException e) {
			assertFail();
		}
	}
	
	
	public static BuildTargetData bt(String targetName, boolean enabled, boolean autoEnabled, 
			String buildArguments, String executablePath) {
		return btd(targetName, enabled, autoEnabled, cmd(buildArguments), executablePath);
	}
	
	public static BuildTargetData btd(String targetName, boolean enabled, boolean autoEnabled, 
			CommandInvocation buildCommand, String executablePath) {
		BuildTargetData bt = new BuildTargetData(
			targetName, 
			enabled, 
			autoEnabled,
			buildCommand, 
			executablePath
		);
		
		assertEquals(bt, bt);
		assertEquals(bt, new BuildTargetData().setData(bt));
		return bt;
	}
	
	protected BuildTargetsSerializer serializer = new BuildTargetsSerializer();
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() throws Exception {
		testSerialize(new ArrayList2<>());
		testSerialize(new ArrayList2<>(btd("", false, false, null, null)));
		testSerialize(new ArrayList2<>(btd("", true, true, cmd("-opt"), "foo.exe")));
		testSerialize(new ArrayList2<>(
				btd("", false, true, cmd(""), ""),
				btd("blah", true, false, cmd("-opt"), "foo.exe"),
				btd("xxx", true, false, null, "foo/bar.ooo")
		));
	}
	
	protected void testSerialize(ArrayList2<BuildTargetData> buildTargetsData) {
		try {
			String xml = serializer.writeToString(buildTargetsData);
			assertAreEqual(buildTargetsData, serializer.readFromString(xml));
		} catch(CommonException e) {
			assertFail();
		}
	}
	
}